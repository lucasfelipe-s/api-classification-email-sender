package br.com.vivo.challengeform.service;

import br.com.vivo.challengeform.dto.SkillDTO;
import br.com.vivo.challengeform.dto.UserDTO;
import br.com.vivo.challengeform.exceptions.resources.ResourceNotFoundException;
import br.com.vivo.challengeform.entities.User;
import br.com.vivo.challengeform.repository.UserRepository;
import br.com.vivo.challengeform.service.emailService.SendEmailService;
import br.com.vivo.challengeform.service.emailService.messages.EmailMessages;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final SendEmailService sendEmailService;

    public UserService(UserRepository userRepository, SendEmailService sendEmailService) {
        this.userRepository = userRepository;
        this.sendEmailService = sendEmailService;
    }

    /**
     * Metodo que retorna a lista de usuários completa.
     * @return Lista de usuários.
     */
    public List<UserDTO> getUsers(){

        //Retorna uma lista de usuário
        List<User> users =  userRepository.findAll();

        // Para cada usuário eu faço um map
        return users.stream()
                .map(user -> new ModelMapper().map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Metodo que retorna um usuário pelo seu Id.
     * @param id do usuário que será localizado.
     * @return Retorna um usuário pelo seu Id, caso sejá encontrado.
     */
    public Optional<UserDTO> getUserById(long id) {

        //Obtendo optional de user pelo id
        Optional<User> user = userRepository.findById(id);

        //se não encontrar, lança exception
        if(user.isEmpty()){
            throw new ResourceNotFoundException("Usuário com id: "+id+" não encontrado");
        }

        //convertendo o optional de user em um userDTO.
        // ModelMapper é a dependencia que transforma automaticamente uma classe em outra
        // .map faz a conversão
        // Para tirar um user de um Optional usa o metodo get()
        UserDTO userDTO = new ModelMapper().map(user.get(), UserDTO.class);

        //criando e retornando um optional de userDTO
        return Optional.of(userDTO);

    }

    /**
     * Metodo que adiciona um usuário no banco de dados, gerando um novo Id para ele.
     * @param userDTO Usuário que será adicionado no banco de dados.
     * @return Usuário que foi adicionado.
     */
    public UserDTO addUser(@Valid UserDTO userDTO) {

        //crio um objeto de mapeamento
        ModelMapper mapper = new ModelMapper();

        // converto o userDTo em um user
        User user = mapper.map(userDTO, User.class);

        //salvo o user no banco
        user = userRepository.save(user);

        userDTO.setId(user.getId());

        //retorno o DTO
        return userDTO;
    }

    /**
     * Metodo que envia um email para algum usuario
     * @param user usuario que receberá o email
     */
    public void sendEmailToUser(@Valid UserDTO user) {
        String resultMessage = classifyUser(user);
        this.sendEmailService.sendEmail(user.getEmail(),
                EmailMessages.createTitle(user),
                EmailMessages.messageToNewCandidate(user, resultMessage));
    }

    /**
     * Metodo que remove um usuário do banco de dados pelo Id.
     * @param id do usuário que será removido.
     */
    public void deleteUser(long id) {
        // verificar se o usuário existe
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty()){
            throw new ResourceNotFoundException("Não foi possível deletar um usuário com o id:"+id+" - Usuário não existe");
        }

        userRepository.deleteById(id);
    }

    /** Metodo que classifica o nível de habilidade de um usuário e envia um e-mail de acordo com o resultado dos niveis da suas habilidades.
     * @param user Usuário que será avaliado
     * @return Retorna um email classificando um usuário de acordo com os seus níveis de habilidade.
    */
    public String classifyUser(UserDTO user) {
        boolean isFrontEndDev = false;
        boolean isBackEndDev = false;
        int counterBackEndSkills = 0;
        int counterFrontEndSkills = 0;

        for (SkillDTO skill : user.getSkills()) {

            if (verifySkillMatchAndRatingInRange(skill,"Java")
                    || verifySkillMatchAndRatingInRange(skill,"C#")
                    || verifySkillMatchAndRatingInRange(skill,"GOLANG")) {
                counterBackEndSkills++;
                continue;
            }

            if (verifySkillMatchAndRatingInRange(skill,"JavaScript")
                    || verifySkillMatchAndRatingInRange(skill,"vue")
                    || verifySkillMatchAndRatingInRange(skill,"php")) {
                counterFrontEndSkills++;
            }
        }

        int amountBackEndSkills = 3;
        int amountFrontEndSkills = 3;
        if(counterBackEndSkills == amountBackEndSkills) {
            isBackEndDev = true;
        }

        if(counterFrontEndSkills == amountFrontEndSkills) {
            isFrontEndDev = true;
        }

        if (isBackEndDev && isFrontEndDev) {
            return "Você foi classificado como Desenvolvedor FullStack, entraremos em contato.";
        } else if (isBackEndDev) {
            return "Você foi classificado como Desenvolvedor BackEnd, entraremos em contato.";
        } else if (isFrontEndDev) {
            return "Você foi classificado como Desenvolvedor FrontEnd, entraremos em contato.";
        } else
            return "Infelizmente não encontramos uma vaga para seu perfil, entraremos em contato em breve, caso apareça uma oportunidade.";
    }

    /**
     * Metodo que verifica se o nome de uma habilidade é compativel ao nome de alguma habilidade específica e se o nível dessa habilidade está dentro de um determinado intervalo.
     * @param skill Habilidade que será verificada.
     * @param skillName Nome da habilidade específica que será feita a comparação.
     * @return Retorna verdadeiro ou falso.
     */
    public boolean verifySkillMatchAndRatingInRange(SkillDTO skill, String skillName) {
        return skillsNamesMatch(skill, skillName) && skillRatingInRange(skill);
    }

    /**
     * Metodo que verifica se uma habilidade está entre a faixa de classificação
     * adequada.
     *
     * @param skill Habilidade que será verificada.
     * @return Retorna verdadeiro ou falso.
     */
    public boolean skillRatingInRange(SkillDTO skill) {
        return skill.getRating() >= 7 && skill.getRating() <= 10;
    }

    /**
     * Metodo que recebe uma SkillDTO e verifica se o nome dela é igual ao nome de
     * alguma habilidade específica (ignorando maiuscula e minuscula).
     *
     * @param skill Habilidade que gostaria de comparar.
     * @param skillName Nome da habilidade que gostaria de comparar.
     * @return Retorna verdadeiro ou falso.
     */
    public boolean skillsNamesMatch(SkillDTO skill, String skillName) {
        return skill.getName().equalsIgnoreCase(skillName);
    }

}
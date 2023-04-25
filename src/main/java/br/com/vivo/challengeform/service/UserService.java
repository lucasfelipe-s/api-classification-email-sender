package br.com.vivo.challengeform.service;

import br.com.vivo.challengeform.dto.LevelDTO;
import br.com.vivo.challengeform.dto.TechnologyDTO;
import br.com.vivo.challengeform.entities.Level;
import br.com.vivo.challengeform.enums.TechnologyTypes;
import br.com.vivo.challengeform.dto.UserDTO;
import br.com.vivo.challengeform.entities.Technology;
import br.com.vivo.challengeform.exceptions.resources.ResourceBadRequestException;
import br.com.vivo.challengeform.exceptions.resources.ResourceNotFoundException;
import br.com.vivo.challengeform.entities.User;
import br.com.vivo.challengeform.repository.TechnologyRepository;
import br.com.vivo.challengeform.repository.UserRepository;
import br.com.vivo.challengeform.service.emailService.SendEmailService;
import br.com.vivo.challengeform.service.emailService.messages.EmailMessages;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final TechnologyRepository technologyRepository;
    private final SendEmailService sendEmailService;

    public UserService(UserRepository userRepository, TechnologyRepository technologyRepository, SendEmailService sendEmailService) {
        this.userRepository = userRepository;
        this.technologyRepository = technologyRepository;
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

//        ModelMapper modelMapper = new ModelMapper();
//        User user = modelMapper.map(userDTO, User.class);

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        List<LevelDTO> userDTOLevels = userDTO.getLevels();
        isTechnologyInDataBase(userDTOLevels);
        verifyListAndDatabaseSizeMatch(userDTOLevels);

        List<Level> levels = new ArrayList<>();
        for (LevelDTO levelDTO : userDTOLevels) {
            Level level = new Level();
            level.setValue(levelDTO.getValue());
            level.setTechnology(technologyRepository.findFirstByName(levelDTO.getTechnology().getName()));
            level.setUser(user);
            levels.add(level);
        }
        user.setLevels(levels);

        user = userRepository.save(user);
        userDTO.setId(user.getId());

        return userDTO;
    }

    /**
     * Verifica se as tecnologias presentes na lista de LevelDTO estão cadastradas no banco de dados.
     * Caso alguma tecnologia não esteja cadastrada é lançada uma exceção indicando o erro.
     *
     * @param levelDTO uma lista de LevelDTO contendo as tecnologias a serem verificadas.
     */
    public void isTechnologyInDataBase(List<LevelDTO> levelDTO){
        boolean nameTechnologyIsValid;
        boolean typeTechnologyIsValid;

        for(LevelDTO level: levelDTO) {
            nameTechnologyIsValid = false;
            typeTechnologyIsValid = false;


            String technologyName = level.getTechnology().getName();
            TechnologyTypes technologyType = level.getTechnology().getType();


            List<Technology> availableTechnologies = technologyRepository.findAll();

            for (Technology availableTechnology : availableTechnologies) {
                if (technologyName.equalsIgnoreCase(availableTechnology.getName())) {
                    nameTechnologyIsValid = true;
                }
                if (technologyType.toString().equalsIgnoreCase(availableTechnology.getType().toString())) {
                    typeTechnologyIsValid = true;
                }
            }

            if (!nameTechnologyIsValid) {
                throw new ResourceBadRequestException("O nome da habilidade '" + technologyName + "' está incorreto");
            }

            if (!typeTechnologyIsValid) {
                throw new ResourceBadRequestException("O tipo da habilidade '" + technologyName + "' está incorreto");
            }

        }

    }

    public void verifyListAndDatabaseSizeMatch(List<LevelDTO> levelDTO){

        if(levelDTO.size() < technologyRepository.count()){
            throw new ResourceBadRequestException("A quantidade de habilidades está incorreta, você precisa adicionar +"
                    + (technologyRepository.count() - levelDTO.size())+" habilidade(s)");
        } else if(levelDTO.size() > technologyRepository.count()){
            throw new ResourceBadRequestException("A quantidade de habilidades está incorreta, você precisa remover "
                    + (levelDTO.size() - technologyRepository.count())+" habilidade(s)");
        }
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

    /** Metodo que classifica o nível de habilidade de um usuário e envia um e-mail de acordo com o resultado dos niveis da suas habilidades.
     * @param user Usuário que será avaliado
     * @return Retorna um email classificando um usuário de acordo com os seus níveis de habilidade.
    */
    public String classifyUser(UserDTO user) {
        boolean isBackEndDev = false;
        boolean isFrontEndDev = false;
        int counterBackEndTechnologies = 0;
        int counterFrontEndTechnologies = 0;

        for (LevelDTO level: user.getLevels()) {
            if(isTechnologyTypeMatch(level,TechnologyTypes.BACK_END)){
                if (verifyTechnologyMatchAndLevelInRange(level.getTechnology(), level)) {
                    counterBackEndTechnologies++;
                }
            }else if (isTechnologyTypeMatch(level,TechnologyTypes.FRONT_END)){
                if (verifyTechnologyMatchAndLevelInRange(level.getTechnology(), level)) {
                    counterFrontEndTechnologies++;
                }
            }
        }

        Map<TechnologyTypes, Long> counts = countTechnologiesByType();
        Long amountBackEndTechnologies = counts.get(TechnologyTypes.BACK_END);
        Long amountFrontEndTechnologies = counts.get(TechnologyTypes.FRONT_END);
        if(counterBackEndTechnologies == amountBackEndTechnologies) {
            isBackEndDev = true;
        }

        if(counterFrontEndTechnologies == amountFrontEndTechnologies) {
            isFrontEndDev = true;
        }

        if (isBackEndDev && isFrontEndDev) {
            setUserProfileTechnologyType(user,TechnologyTypes.FULL_STACK);
            return "Você foi classificado como Desenvolvedor FullStack, entraremos em contato.";
        } else if (isBackEndDev) {
            setUserProfileTechnologyType(user,TechnologyTypes.BACK_END);
            return "Você foi classificado como Desenvolvedor BackEnd, entraremos em contato.";
        } else if (isFrontEndDev) {
            setUserProfileTechnologyType(user,TechnologyTypes.FRONT_END);
            return "Você foi classificado como Desenvolvedor FrontEnd, entraremos em contato.";
        } else
            return "Infelizmente não encontramos uma vaga para seu perfil, entraremos em contato em breve, caso apareça uma oportunidade.";
    }

    /**
     * Verifica se o tipo da tecnologia é igual ao tipo especificado no parâmetro.
     *
     * @param level o objeto LevelDTO a ser verificado
     * @param technologyType o tipo de tecnologia a ser comparado com o tipo da tecnologia do LevelDTO
     * @return true se o tipo da tecnologia do LevelDTO é igual ao tipo especificado, false caso contrário
     */
    public boolean isTechnologyTypeMatch(LevelDTO level, TechnologyTypes technologyType) {
        return level.getTechnology().getType() == technologyType;
    }

    //=============POSSIVEL MELHORIA, UNIFICAR OS METODOS=================
    /**
     Verifica se a tecnologia informada é compatível com as tecnologias existentes
     e se o nível informado está dentro da faixa permitida.
     @param technology a tecnologia a ser verificada
     @param level o nível a ser verificado
     @return true se a habilidade e o nível estão dentro dos limites estabelecidos, false caso contrário
     */
    public boolean verifyTechnologyMatchAndLevelInRange(TechnologyDTO technology, LevelDTO level) {
        return technologyNamesMatch(technology) && levelInRange(level);
    }

    /**
     Verifica se o nível de habilidade contido no objeto LevelDTO está dentro da faixa permitida.
     @param level Objeto LevelDTO contendo as informações do nível de habilidade a ser verificado.
     @return true se o nível de habilidade estiver dentro da faixa permitida, false caso contrário.
     */
    public boolean levelInRange(LevelDTO level) {
        return level.getValue() >= 7 && level.getValue() <= 10;
    }

    /**
     Verifica se o nome da tecnologia no objeto TechnologyDTO passado como parâmetro
     corresponde a algum dos tipos de tecnologia pré-definidos.
     @param technologyDTO objeto contendo informações sobre a tecnologia
     @return true se o nome da tecnologia corresponder a algum dos tipos pré-definidos, false caso contrário
     */
    public boolean technologyNamesMatch(TechnologyDTO technologyDTO) {
        List<Technology> technologies = technologyRepository.findAll();

        for(Technology technology: technologies){
            return technologyDTO.getName().equalsIgnoreCase(technology.getName());
        }
        return false;
    }
    //=============FIM DA POSSIVEL MELHORIA=================

    /**
     Retorna um mapa que contém o número de habilidades por tipo de habilidade.
     @return um Mapa em que as chaves são os tipos de habilidade e os valores são o número de habilidades de cada tipo.
     */
    public Map<TechnologyTypes, Long> countTechnologiesByType() {
        return technologyRepository.countByTechnologyType();
    }

    /**
     Define o tipo de tecnologia do perfil do usuário.
     @param user o usuário a ser atualizado.
     @param technologyType o tipo de tecnologia a ser definido.
     */
    public void setUserProfileTechnologyType(UserDTO user, TechnologyTypes technologyType) {
        user.setProfile(technologyType);
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
}
package br.com.vivo.challengeform.validator;

import br.com.vivo.challengeform.dto.UserDTO;
import br.com.vivo.challengeform.exceptions.resources.ResourceBadRequestException;
import br.com.vivo.challengeform.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class UserValidator implements ConstraintValidator<ValidUser, UserDTO> {

    private final UserRepository userRepository;
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(UserDTO user, ConstraintValidatorContext context) {

        return validateUserName(user) && validateEmail(user) && validateLevels(user);

    }

    /**
     * Verifica se o usuário tem níveis de habilidade definidos.
     * @param user O usuário a ser verificado.
     * @return True se o usuário tiver níveis de habilidade definidos, false caso contrário.
     */
    public boolean validateLevels(UserDTO user) {
        if (user.getLevels() == null || user.getLevels().isEmpty()){
            throw new ResourceBadRequestException("O campo 'levels' não pode estar vazio");
        }
        return true;
    }

    /**
     * Método responsável por validar o nome de um usuário.
     * @param user o objeto UserDTO contendo o nome do usuário a ser validado.
     * @return true se o nome do usuário é válido, lança uma exceção caso contrário.
     */
    public boolean validateUserName(UserDTO user){

        if(user.getName() == null || user.getName().isBlank()) {
            throw new ResourceBadRequestException("O campo 'nome' não pode estar vazio");
        }

        if(user.getName().length() < 2 || user.getName().length() > 50) {
            throw new ResourceBadRequestException("O campo 'nome' deve ter entre 2 e 50 caracteres");
        }

        return true;
    }

    /**
     * Método responsável por validar o email de um usuário.
     * @param user o objeto UserDTO contendo o nome do usuário a ser validado.
     * @return true se o nome do usuário é válido, lança uma exceção caso contrário.
     */
    public boolean validateEmail(UserDTO user){

        String userEmail = user.getEmail();

        if (userEmail == null) {
            throw new ResourceBadRequestException("O campo 'email' não pode estar nulo!");
        }else if (userEmail.isBlank()) {
            throw new ResourceBadRequestException("O campo 'email' não pode estar vazio!");
        }else if (!userEmail.contains("@")
                || userEmail.indexOf("@") < 1
                || !userEmail.contains(".")
                || userEmail.indexOf(".") < 3
                || userEmail.contains(" ") ){
            throw new ResourceBadRequestException("O campo 'email' está inválido!");
        }

        if(userRepository.existsByEmailIgnoreCase(userEmail)){
            throw new ResourceBadRequestException("O Email inserido já está cadastrado!");
        }

        return true;
    }
}

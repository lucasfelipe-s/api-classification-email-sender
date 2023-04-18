package br.com.vivo.challengeform.validator;

import br.com.vivo.challengeform.dto.UserDTO;
import br.com.vivo.challengeform.exceptions.resources.ResourceBadRequestException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class UserValidator implements ConstraintValidator<ValidUser, UserDTO> {
    @Override
    public boolean isValid(UserDTO user, ConstraintValidatorContext context) {

        return validateUserName(user) && validateEmail(user);

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

        if (user.getEmail() == null) {
            throw new ResourceBadRequestException("O campo 'email' não pode estar nulo");
        }else if (user.getEmail().isBlank()) {
            throw new ResourceBadRequestException("O campo 'email' não pode estar vazio");
        }else if (!user.getEmail().contains("@")
                || user.getEmail().indexOf("@") < 1
                || !user.getEmail().contains(".")
                || user.getEmail().indexOf(".") < 3
                || user.getEmail().contains(" ") ){
            throw new ResourceBadRequestException("O campo 'email' está inválido");
        }

        return true;
    }
}

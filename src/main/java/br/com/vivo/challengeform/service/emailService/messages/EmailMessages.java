package br.com.vivo.challengeform.service.emailService.messages;

import br.com.vivo.challengeform.dto.UserDTO;

public class EmailMessages {

    /**
     * Adiciona o nome de um usuário em um título pradonizado de email.
     * @param user O usuário cujo nome será utilizado.
     * @return Uma String formatada contendo um texto e o nome do usuário.
     */
    public static String createTitle(UserDTO user) {
        return "Resultado da avaliação de habilidades do candidato: " + user.getName();
    }

    /**
     * Formata uma mensagem, adiciona o nome de um usuário no corpo da mensagem e adiciona uma mensagem personalizada.
     * @param user O usuário para o qual a mensagem está sendo enviada.
     * @param resultMessage A mensagem personalizada a ser adicionada à mensagem de email.
     * @return A mensagem formatada para ser enviada como email.
     */
    public static String messageToNewCandidate(UserDTO user, String resultMessage){
        return String.format("""
                Olá %s,

                Agradecemos por ter preenchido o formulário de avaliação de habilidades. Gostaríamos de compartilhar com você os resultados da sua avaliação:

                %s

                Muito obrigado pelo seu tempo e consideração.

                Atenciosamente,
                Desafio Formulário!""", user.getName(), resultMessage);
    }
}

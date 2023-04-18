package br.com.vivo.challengeform.service.emailService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j //criar logs de mensagens informativas
public class SendEmailService {

    private final JavaMailSender javaMailSender;

    public SendEmailService(final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Metodo que envia um e-mail.
     * @param to endereço de e-mail do destinatário
     * @param title título do e-mail
     * @param content conteúdo do e-mail
     */
    public void sendEmail(String to, String title, String content) {
        log.info("Enviando email...");

        var message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        javaMailSender.send(message);
        log.info("Email enviado com sucesso!");
    }
}

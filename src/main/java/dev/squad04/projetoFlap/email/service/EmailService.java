package dev.squad04.projetoFlap.email.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }

    public void sendPasswordResetCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("squad04flapdigital@gmail.com");
        message.setTo(to);
        message.setSubject("Seu código de Recuperação de Senha");
        message.setText(
                "Olá,\n\n" +
                        "Recebemos uma solicitação de redefinição de senha para sua conta.\n" +
                        "Use o código abaixo para criar uma nova senha:\n\n" +
                        "Código: " + code + "\n\n" +
                        "Este código é válido por 10 minutos.\n" +
                        "Se você não solicitou isso, pode ignorar este email com segurança."
        );
        mailSender.send(message);
    }

    public void sendWelcomeEmail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("squad04flapdigital@gmail.com");
        message.setTo(to);
        message.setSubject("Bem-vindo ao Flap Digital!");
        message.setText(
                "Olá " + name + ",\n\n" +
                        "Estamos muito felizes em tê-lo conosco no Flap Digital.\n" +
                        "Seja bem-vindo à nossa plataforma!\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe Flap!"
        );
        mailSender.send(message);
    }
}

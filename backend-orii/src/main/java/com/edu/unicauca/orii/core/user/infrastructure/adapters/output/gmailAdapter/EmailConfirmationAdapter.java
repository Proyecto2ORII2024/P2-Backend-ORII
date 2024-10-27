package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.gmailAdapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import com.edu.unicauca.orii.core.user.application.ports.output.IEmailConfirmationOutput;
import com.edu.unicauca.orii.core.user.domain.model.EmailToken;
import com.edu.unicauca.orii.core.user.domain.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailConfirmationAdapter implements IEmailConfirmationOutput {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Value("${api_url}")
    private String apiUrl;

    private void sendEmail(String mailAddress, String subject, String text) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        try {
            helper.setFrom(fromMail);
            helper.setTo(mailAddress);
            helper.setSubject(subject);
            helper.setText(text, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        javaMailSender.send(msg);
    }

    @Override
    public void sendConfirmationEmail(User user, EmailToken token) { 
        try {
            sendEmail(user.getEmail(), "Confirm your email", buildEmailContent(token.getToken()));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String buildEmailContent(String token){
        return "<div style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; width: 100%;\">\n" + //
                        "  <style>\n" + //
                        "    @media screen and (-webkit-min-device-pixel-ratio: 0) {\n" + //
                        "      a.email-button:hover {\n" + //
                        "        background-color: white !important;\n" + //
                        "        color: #07184a !important;\n" + //
                        "        border: 2px solid #07184a !important;\n" + //
                        "      }\n" + //
                        "    }\n" + //
                        "  </style>\n" + //
                        "\n" + //
                        "  <table style=\"border-spacing: 0; width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; border: 1px solid #e0e0e0;\">\n" + //
                        "    <!-- Header -->\n" + //
                        "    <tr>\n" + //
                        "      <td style=\"background-color: #07184a; color: white; text-align: center; padding: 20px; font-size: 24px;\">\n" + //
                        "        Activación de cuenta\n" + //
                        "      </td>\n" + //
                        "    </tr>\n" + //
                        "    \n" + //
                        "    <!-- Body -->\n" + //
                        "    <tr>\n" + //
                        "      <td style=\"padding: 20px; font-size: 16px; color: #333333;\">\n" + //
                        "        <p>Este correo electrónico es para confirmar tu registro en la página de la ORRI. Si eres tú, por favor da clic en el siguiente enlace o en el botón para verificar tu cuenta y completar el proceso de registro.</p>\n" + //
                        "        <p>Si no puedes acceder al enlace, copia y pega la siguiente URL en tu navegador: <a" + 
                        "        href=\"" + apiUrl + "/email/confirmEmail/" + token + "\" style=\"color: #07184a; text-decoration: none;\">" + apiUrl + "/email/confirmEmail/" + token + "</a></p>\n" + // 
                        "\n" + //
                        "        <a href=\"" + apiUrl + "/email/confirmEmail/" + token + "\" class=\"email-button\" style=\"display: inline-block; padding: 10px 20px; margin-top: 20px; background-color: #07184a; color: white; text-decoration: none; border-radius: 5px; border: 2px solid #07184a;\">Verificar mi cuenta</a>\n" + //
                        "\n" + //
                        "        <p>Si no eres tú quien se registró, por favor ignora este correo electrónico. No es necesario que tomes ninguna acción.</p>\n" + //
                        "      </td>\n" + //
                        "    </tr>\n" + //
                        "    \n" + //
                        "    <!-- Footer -->\n" + //
                        "    <tr>\n" + //
                        "      <td style=\"text-align: center; font-size: 12px; color: #777777; padding: 20px; background-color: #f4f4f4;\">\n" + //
                        "        <p>Por favor no responda a este correo electrónico.</p>\n" + //
                        "        <p>&copy; 2024 Universidad del Cauca. Todos los derechos reservados.</p>\n" + //
                        "      </td>\n" + //
                        "    </tr>\n" + //
                        "  </table>\n" + //
                        "</div>";
    }

}

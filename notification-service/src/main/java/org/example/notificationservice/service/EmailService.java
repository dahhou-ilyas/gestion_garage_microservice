package org.example.notificationservice.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String customerName){
        try{
            MimeMessage message=mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

            helper.setTo(to);
            helper.setSubject("Bienvenue chez Garage Auto Service !");

            String htmlContent = String.format("""
                <html>
                    <body>
                        <h2>Bienvenue %s !</h2>
                        <p>Nous sommes ravis de vous compter parmi nos clients.</p>
                        <p>Chez Garage Auto Service, nous nous engageons à fournir :</p>
                        <ul>
                            <li>Un service de qualité</li>
                            <li>Une expertise professionnelle</li>
                            <li>Une transparence totale</li>
                        </ul>
                        <p>N'hésitez pas à nous contacter pour toute question.</p>
                        <p>Cordialement,<br>L'équipe Garage Auto Service</p>
                    </body>
                </html>
                """, customerName);

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Welcome email sent successfully to {}", to);

        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", to, e.getMessage());
        }
    }
}

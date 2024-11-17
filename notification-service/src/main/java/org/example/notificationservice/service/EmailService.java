package org.example.notificationservice.service;

import jakarta.mail.MessagingException;
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

    public void sendCarRegistrationEmail(String to, String customerName, String carDetails) throws MessagingException {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Nouvelle voiture enregistrée - Garage Auto Service");

            String htmlContent = String.format("""
                <html>
                    <body>
                        <h2>Bonjour %s,</h2>
                        <p>Nous vous confirmons l'enregistrement de votre véhicule dans notre système.</p>
                        <div style="background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0;">
                            <h3 style="color: #2c3e50; margin-top: 0;">Détails du véhicule :</h3>
                            %s
                        </div>
                        <p>Notre équipe est à votre disposition pour tout entretien ou réparation nécessaire.</p>
                        <p>Pour prendre rendez-vous ou pour toute question, n'hésitez pas à nous contacter.</p>
                        <p style="margin-top: 20px;">Cordialement,<br>L'équipe Garage Auto Service</p>
                    </body>
                </html>
                """, customerName, carDetails);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Car registration confirmation email sent successfully to {}", to);
        }catch (Exception e) {
            log.error("Failed to send car registration email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

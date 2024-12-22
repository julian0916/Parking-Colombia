package co.zer.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {

    private static final Logger logger = LoggerFactory.getLogger(SendEmail.class);

    public static void send(final String to, final String subject, final String htmlMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String username = "apps@groupware.com.co";
                final String password = "groupware2811";

                Properties prop = new Properties();
                prop.put("mail.smtp.host", "smtp.gmail.com");
                prop.put("mail.smtp.port", "587");
                prop.put("mail.smtp.auth", "true");
                prop.put("mail.smtp.starttls.enable", "true"); //TLS

                Session session = Session.getInstance(prop,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                try {

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("apps@groupware.com.co"));
                    message.setRecipients(
                            Message.RecipientType.TO,
                            InternetAddress.parse(to)
                    );
                    message.setSubject(subject);
                    message.setContent(htmlMessage, "text/html; charset=utf-8");
                    //message.setText(text);

                    Transport.send(message);

                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
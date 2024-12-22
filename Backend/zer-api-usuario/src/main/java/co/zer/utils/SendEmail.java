package co.zer.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {

    public static void send(final String to, final String subject, final String htmlMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Recipient's email ID needs to be mentioned.
                String to = "edison.lopera@misena.edu.co";

                // Sender's email ID needs to be mentioned
                String from = "email.todos@gmail.com";

                // Assuming you are sending email from through gmails smtp
                String host = "smtp.gmail.com";

                // Get system properties
                Properties properties = System.getProperties();

                // Setup mail server
                properties.put("mail.smtp.host", host);
                properties.put("mail.smtp.port", "465");
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.auth", "true");

                // Get the Session object.// and pass username and password
                Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication("email.todos@gmail.com", "001ArgentoEscarto001");

                    }

                });

                // Used to debug SMTP issues
                session.setDebug(true);

                try {
                    // Create a default MimeMessage object.
                    MimeMessage message = new MimeMessage(session);

                    // Set From: header field of the header.
                    message.setFrom(new InternetAddress(from));

                    // Set To: header field of the header.
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                    // Set Subject: header field
                    message.setSubject("This is the Subject Line!");

                    // Now set the actual message
                    //message.setText("This is actual message");
                    message.setContent(
                            "<h1>This is actual message embedded in HTML tags</h1>",
                            "text/html");

                    System.out.println("sending...");
                    // Send message
                    Transport.send(message);
                    System.out.println("Sent message successfully....");
                } catch (MessagingException mex) {
                    mex.printStackTrace();
                }

            }
        }).start();

    }
}
/*
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
*/
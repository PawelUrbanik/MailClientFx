package utils;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Date;
import java.util.Properties;

public class EmailSender {

    private String username = "";
    private String password = "";
    private Properties prop;

    public EmailSender() {
        username = UsernameAndPasswd.getUsername();
        password = UsernameAndPasswd.getPassword();
        prop = getGmailProperties();
    }

    public void sendHTMLMessage(String toAddress, String subject, String body) {
        Session session = Session.getInstance(prop, new GmailAuthenticator(username, password));
        try {
            Message message = getMessage(session, username, toAddress, subject);
            message.setContent(body, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendTextMessage(String toAddress, String subject, String body) {
        Session session = Session.getInstance(prop, new GmailAuthenticator(username, password));
        try {
            Message message = getMessage(session, username, toAddress, subject);
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendAttachmentMessage(String toAddress, String subject, String body, String fileName) {
        Session session = Session.getInstance(prop, new GmailAuthenticator(username, password));
        try {
            Message message = getMessage(session, username, toAddress, subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            messageBodyPart = getBodyPart(fileName);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private BodyPart getBodyPart(String fileName) throws MessagingException {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileName);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        return messageBodyPart;
    }


    private Message getMessage(Session session, String fromAddress, String toAddress, String subject) throws AddressException, MessagingException
    {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromAddress));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
        message.setSubject(subject);
        message.setSentDate(new Date());
        return message;
    }
    private Properties getGmailProperties() {
        Properties properties= new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.debug", true);
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 465);
        properties.put("mail.smtp.ssl.enable", true);

        return properties;
    }

    private class GmailAuthenticator extends Authenticator {
        private String userName;
        private String password;

        GmailAuthenticator(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, password);
        }
    }
}

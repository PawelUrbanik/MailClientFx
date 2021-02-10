package utils;


import jakarta.mail.*;
import java.util.Properties;

public class EmailReader {

    private String username = "testerterster535@gmail.com";
    private String password = "Tester123";

    private Session session;
    private Store store;
    private Properties properties;

    public EmailReader(String username, String password) {
        this.username= username;
        this.password = password;
        properties = getGmailProperties();
        connect();
    }

    public void readEmails() {
        try {
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            for (int i = folder.getMessageCount(); i >0 ; i--) {
                Message message = folder.getMessage(i);
                System.out.printf("%1$td/%1$tm/%1$tY %1$tH:%1$tM:%1$tS %2$s %3$s %n",
                        message.getReceivedDate(), message.getFrom()[0].toString(),
                        message.getSubject() );
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static Properties getGmailProperties() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", 993);
        properties.put("incomingHost", "imap.gmail.com");
        return properties;
    }


    private void connect() {
        session = Session.getInstance(properties, new GmailAuthenticator(username, password));
        try {
            store =session.getStore();
            store.connect("imap.gmail.com", 993, username, password);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    private static class GmailAuthenticator extends Authenticator {
        private String userName;
        private String passwd;

        GmailAuthenticator(String userName, String password) {
            this.userName = userName;
            this.passwd = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(userName, passwd);
        }
    }

}

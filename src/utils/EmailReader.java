package utils;


import jakarta.mail.*;
import javafx.scene.control.TreeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmailReader {

    private String username = "";
    private String password = "";

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
            Folder rf = store.getFolder("[Gmail]");
            if ((rf.getType() & Folder.HOLDS_FOLDERS) != 0)
            {
                Folder[] f = rf.list();
                for (int i = 0; i < f.length; i++)
                {
//                    System.out.println(f[i].getName());
                }
            }
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

    public Folder getFolder(String folderName){

        try {
//            System.out.println(store.getDefaultFolder().getFullName());
            return store.getFolder(folderName);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return null;
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

    public List<TreeItem<Folder>> getTreeItems() {
        List<TreeItem<Folder>> folders= new ArrayList<>();
        try {
            Folder mainFolder = store.getDefaultFolder();
            if ((mainFolder.getType() & Folder.HOLDS_FOLDERS) != 0) {
                Folder[] mainFolderLiist = mainFolder.list();
                for (int i = 0; i < mainFolderLiist.length; i++) {
                    TreeItem<Folder> folderTreeItem = new TreeItem<>(mainFolderLiist[i]);
                    if ((mainFolderLiist[i].getType() & Folder.HOLDS_FOLDERS) != 0) {
                        Folder[] inFolder = mainFolderLiist[i].list();
                        for (int j = 0; j < inFolder.length; j++) {
                            folderTreeItem.getChildren().add(new TreeItem<>(inFolder[j]));
                        }
                    }
                    folders.add(folderTreeItem);
                }
            }
        } catch (MessagingException messagingException) {
            messagingException.printStackTrace();
        }
        return folders;
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

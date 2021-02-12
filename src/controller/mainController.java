package controller;

import jakarta.mail.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import utils.EmailReader;
import utils.UsernameAndPasswd;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class mainController implements Initializable {


    @FXML
    private TreeView<Folder> treeView;
    @FXML
    private TableView<Message> tableView;
    @FXML
    private TableColumn<Message, String> subjectColumn;
    @FXML
    private TableColumn<Message, String> senderColumn;
    @FXML
    private TableColumn<Message, Integer> sizeColumn;
    @FXML
    private WebView messageWebView;

    @FXML
    private Button sendTextEmailButton;


    private Document messageLayout;

    private String username = "";
    private String passwd = "";
    EmailReader emailReader;

    private ObservableList<Message> messagesList = FXCollections.observableList(new ArrayList<>());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username= UsernameAndPasswd.getUsername();
        passwd= UsernameAndPasswd.getPassword();
        emailReader = new EmailReader(username, passwd);

        TreeItem<Folder> root = new TreeItem(username);
        List<TreeItem<Folder>> folders =emailReader.getTreeItems();
//        emailReader.readEmails();
        for (TreeItem<Folder> folder : folders) {
            root.getChildren().add(folder);
        }

        treeView.setRoot(root);

        subjectColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("subject"));
        senderColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("sender"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<Message, Integer>("size"));

        tableView.setItems(messagesList);
    }

    @FXML
    void treeViewMouseClicked() {
        if (treeView.getSelectionModel().getSelectedItem()== null || treeView.getSelectionModel().getSelectedItem().getParent()==null) return;
        String folderName =  treeView.getSelectionModel().getSelectedItem().getValue().getFullName();
        System.out.println("FN: "+folderName);
        messagesList.clear();
        if (!folderName.equals(username) && !folderName.equals("[Gmail]"))
        try {
                Folder folder =emailReader.getFolder(folderName);
                if (folder.getMessageCount()==0)return;
                System.out.println("Full Name:  -> " +folder.getFullName());
                folder.open(Folder.READ_ONLY);
                for (int i = folder.getMessageCount(); i >0 ; i--) {
                    Message message = folder.getMessage(i);
                    messagesList.add(message);

            }


        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void tableViewMouseClicked() {
        if (tableView.getSelectionModel().getSelectedItem()==null)return;
        Message message = tableView.getSelectionModel().getSelectedItem();

        showMessage(toHtml(message));


    }

    /**
     * Konwertowanie wiadomości do HTML
     * @param message
     * @return
     */
    private Element toHtml(Message message) {
        String messageSubject="";
        Address[] messageFrom = new Address[1];
        Address[] messageTo = new Address[100];
        Date dateTime = null;
        String messageContent="";

        try {
            messageSubject = message.getSubject();
            messageFrom = message.getFrom().clone();
            messageTo = message.getAllRecipients();
            dateTime= message.getReceivedDate();
            Object o = message.getContent();

            /*Jeśli cotent jest stringiem*/
            if (o instanceof String) messageContent = (String) o;
            /*Jeśli content jest podzielony na części*/
            if (o instanceof Multipart) {
                Multipart mp = (Multipart)o;
                int count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    if(mp.getBodyPart(i).getContent().getClass().equals(String.class)) {
                        messageContent = (String) mp.getBodyPart(i).getContent();
                    }
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        Element wrapper = new Element("li");
        Element messageDetails = new Element("div").appendTo(wrapper);
        Element message_div = new Element("div").appendTo(wrapper);
        String messageDetailsHTML = "<div><b>Subject: </b>"+messageSubject+"<br> " +
                "<b>From: </b>" + Arrays.stream(messageFrom).findFirst().get() + "<br>" +
                "<b>To: </b>" + Arrays.stream(messageTo).findAny().get() + "<br>"+
                "<b>Date: </b>" + dateTime + "<br>"+
                "</div> <br><br>";
        new Element("div").append(messageDetailsHTML).appendTo(messageDetails);
//        new Element("b").append("")
        new Element("div").append(messageContent).appendTo(message_div);

        return wrapper;
    }

    private void showMessage(Element message) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messageWebView.getEngine().loadContent("<html><head><meta charset='UTF-8'>\" +\n" +
                        "                        \"</head><body></body></html>\",\n" +
                        "                \"UTF-16\"");
                messageWebView.getEngine().loadContent(message.html());
            }
        });
    }

    @FXML
    void sendTextEmailAction() {
        Scene scene=null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/sendTextEmailView.fxml"));
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

}

package controller;

import jakarta.mail.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
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
    private TreeView<String> treeView;
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

    private Document messageLayout;

    private String username = "";
    private String passwd = "";

    private ObservableList<Message> messagesList = FXCollections.observableList(new ArrayList<>());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username= UsernameAndPasswd.getUsername();
        passwd= UsernameAndPasswd.getPassword();


        TreeItem<String> root = new TreeItem<>(username);
        TreeItem<String> nodeA = new TreeItem<>("Inbox");
        TreeItem<String> nodeB = new TreeItem<>("[Gmail]/Kosz");
        TreeItem<String> nodeC = new TreeItem<>("[Gmail]/Oznaczone gwiazdką");
        TreeItem<String> nodeD = new TreeItem<>("[Gmail]/Spam");
        TreeItem<String> nodeE = new TreeItem<>("[Gmail]/Ważne");
        TreeItem<String> nodeF = new TreeItem<>("[Gmail]/Wersje robocze");
        TreeItem<String> nodeG = new TreeItem<>("[Gmail]/Wszystkie");
        TreeItem<String> nodeH = new TreeItem<>("[Gmail]/Wysłane");
        root.getChildren().addAll(nodeA, nodeB, nodeC, nodeD, nodeE, nodeF,nodeG,nodeH);
        treeView.setRoot(root);

        subjectColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("subject"));
        senderColumn.setCellValueFactory(new PropertyValueFactory<Message, String>("sender"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<Message, Integer>("size"));


        messageLayout = Jsoup.parse(
                "<html><head><meta charset='UTF-8'>" +
                        "</head><body></body></html>",
                "UTF-16",
                Parser.xmlParser()
        );
        messageWebView.getEngine().loadContent(messageLayout.html());
        tableView.setItems(messagesList);
    }

    @FXML
    void treeViewMouseClicked() {
        if (treeView.getSelectionModel().getSelectedItem()== null) return;
        String folderName =  treeView.getSelectionModel().getSelectedItem().getValue();
        System.out.println(folderName);
        EmailReader emailReader = new EmailReader(username, passwd);

        if (!folderName.equals(username))
        try {
            messagesList.clear();
            Folder folder =emailReader.getFolder(folderName);
            folder.open(Folder.READ_ONLY);
            for (int i = folder.getMessageCount(); i >0 ; i--) {
                Message message = folder.getMessage(i);
                messagesList.add(message);
            }

            for (Message message : messagesList) {
                System.out.println(message.getSubject());
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

}

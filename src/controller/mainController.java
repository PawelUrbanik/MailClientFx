package controller;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
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
import utils.EmailReader;
import utils.UsernameAndPasswd;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

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

}

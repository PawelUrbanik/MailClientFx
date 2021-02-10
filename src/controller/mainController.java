package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import utils.EmailReader;
import utils.UsernameAndPasswd;

import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable {

    private String username = "";
    private String passwd = "";

    @FXML
    private TreeView<String> tableView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username= UsernameAndPasswd.getUsername();
        passwd= UsernameAndPasswd.getPassword();
        EmailReader emailReader = new EmailReader(username, passwd);
        emailReader.readEmails();
        TreeItem<String> root = new TreeItem<>("testerterster535@gmail.com");
        TreeItem<String> nodeA = new TreeItem<>("NodeA");
        TreeItem<String> nodeB = new TreeItem<>("NodeB");
        TreeItem<String> nodeC = new TreeItem<>("NodeC");

        root.getChildren().addAll(nodeA, nodeB, nodeC);

        tableView.setRoot(root);
    }
}

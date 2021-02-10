package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable {


    @FXML
    private TreeView<String> tableView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> root = new TreeItem<>("testerterster535@gmail.com");
        TreeItem<String> nodeA = new TreeItem<>("NodeA");
        TreeItem<String> nodeB = new TreeItem<>("NodeB");
        TreeItem<String> nodeC = new TreeItem<>("NodeC");

        root.getChildren().addAll(nodeA, nodeB, nodeC);

        tableView.setRoot(root);
    }
}

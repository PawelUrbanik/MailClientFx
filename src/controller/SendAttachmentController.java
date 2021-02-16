package controller;

import jakarta.mail.MessagingException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import utils.EmailSender;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SendAttachmentController implements Initializable {

    @FXML
    private TextField toField;
    @FXML
    private TextField subjectField;
    @FXML
    private TextArea contentField;
    @FXML
    private TableView<File> fileTableView;
    @FXML
    private TableColumn<File, String> filenameColumn;

    @FXML
    private TableColumn<File, Long> sizeColumn;

    private ObservableList<File> files = FXCollections.observableList(new ArrayList<>());

    private FileChooser fileChooser= null;
    private EmailSender sender;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filenameColumn.setCellValueFactory(new PropertyValueFactory<File, String>("name"));
//        sizeColumn.setCellValueFactory(new PropertyValueFactory<File, Long>("length()"));

        sender = new EmailSender();
        fileTableView.setItems(files);
        fileChooser = new FileChooser();
    }

    @FXML
    void addFileAction() {
        List<File> fileList = fileChooser.showOpenMultipleDialog(null);
        if (fileList!= null)
        {
            fileList.forEach((file) -> files.add(file));
        }

    }


    @FXML
    void sendAction( ) {
        Alert alert;
        try {
            sender.sendAttachmentMessage(toField.getText(), subjectField.getText(), contentField.getText(), files);
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sending e-mail");
            alert.setHeaderText("Message sent");
            alert.setContentText("Message sent succesfully");
            alert.showAndWait();
        } catch (MessagingException e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Sending e-mail");
            alert.setHeaderText("Message not sent");
            alert.setContentText("Cause: " + e.getMessage());
            alert.showAndWait();
                e.printStackTrace();
        }
    }

}

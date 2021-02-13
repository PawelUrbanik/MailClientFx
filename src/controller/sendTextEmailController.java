package controller;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import utils.EmailSender;

import java.net.URL;
import java.util.ResourceBundle;

public class sendTextEmailController implements Initializable {

    @FXML
    private TextField toField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextArea contentField;

    private EmailSender emailSender;

    private Alert alert;

    @FXML
    void sendAction() {
        if ( toField.getLength()>0 &&
            subjectField.getLength()>0 &&
            contentField.getLength()>0) {
            try {
                emailSender.sendTextMessage(toField.getText(), subjectField.getText(), contentField.getText());
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
//                e.printStackTrace();
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailSender = new EmailSender();
    }


}

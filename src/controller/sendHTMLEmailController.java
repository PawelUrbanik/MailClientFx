package controller;

import jakarta.mail.MessagingException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import utils.EmailSender;

import java.net.URL;
import java.util.ResourceBundle;

public class sendHTMLEmailController implements Initializable {

    @FXML
    private TextField toField;

    @FXML
    private TextField subjectField;

    @FXML
    private HTMLEditor htmlField;

    private EmailSender emailSender;

    private Alert alert;

    @FXML
    void sendAction() {
        if ( toField.getLength()>0 &&
                subjectField.getLength()>0 &&
                htmlField.getHtmlText().length()>0) {
            try {
                emailSender.sendHTMLMessage(toField.getText(), subjectField.getText(), htmlField.getHtmlText());
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
//            System.out.println(htmlField.getHtmlText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailSender = new EmailSender();
    }
}

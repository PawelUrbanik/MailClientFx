package controller;

import jakarta.mail.Message;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @FXML
    void sendAction() {
        if ( toField.getLength()>0 &&
            subjectField.getLength()>0 &&
            contentField.getLength()>0)
        emailSender.sendTextMessage(toField.getText(), subjectField.getText(), contentField.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailSender = new EmailSender();
    }


}

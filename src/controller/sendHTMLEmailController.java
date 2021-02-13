package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @FXML
    void sendAction() {
        if ( toField.getLength()>0 &&
                subjectField.getLength()>0 &&
                htmlField.getHtmlText().length()>0)
            emailSender.sendHTMLMessage(toField.getText(), subjectField.getText(), htmlField.getHtmlText());
            System.out.println(htmlField.getHtmlText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailSender = new EmailSender();
    }
}

package artsishevskiy.cryptmethods.lab3;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Button_Close;

    @FXML
    void OnAction_Button_Close(ActionEvent event) {
        Stage st = (Stage) Button_Close.getScene().getWindow();
        st.close();
    }

    @FXML
    void initialize() {
        assert Button_Close != null : "fx:id=\"Button_Close\" was not injected: check your FXML file 'about.fxml'.";

    }
}
package artsishevskiy.cryptmethods.lab3;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FileController {

    Stage stageAbout;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem menuButtonChoose;

    @FXML
    private MenuItem menuButtonSave;

    @FXML
    private TextField text_author_name;

    @FXML
    private MenuItem menuButtonExit;

    @FXML
    private MenuBar menu;

    @FXML
    private MenuItem menuButtonDelete;

    @FXML
    private Label labelErrorSave;

    @FXML
    private TextArea text_edit_field;

    @FXML
    private Button Button_Save;

    @FXML
    private MenuItem menuButtonAbout;

    @FXML
    private Button Button_ChooseUser;

    @FXML
    private Label labelErrorUser;

    @FXML
    private Label labelErrorLoad;

    @FXML
    private Button Button_DownloadDoc;

    @FXML
    private TextField text_user_name;

    @FXML
    private MenuItem menuButtonLoad;

    @FXML
    void OnAction_menuButtonSave(ActionEvent event) {

    }

    @FXML
    void OnAction_menuButtonLoad(ActionEvent event) {

    }

    @FXML
    void OnAction_menuButtonExit(ActionEvent event) {

    }

    @FXML
    void OnAction_menuButtonChoose(ActionEvent event) {

    }

    @FXML
    void OnAction_menuButtonDelete(ActionEvent event) {

    }

    @FXML
    void OnAction_menuButtonAbout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("about.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 332);
        stageAbout = new Stage();
        stageAbout.setTitle("О программе");
        stageAbout.setScene(scene);
        stageAbout.show();
    }

    @FXML
    void OnAction_Button_ChooseUser(ActionEvent event) {

    }

    @FXML
    void OnAction_Button_Save(ActionEvent event) {

    }

    @FXML
    void OnAction_Button_DownloadDoc(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert menuButtonChoose != null : "fx:id=\"menuButtonChoose\" was not injected: check your FXML file 'file.fxml'.";
        assert menuButtonSave != null : "fx:id=\"menuButtonSave\" was not injected: check your FXML file 'file.fxml'.";
        assert text_author_name != null : "fx:id=\"text_author_name\" was not injected: check your FXML file 'file.fxml'.";
        assert menuButtonExit != null : "fx:id=\"menuButtonExit\" was not injected: check your FXML file 'file.fxml'.";
        assert menu != null : "fx:id=\"menu\" was not injected: check your FXML file 'file.fxml'.";
        assert menuButtonDelete != null : "fx:id=\"menuButtonDelete\" was not injected: check your FXML file 'file.fxml'.";
        assert labelErrorSave != null : "fx:id=\"labelErrorSave\" was not injected: check your FXML file 'file.fxml'.";
        assert text_edit_field != null : "fx:id=\"text_edit_field\" was not injected: check your FXML file 'file.fxml'.";
        assert Button_Save != null : "fx:id=\"Button_Save\" was not injected: check your FXML file 'file.fxml'.";
        assert menuButtonAbout != null : "fx:id=\"menuButtonAbout\" was not injected: check your FXML file 'file.fxml'.";
        assert Button_ChooseUser != null : "fx:id=\"Button_ChooseUser\" was not injected: check your FXML file 'file.fxml'.";
        assert labelErrorUser != null : "fx:id=\"labelErrorUser\" was not injected: check your FXML file 'file.fxml'.";
        assert labelErrorLoad != null : "fx:id=\"labelErrorLoad\" was not injected: check your FXML file 'file.fxml'.";
        assert Button_DownloadDoc != null : "fx:id=\"Button_DownloadDoc\" was not injected: check your FXML file 'file.fxml'.";
        assert text_user_name != null : "fx:id=\"text_user_name\" was not injected: check your FXML file 'file.fxml'.";
        assert menuButtonLoad != null : "fx:id=\"menuButtonLoad\" was not injected: check your FXML file 'file.fxml'.";

        text_edit_field.setDisable(true);
        text_author_name.setDisable(true);
        text_user_name.setDisable(true);

        Button_Save.setDisable(true);
        Button_DownloadDoc.setDisable(true);
    }
}
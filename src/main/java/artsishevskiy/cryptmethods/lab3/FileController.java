package artsishevskiy.cryptmethods.lab3;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileController {

    private Stage stageAbout;

    private CertificatesHandler handler;
    private boolean isChosen = false;
    private String currentUser;

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
    private TextArea text_edit_field;

    @FXML
    private Button Button_Save;

    @FXML
    private MenuItem menuButtonAbout;

    @FXML
    private Button Button_ChooseUser;

    @FXML
    private Label label_error_save;

    @FXML
    private Label label_error_user;

    @FXML
    private Button Button_DownloadDoc;

    @FXML
    private Button Button_Select;

    @FXML
    private TextField text_user_name;

    @FXML
    private MenuItem menuButtonLoad;

    @FXML
    private ListView<String> listView;

    @FXML
    void OnAction_menuButtonExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void OnAction_menuButtonDelete(ActionEvent event) {
        handler.deleteCertificate(text_user_name.getText());

        text_author_name.setText("");
        text_user_name.setText("");
        text_edit_field.setText("");

        text_edit_field.setDisable(true);
        Button_Save.setDisable(true);
        Button_DownloadDoc.setDisable(true);
//        buttonLoad.setDisable(true);

        label_error_save.setText("Сертификат успешно удален");
        label_error_save.setTextFill(Color.GREEN);
        label_error_save.setAlignment(Pos.TOP_CENTER);
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
        label_error_save.setText("");
        label_error_user.setText("");
        text_edit_field.setText("");

        if (!isChosen) {
            handler.init();
            ArrayList<String> users = new ArrayList<>();
            listView.setVisible(true);
            Button_Select.setVisible(true);

            while (handler.data.hasMoreElements()) {
                String name = handler.data.nextElement();
                users.add(handler.getDecodedString(name));

//                try{
//                    users.add(handler.convert(name));
//                } catch (UnsupportedEncodingException e) {
//                    System.out.println("\nПроблема с изменением кодировки при выводе списка сертификатов");
//                    e.printStackTrace();
//                }


//                System.out.println(certificates.hasIt(certificates.getDecodedString(name)));
            }
            listView.setItems(FXCollections.observableArrayList(users));
        }
        else {
            currentUser = listView.getSelectionModel().getSelectedItems().toString();
            currentUser = currentUser.substring(1, currentUser.length()-1);
            text_user_name.setText(currentUser);

            if (!currentUser.equals("")) {
                text_edit_field.setDisable(false);
                Button_Save.setDisable(false);
                Button_DownloadDoc.setDisable(false);
            } else {
                text_edit_field.setDisable(true);
                Button_Save.setDisable(true);
                Button_DownloadDoc.setDisable(true);
            }

            listView.setVisible(false);
            Button_Select.setVisible(false);
        }
        isChosen = !isChosen;
    }

    @FXML
    void OnAction_Button_Save(ActionEvent event) {
        label_error_save.setText("");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите путь сохранения и имя файла");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Подписанный документ(.sd)", "*.sd"));
        File defaultDirectory = new File("./");
        fileChooser.setInitialDirectory(defaultDirectory);
        File selectedFile = fileChooser.showSaveDialog(new Stage());

        if (selectedFile == null) {
            return;
        }

        if (!handler.signDoc(currentUser, text_edit_field.getText(), selectedFile.getPath())) {
            label_error_save.setTextFill(Color.RED);
            label_error_save.setText("Документ не подписан, возможно, поврежден сертификат");
            label_error_save.setAlignment(Pos.CENTER);
        } else {
            label_error_save.setTextFill(Color.GREEN);
            label_error_save.setText("Документ успешно подписан");
            label_error_save.setAlignment(Pos.CENTER);
            text_edit_field.setText("");
        }
    }

    @FXML
    void OnAction_Button_DownloadDoc(ActionEvent event) {
        label_error_save.setText("");
        text_edit_field.setText("");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите подписанный файл");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Подписанный документ(.sd)", "*.sd"));
        File defaultDirectory = new File("./");
        fileChooser.setInitialDirectory(defaultDirectory);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile == null) {
            return;
        }

        String author = handler.checkSignedDoc(selectedFile.getPath());
        if (author.equals("")) {
            label_error_save.setText("Сертификат поврежден или не существует");
            label_error_save.setAlignment(Pos.CENTER);
            label_error_save.setTextFill(Color.RED);
            return;
        }

        text_author_name.setText(author);
        text_edit_field.setText(handler.getTextFromSignedDoc(selectedFile.getPath()));
    }

    @FXML
    void initialize() {
        assert menuButtonChoose != null : "fx:id=\"menuButtonChoose\" was not injected: check your FXML file 'file.fxml'.";
        assert label_error_save != null : "fx:id=\"label_error_save\" was not injected: check your FXML file 'file.fxml'.";
        assert menuButtonSave != null : "fx:id=\"menuButtonSave\" was not injected: check your FXML file 'file.fxml'.";
        assert text_author_name != null : "fx:id=\"text_author_name\" was not injected: check your FXML file 'file.fxml'.";
        assert menuButtonExit != null : "fx:id=\"menuButtonExit\" was not injected: check your FXML file 'file.fxml'.";
        assert Button_Select != null : "fx:id=\"Button_Select\" was not injected: check your FXML file 'file.fxml'.";
        assert menu != null : "fx:id=\"menu\" was not injected: check your FXML file 'file.fxml'.";
        assert menuButtonDelete != null : "fx:id=\"menuButtonDelete\" was not injected: check your FXML file 'file.fxml'.";
        assert label_error_user != null : "fx:id=\"label_error_user\" was not injected: check your FXML file 'file.fxml'.";
        assert text_edit_field != null : "fx:id=\"text_edit_field\" was not injected: check your FXML file 'file.fxml'.";
        assert Button_Save != null : "fx:id=\"Button_Save\" was not injected: check your FXML file 'file.fxml'.";
        assert listView != null : "fx:id=\"listView\" was not injected: check your FXML file 'file.fxml'.";
        assert menuButtonAbout != null : "fx:id=\"menuButtonAbout\" was not injected: check your FXML file 'file.fxml'.";
        assert Button_ChooseUser != null : "fx:id=\"Button_ChooseUser\" was not injected: check your FXML file 'file.fxml'.";
        assert Button_DownloadDoc != null : "fx:id=\"Button_DownloadDoc\" was not injected: check your FXML file 'file.fxml'.";
        assert text_user_name != null : "fx:id=\"text_user_name\" was not injected: check your FXML file 'file.fxml'.";
        assert menuButtonLoad != null : "fx:id=\"menuButtonLoad\" was not injected: check your FXML file 'file.fxml'.";

        text_edit_field.setDisable(true);
        text_author_name.setDisable(true);
        text_user_name.setDisable(true);

        Button_Save.setDisable(true);
        Button_DownloadDoc.setDisable(true);

        listView.setVisible(false);
        Button_Select.setVisible(false);

        handler = new CertificatesHandler();
    }
}
package org.example.hotelmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class FormBuilder {
    private FXMLLoader fxmlLoader;
    private Scene scene;
    public void openWindow(Stage stage, String source, String title, int v, int v1) throws IOException {
        fxmlLoader = new FXMLLoader(ManagerApplication.class.getResource(source));
        scene = new Scene(fxmlLoader.load(), v, v1);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    public void openDialog() throws IOException{
        fxmlLoader = new FXMLLoader(ManagerApplication.class.getResource("after-register-form.fxml"));
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Поки так"); // ОБОВ'ЯЗКОВО ЗМІНИТИ ЗАГОЛОВОК
        Scene dialogScene = new Scene(fxmlLoader.load(), 400, 400);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }
}

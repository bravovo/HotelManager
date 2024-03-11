package org.example.hotelmanager.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.RoomType;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomTypesFormController implements Initializable {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    @FXML private TableColumn<RoomType, Integer> type_capacity_col;
    @FXML private TableColumn<RoomType, String> type_description_col;
    @FXML private TableColumn<RoomType, Integer> type_id_col;
    @FXML private TableColumn<RoomType, String> type_name_col;
    @FXML private TableView<RoomType> types_table;

    public void initialize(URL url, ResourceBundle resourceBundle){
        setTable();
    }

    public void setTable(){ //TODO Змінити опис типів кімнат в базі даних
        type_id_col.setCellValueFactory(new PropertyValueFactory<>("typeID"));
        type_name_col.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        type_description_col.setCellValueFactory(new PropertyValueFactory<>("typeDescription"));
        type_capacity_col.setCellValueFactory(new PropertyValueFactory<>("typeCapacity"));
        types_table.setItems(mongoDatabaseConnection.getRoomTypes());
    }
}

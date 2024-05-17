package org.example.hotelmanager.controllers.superAdmin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class SuperClientFormController implements Initializable {
    SuperAdmin superAdmin = new SuperAdmin();
    SuperAdminHolder superAdminHolder = SuperAdminHolder.getInstance();
    ClientHolder clientHolder = ClientHolder.getInstance();
    FormBuilder formBuilder = new FormBuilder();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    public ToggleButton find_client_btn;
    public Button delete_client_btn;
    public Label clients_cnt;
    public AnchorPane hotels_anchor_pane;
    public VBox find_client_vbox;
    public ChoiceBox<String> filter_choice;
    public TextField find_input;
    public Button find_button;
    public Button reset_button;
    public TableView<Client> client_table;
    public TableColumn client_id;
    public TableColumn client_first_name;
    public TableColumn client_second_name;
    public TableColumn client_email;
    public TableColumn client_phone_number;
    public TableColumn client_date_of_birth;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        superAdmin = superAdminHolder.getSuper();
        setClientsTable();
        ObservableList<String> items = FXCollections.observableArrayList(
                "Ім'я", "Прізвище", "Електронна пошта", "Номер телефону"
        );
        filter_choice.setItems(items);
        filter_choice.setValue("Фільтр для пошуку");
        client_table.setOnMouseClicked(event -> {
            Client selectedClient = client_table.getSelectionModel().getSelectedItem();
            clientHolder.setUser(selectedClient);
            if (event.getClickCount() == 1) { // Одинарне клацання
                if (selectedClient != null) {
                    delete_client_btn.setDisable(false);
                    clientHolder.setUser(selectedClient);
                }
                else {
                    delete_client_btn.setDisable(true);
                }
            }
        });
    }
    private void setClientsTable() {
        superAdmin = superAdminHolder.getSuper();
        find_input.setText("");
        client_id.setCellValueFactory(new PropertyValueFactory<>("clientID"));
        client_first_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        client_second_name.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        client_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        client_phone_number.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        client_date_of_birth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        ObservableList<Client> clients = superAdmin.getClients();
        client_table.setItems(clients);
        clients_cnt.setText(String.valueOf(clients.size()));
    }
    public void setClientTable(ObservableList<Client> clients){
        client_id.setCellValueFactory(new PropertyValueFactory<>("clientID"));
        client_first_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        client_second_name.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        client_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        client_phone_number.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        client_date_of_birth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        client_table.setItems(clients);
    }
    public void findClientButtonClick(ActionEvent actionEvent) {
        if(find_client_btn.isSelected()){
            AnchorPane.setTopAnchor(client_table, 80.0);
            find_button.setVisible(true);
            client_table.setPrefHeight(541);
        }else {
            setClientsTable();
            AnchorPane.setTopAnchor(client_table, 0.0);
            find_button.setVisible(false);
            client_table.setPrefHeight(621);
        }
    }
    public void deleteClientButtonClick(ActionEvent actionEvent) throws IOException {
        formBuilder.openDialog("super-admin-forms/super-confirm-client-delete-form.fxml", "Підтвердити видалення", 300, 200);
        superAdmin.setClients(mongoDatabaseConnection.getClient());
        setClientsTable();
        delete_client_btn.setDisable(true);
    }
    public void findButtonClick(ActionEvent actionEvent) {
        ObservableList<Client> clientList = FXCollections.observableArrayList();
        String filter;
        String value;
        if (filter_choice.getValue().equals("Фільтр для пошуку")){
            formBuilder.errorValidation("Фільтр для пошуку повинен бути вибраний!");
            return;
        }
        switch (filter_choice.getValue()){
            case "Ім'я" -> {
                String clientFirstName = find_input.getText();
                filter = "Ім'я";
                value = clientFirstName;
                clientList = findClientsByFilterAndValue(filter, value);
            }
            case "Прізвище" -> {
                String clientSecondName = find_input.getText();
                filter = "Прізвище";
                value = clientSecondName;
                clientList = findClientsByFilterAndValue(filter, value);
            }
            case "Електронна пошта" -> {
                String clientEmail = find_input.getText();
                filter = "Електронна пошта";
                value = clientEmail;
                clientList = findClientsByFilterAndValue(filter, value);
            }
            case "Номер телефону" -> {
                String phoneNumber = find_input.getText().replaceAll("[^0-9]", "");
                filter = "Номер телефону";
                value = phoneNumber;
                clientList = findClientsByFilterAndValue(filter, value);
            }
        }
        setClientTable(clientList);
    }
    private ObservableList<Client> findClientsByFilterAndValue(String filter, String value) {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        for(Client clientFromList : superAdmin.getClients()){
            switch (filter){
                case "Ім'я" -> {
                    if(clientFromList.getFirstName().equals(value)){
                        clients.add(clientFromList);
                    }
                }
                case "Прізвище" -> {
                    if(clientFromList.getLastName().equals(value)){
                        clients.add(clientFromList);
                    }
                }
                case "Електронна пошта" -> {
                    if(clientFromList.getEmail().equals(value)){
                        clients.add(clientFromList);
                    }
                }
                case "Номер телефону" -> {
                    if(clientFromList.getPhoneNumber().equals(value)){
                        clients.add(clientFromList);
                    }
                }
            }
        }
        return clients;
    }
    public void resetTableButtonClick(ActionEvent actionEvent) {
        setClientsTable();
    }
}

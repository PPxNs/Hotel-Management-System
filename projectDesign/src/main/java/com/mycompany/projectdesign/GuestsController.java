/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectdesign;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;



import com.mycompany.projectdesign.Project.Model.*;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.value.ChangeListener;


public class GuestsController implements Initializable{

    //ดึง javaFX มา
    @FXML private TableView<GuestsTableView> guestTable;

    //ดึงแต่ละ coluumn
    @FXML private TableColumn<GuestsTableView,String> RoomNoColumn;
    @FXML private TableColumn<GuestsTableView,String> IdCardColumn;
    @FXML private TableColumn<GuestsTableView,String> GuestColumn;
    @FXML private TableColumn<GuestsTableView,String> CheckInColumn;
    @FXML private TableColumn<GuestsTableView,String> CheckOutColumn;
    @FXML private TableColumn<GuestsTableView,String> StatusColumn;   

    //ดึงไว้แก้ข้อมูลฝั่งซ้าย
    @FXML private Label LabelFullname;
    @FXML private Label LabelFirstName;
    @FXML private Label LabelLastName;
    @FXML private Label LabelEmail;
    @FXML private Label LabelPhone;
    @FXML private Label LabelGender;
    @FXML private Label LabelCountry;
    @FXML private Label LabelCity;
    @FXML private Label LabelAddress;
    

    //new เพื่อจะเรียกโหลด csv มาใส่ใน column
    private CustomerRepository customerRepository = new CustomerRepository();
    private RoomRepository roomRepository = new RoomRepository();


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        RoomNoColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("numberRoom"));
        IdCardColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("idCard"));
        GuestColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("fullnameCustomer")); //ทดสอบแก้ไข
        CheckInColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("Checkin"));
        CheckOutColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("Checkout"));
        StatusColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("statusCustomer"));

        //โหลดข้อมูลจาก csv
        customerRepository.loadCustomerFromCSV();
        roomRepository.loadRoomFromCSV();

        //TableView ของ JavaFX ใช้ ObservableList
        ObservableList<GuestsTableView> allCustomers = FXCollections.observableArrayList();

        //เราดึงข้อมูลจาก Hash มาเป็นลิส
        for (Map.Entry<String, List<Customer>> entry : customerRepository.getMapCustomer().entrySet()) {
        Room room = roomRepository.getRoom(entry.getKey());
        for (Customer customer : entry.getValue()) {
            allCustomers.add(new GuestsTableView(room, customer));
        }
    }

        //เราข้อมูลทั้งหมดใส่ใน tableview
        guestTable.setItems(allCustomers);
        guestTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GuestsTableView>() {
            @Override
            public void changed(ObservableValue<? extends GuestsTableView> observable, GuestsTableView oldValue, GuestsTableView newValue) {
            // newValue คือแถวใหม่ที่ถูกเลือก (อาจจะเป็น null ถ้าไม่มีแถวไหนถูกเลือก)
            if (newValue != null) {
            // ดึงอ็อบเจกต์ Customer จากแถวที่เลือก แล้วส่งไปแสดงผล
            showCustomerDetails(newValue.getCustomer(), newValue.getRoom());
        }}});
       

    }

    //ข้อมูลสำหรับฝั่งซ้าย
    private void showCustomerDetails(Customer customer, Room room) {
    LabelFullname.setText("Room | " + room.getNumberRoom());
    LabelFirstName.setText(customer.getFirstnameCustomer());
    LabelLastName.setText(customer.getLastnameCustomer());
    LabelEmail.setText(customer.getEmail());
    LabelPhone.setText(customer.getPhone());
    LabelGender.setText(customer.getGender());
    LabelCountry.setText(customer.getCountry());
    LabelCity.setText(customer.getCity());
    LabelAddress.setText(customer.getAddress());
    }


}




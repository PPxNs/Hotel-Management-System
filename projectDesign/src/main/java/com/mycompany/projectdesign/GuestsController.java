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
    private CustomerRepository customerRepository = CustomerRepository.getInstance();
    private RoomRepository roomRepository = RoomRepository.getInstance();
    private BookingRepository bookingRepository = BookingRepository.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        RoomNoColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("numberRoom"));
        IdCardColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("idCard"));
        GuestColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("fullnameCustomer"));
        CheckInColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("checkin"));
        CheckOutColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("checkout"));
        StatusColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("status"));

        //โหลดข้อมูลจาก csv
        /*customerRepository.loadCustomerFromCSV();
        roomRepository.loadRoomFromCSV();
        bookingRepository.loadBookingFromCSV();*/

        //TableView ของ JavaFX ใช้ ObservableList
        ObservableList<GuestsTableView> allCustomers = FXCollections.observableArrayList();

        //เราดึงข้อมูลจาก Hash มาเป็นลิส
        for (Map.Entry<String, List<Customer>> entry : customerRepository.getAllCustomers().entrySet()) {
        String roomNo = entry.getKey();
        Room room = roomRepository.getRoom(roomNo);

            if (room != null && !entry.getValue().isEmpty()) { 
       
                Customer primaryCustomer = entry.getValue().get(0); 
                Bookings booking = bookingRepository.getBookingByRoom(roomNo);

            if (booking != null) {
                allCustomers.add(new GuestsTableView(room, primaryCustomer, booking));
        }
        } else {
            System.out.println("ห้อง " + roomNo + " ไม่มีใน roomRepository หรือไม่มีข้อมูลลูกค้า");
    }



    }
        //เราข้อมูลทั้งหมดใส่ใน tableview
        guestTable.setItems(allCustomers);
        guestTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GuestsTableView>() {
            @Override
            public void changed(ObservableValue<? extends GuestsTableView> observable, GuestsTableView oldValue, GuestsTableView newValue) {
            
            if (newValue != null) {
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




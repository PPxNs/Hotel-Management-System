/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectdesign;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.mycompany.projectdesign.Project.Model.Customer;
import com.mycompany.projectdesign.Project.Model.CustomerRepository;
import com.mycompany.projectdesign.Project.Model.Room;
import com.mycompany.projectdesign.Project.Model.RoomRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class ver2 implements Initializable {

    //ดึง javaFX มา
    @FXML private TableView<ReservationsTableView> ReservationsTable;

    //ดึงแต่ละ coluumn
    @FXML private TableColumn<ReservationsTableView,String> BookingIDColumn;
    @FXML private TableColumn<ReservationsTableView,String> RoomNoColumn;
    @FXML private TableColumn<ReservationsTableView,String> GuestColumn;
    @FXML private TableColumn<ReservationsTableView,String> CheckInColumn;
    @FXML private TableColumn<ReservationsTableView,String> DateBookingColumn;   


    //new เพื่อจะเรียกโหลด csv มาใส่ใน column
    private CustomerRepository customerRepository = new CustomerRepository();
    private RoomRepository roomRepository = new RoomRepository();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        BookingIDColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>  ("bookingID"));
        RoomNoColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>  ("numberRoom"));
        GuestColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String> ("fullnameCustomer"));
        CheckInColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>("Checkin"));
        DateBookingColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>("booking"));
       
        customerRepository.loadCustomerFromCSV();
        roomRepository.loadRoomFromCSV();

        //TableView ของ JavaFX ใช้ ObservableList
        ObservableList<ReservationsTableView> allCustomers = FXCollections.observableArrayList();

        //เราดึงข้อมูลจาก Hash มาเป็นลิส
        for (Map.Entry<String, List<Customer>> entry : customerRepository.getMapCustomer().entrySet()) {
        Room room = roomRepository.getRoom(entry.getKey());
        for (Customer customer : entry.getValue()) {
            allCustomers.add(new ReservationsTableView(room, customer));
        }
    }

        //เราข้อมูลทั้งหมดใส่ใน tableview
        ReservationsTable.setItems(allCustomers);
    }

    

}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectdesign;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.mycompany.projectdesign.Project.Model.BookingRepository;
import com.mycompany.projectdesign.Project.Model.BookingStatus;
import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.Model.Customer;
import com.mycompany.projectdesign.Project.Model.CustomerRepository;
import com.mycompany.projectdesign.Project.Model.Room;
import com.mycompany.projectdesign.Project.Model.RoomRepository;
import com.mycompany.projectdesign.Project.Model.RoomStatus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;


public class ReservationsController implements Initializable {

    //ดึง javaFX มา
    @FXML private TableView<ReservationsTableView> ReservationsTable;

    //ดึงแต่ละ coluumn
    @FXML private TableColumn<ReservationsTableView,String> BookingIDColumn;
    @FXML private TableColumn<ReservationsTableView,String> DateBookingColumn;
    @FXML private TableColumn<ReservationsTableView,String> RoomNoColumn;
    @FXML private TableColumn<ReservationsTableView,String> GuestColumn;
    @FXML private TableColumn<ReservationsTableView,String> CheckInColumn;
    @FXML private TableColumn<ReservationsTableView,String> CheckoutColumn;
    @FXML private TableColumn<ReservationsTableView,String> StatusColumn;

    @FXML private ComboBox<String> statusComboBox;

    @FXML private TextField searchField;




    //new เพื่อจะเรียกโหลด csv มาใส่ใน column
    private CustomerRepository customerRepository = CustomerRepository.getInstance();
    private RoomRepository roomRepository = RoomRepository.getInstance();
    private BookingRepository bookingRepository = BookingRepository.getInstance();
    private ObservableList<ReservationsTableView> bookingList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        BookingIDColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>  ("bookingID"));
        RoomNoColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>  ("numberRoom"));
        GuestColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String> ("fullnameCustomer"));
        DateBookingColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>("booking"));
        CheckInColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>("checkin"));
        CheckoutColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>("checkout"));
        
        ObservableList<String> allStatus = FXCollections.observableArrayList(
       "All Status",
                "CONFIRMED",      
                "CHECKED_IN"    
        );

        statusComboBox.setItems(allStatus);
        statusComboBox.setValue("All Status");

        //set ให้ status แก้ได้
        ObservableList<String> statusObtions = FXCollections.observableArrayList();
        for(BookingStatus status : BookingStatus.values()){
            statusObtions.add(status.name());
        }

        StatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        StatusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(statusObtions));

        StatusColumn.setOnEditCommit(event -> {
            ReservationsTableView reservationsView = event.getRowValue();
            Bookings reservationsUpdate = reservationsView.getBookings();
            BookingStatus newStatus = BookingStatus.valueOf(event.getNewValue());
            reservationsUpdate.setStatus(newStatus);
            BookingRepository.getInstance().saveBookingToCSV();
            //ReservationsTable.refresh();
            loadReservationsData();
        });

        ReservationsTable.setEditable(true); //แก้ตารางได้
        StatusColumn.setEditable(true); //แก้สเตตัสได้

        //set เรื่องการค้นหา
        FilteredList<ReservationsTableView> filteredData = new FilteredList<>(bookingList, p -> true);
        searchField.textProperty().addListener((obs, oldVal,newVal) ->{
            filteredData.setPredicate(createPredicate(newVal, statusComboBox.getValue()));
        });

        statusComboBox.valueProperty().addListener((obs, oldVal,newVal) ->{
            filteredData.setPredicate(createPredicate(searchField.getText(), newVal));
        });


        
        SortedList<ReservationsTableView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ReservationsTable.comparatorProperty());

        //เราข้อมูลทั้งหมดใส่ใน tableview
        loadReservationsData();
        ReservationsTable.setItems(sortedData);
    }

    private Predicate<ReservationsTableView> createPredicate(String searchText, String status){
        //ส่งข้อมูลก็ต่อเมื่อ ....
        return booking -> {
            boolean searchMatch = true;
            if (searchText != null && !searchText.isEmpty()) {
                String lowercase = searchText.toLowerCase();
                if (booking.getFullnameCustomer().toLowerCase().contains(lowercase)) {
                    searchMatch = true;
                }else if (booking.getNumberRoom().toLowerCase().contains(lowercase)) {
                    searchMatch = true;
                }else {
                    searchMatch = false;
                }
            }

            boolean statusMatch = true;
            if (status != null && !status.equalsIgnoreCase("All status")) {
                statusMatch = booking.getStatus().equalsIgnoreCase(status);
            }

            return searchMatch && statusMatch;
        };
    }

    private void loadReservationsData() {

        bookingList.clear();

       
        for (Bookings booking : bookingRepository.getAllBookings()) {
            
            Room room = booking.getRoom();
            Customer customer = booking.getCustomer();

            if (room != null && customer != null) {
                bookingList.add(new ReservationsTableView(room, customer, booking));
            } else {
                System.out.println("Booking " + booking.getBookingID() + " is missing Room or Customer data.");
            }
        }
    }

    public void refreshTable() {
        loadReservationsData();
    }


}

        
    






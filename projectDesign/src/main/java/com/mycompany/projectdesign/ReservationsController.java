/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectdesign;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import com.mycompany.projectdesign.Project.Model.*;
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

/**
 * Controller สำหรับหน้าจอจัดการรายการจอง (Reservations.fxml)
 * ทำหน้าที่แสดง, กรอง, ค้นหา, และแก้ไขสถานะของการจองทั้งหมดในระบบ
 */
public class ReservationsController implements Initializable {

    // @FXML Components: ส่วนเกี่ยวกับคอลลัมแสดงข้อมูล
    @FXML private TableView<ReservationsTableView> ReservationsTable;
    @FXML private TableColumn<ReservationsTableView,String> BookingIDColumn;
    @FXML private TableColumn<ReservationsTableView,String> DateBookingColumn;
    @FXML private TableColumn<ReservationsTableView,String> RoomNoColumn;
    @FXML private TableColumn<ReservationsTableView,String> GuestColumn;
    @FXML private TableColumn<ReservationsTableView,String> CheckInColumn;
    @FXML private TableColumn<ReservationsTableView,String> CheckoutColumn;
    @FXML private TableColumn<ReservationsTableView,String> StatusColumn;

    // @FXML Components: ส่วนกรองข้อมูล 
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField searchField;

    // แหล่งข้อมูลหลักของระบบ
    private CustomerRepository customerRepository = CustomerRepository.getInstance();
    private RoomRepository roomRepository = RoomRepository.getInstance();
    private BookingRepository bookingRepository = BookingRepository.getInstance();
    private ObservableList<ReservationsTableView> bookingList = FXCollections.observableArrayList();

    /**
     * เมธอดหลักที่ถูกเรียกโดยอัตโนมัติเมื่อ FXML โหลดเสร็จ
     * ใช้สำหรับตั้งค่าเริ่มต้นทั้งหมดของหน้าจอ
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
           
        //ตั้งค่า CellValueFactory สำหรับแต่ละคอลัมน์เพื่อผูกกับ Property ของ ReservationsTableView
        BookingIDColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>  ("bookingID"));
        RoomNoColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>  ("numberRoom"));
        GuestColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String> ("fullnameCustomer"));
        DateBookingColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>("booking"));
        CheckInColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>("checkin"));
        CheckoutColumn.setCellValueFactory(new PropertyValueFactory<ReservationsTableView,String>("checkout"));
        
        //กำหนด item ใน combobox
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

        // กำหนด Action ที่จะเกิดขึ้นเมื่อแก้ไขสถานะเสร็จ
        StatusColumn.setOnEditCommit(event -> {
            ReservationsTableView reservationsView = event.getRowValue();
            Bookings reservationsUpdate = reservationsView.getBookings();
            BookingStatus newStatus = BookingStatus.valueOf(event.getNewValue());
            reservationsUpdate.setStatus(newStatus);
            BookingRepository.getInstance().saveBookingToCSV();
            loadReservationsData();
        });

        ReservationsTable.setEditable(true); //แก้ตารางได้
        StatusColumn.setEditable(true); //แก้สเตตัสได้

        //set เรื่องการค้นหา
        // สร้าง FilteredList พร้อมเงื่อนไขเริ่มต้น
        FilteredList<ReservationsTableView> filteredData = new FilteredList<>(bookingList, reservations ->                 
                reservations.getStatus().equalsIgnoreCase("CONFIRMED") ||
                reservations.getStatus().equalsIgnoreCase("CHECKED_IN"));
        
        // เพิ่ม Listener ให้ช่องค้นหาและ ComboBox
        searchField.textProperty().addListener((obs, oldVal,newVal) ->{
            filteredData.setPredicate(createPredicate(newVal, statusComboBox.getValue()));
        });

        statusComboBox.valueProperty().addListener((obs, oldVal,newVal) ->{
            filteredData.setPredicate(createPredicate(searchField.getText(), newVal));
        });


        // สร้าง SortedList จาก FilteredList
        SortedList<ReservationsTableView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ReservationsTable.comparatorProperty());

        // โหลดข้อมูลใหม่ทั้งหมดเพื่อรีเฟรชตาราง
        loadReservationsData();

        // ผูก TableView เข้ากับ SortedList
        ReservationsTable.setItems(sortedData);
    }

    /**
     * สร้าง Predicate (เงื่อนไข) สำหรับใช้ในการกรองข้อมูลใน FilteredList
     * @param searchText ข้อความจากช่องค้นหา
     * @param status สถานะที่เลือกจาก ComboBox
     * @return Predicate ที่รวมเงื่อนไขการค้นหาและสถานะ
     */
    private Predicate<ReservationsTableView> createPredicate(String searchText, String status){
        //ส่งข้อมูลก็ต่อเมื่อ ....
        return booking -> {
            BookingStatus currentStatus = booking.getBookings().getStatus();
            if (currentStatus != BookingStatus.CONFIRMED && currentStatus != BookingStatus.CHECKED_IN) {
                return false; //ไม่ใช่คอนเฟิร์ม เช็คอินให้คัดออก
            }

            // เงื่อนไขการค้นหา: ข้อความต้องมีอยู่ในชื่อลูกค้าหรือหมายเลขห้อง
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

            // เงื่อนไขสถานะ: ต้องตรงกับสถานะที่เลือก
            boolean statusMatch = true;
            if (status != null && !status.equalsIgnoreCase("All status")) {
                statusMatch = booking.getStatus().equalsIgnoreCase(status);
            }

            return searchMatch && statusMatch;
        };
    }

    /**
     * โหลดข้อมูลจาก BookingRepository และแปลงเป็น ReservationsTableView เพื่อใส่ใน bookingList
     */
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

    /**
     * เมธอดสาธารณะสำหรับสั่งให้ตารางโหลดข้อมูลใหม่ (อาจถูกเรียกใช้จาก Controller อื่น)
     */   
    public void refreshTable() {
        loadReservationsData();
    }


}

        
    






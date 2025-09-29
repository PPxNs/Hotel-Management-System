/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectdesign;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mycompany.projectdesign.Project.Model.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


public class GuestsController implements Initializable{

    //ดึง javaFX มา
    @FXML private TableView<GuestsTableView> guestTable;

    //ดึงแต่ละ coluumn
    @FXML private TableColumn<GuestsTableView,String> RoomNoColumn;
    @FXML private TableColumn<GuestsTableView,String> IdCardColumn;
    @FXML private TableColumn<GuestsTableView,String> FirstNameColumn;
    @FXML private TableColumn<GuestsTableView,String> LastNameColumn;
    @FXML private TableColumn<GuestsTableView,String> CheckInColumn;
    @FXML private TableColumn<GuestsTableView,String> CheckOutColumn;
    @FXML private TableColumn<GuestsTableView,String> StatusColumn;
    @FXML private TableColumn<GuestsTableView,String> EmailColumn;
    @FXML private TableColumn<GuestsTableView,String> PhoneColumn;
    @FXML private TableColumn<GuestsTableView,String> GenderColumn;
    @FXML private TableColumn<GuestsTableView,String> AddressColumn;
    @FXML private TableColumn<GuestsTableView,String> CityColumn;
    @FXML private TableColumn<GuestsTableView,String> CountryColumn;


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
    
    @FXML private ComboBox<String> statusComboBox;

    @FXML private TextField searchField;

    //new เพื่อจะเรียกโหลด csv มาใส่ใน column
    private CustomerRepository customerRepository = CustomerRepository.getInstance();
    private RoomRepository roomRepository = RoomRepository.getInstance();
    private BookingRepository bookingRepository = BookingRepository.getInstance();

    //ลิสจัดการข้อมูล
    private ObservableList<GuestsTableView> masterData = FXCollections.observableArrayList();
    private FilteredList<GuestsTableView> filteredData;
    private SortedList<GuestsTableView> sortedData;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setupTableColumns();
        ObservableList<String> allStatus = FXCollections.observableArrayList(
       "All Status",
                "CONFIRMED",      
                "CHECKED_IN"    
        );

        statusComboBox.setItems(allStatus);
        statusComboBox.setValue("All Status");

        loadAndFilterData();

        searchField.textProperty().addListener((obs,oldVal,newVal)-> applyFilters() );
        statusComboBox.valueProperty().addListener((obs,oldVal,newVal)-> applyFilters() );
        guestTable.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal)-> {
            if (newVal != null) {
                showCustomerDetails(newVal.getCustomer(), newVal.getRoom());
            }
        } );
    }

     private void loadAndFilterData() {
        masterData.clear();
        bookingRepository.getAllBookings().stream()
            .map(booking -> new GuestsTableView(booking.getRoom(), booking.getCustomer(), booking))
            .forEach(masterData::add);

        // ตั้งค่าการกรองเริ่มต้นให้แสดงเฉพาะ CONFIRMED และ CHECKED_IN
        filteredData = new FilteredList<>(masterData, guest -> {
            BookingStatus status = guest.getBookings().getStatus();
            return status == BookingStatus.CONFIRMED || status == BookingStatus.CHECKED_IN;
        });     

        SortedList<GuestsTableView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(guestTable.comparatorProperty());
        guestTable.setItems(sortedData);
    }

    private void applyFilters(){
        String searchText = searchField.getText();
        String status = statusComboBox.getValue();
        filteredData.setPredicate(createPredicate(searchText, status));
    }

    private Predicate<GuestsTableView> createPredicate(String searchText, String status){
        //ส่งข้อมูลก็ต่อเมื่อ ....
        return guest -> {
            BookingStatus currentStatus = guest.getBookings().getStatus();
            if (currentStatus != BookingStatus.CONFIRMED && currentStatus != BookingStatus.CHECKED_IN) {
                return false; //ไม่ใช่คอนเฟิร์ม เช็คอินให้คัดออก
            }

            //ใช้ startsWith เพราะ ตอนค้นด้วย a มันดันเอาตัวที่ไม่ขึ้นต้นด้วย a มาด้วย 
            boolean searchMatch = true;
            if (searchText != null && !searchText.isEmpty() ) {
                String lowercase = searchText.toLowerCase();
                searchMatch = guest.getCustomer().getFirstnameCustomer().toLowerCase().startsWith(lowercase) ||
                              guest.getCustomer().getLastnameCustomer().toLowerCase().startsWith(lowercase) ||
                              guest.getNumberRoom().toLowerCase().startsWith(lowercase)||
                              guest.getCustomer().getidCard().toLowerCase().startsWith(lowercase); 
                }

            boolean statusMatch = true;
            if (status != null && !status.equalsIgnoreCase("All status")) {
                statusMatch = guest.getStatus().equalsIgnoreCase(status);
            }

            return searchMatch && statusMatch;
        };
    }

    //อันนี้ที่แยกออกมาเพราะตาลาย
    private void setupTableColumns(){
        guestTable.setEditable(true);
        RoomNoColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("numberRoom"));
        IdCardColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("idCard"));
        CheckInColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("checkin"));
        CheckOutColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("checkout"));
        StatusColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("status"));

        setupEditabeColumns();
    }

    private void setupEditabeColumns(){
        
        // กำหนดสิ่งที่แก้ได้
        FirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        FirstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        FirstNameColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setFirstName(event.getNewValue());
            guest.getCustomer().setFirstnameCustomer(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        LastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        LastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        LastNameColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setLastName(event.getNewValue());
            guest.getCustomer().setLastnameCustomer(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        EmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        EmailColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setEmail(event.getNewValue());
            guest.getCustomer().setEmail(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        PhoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        PhoneColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setPhone(event.getNewValue());
            guest.getCustomer().setPhone(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        AddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        AddressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        AddressColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setAddress(event.getNewValue());
            guest.getCustomer().setAddress(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        CityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        CityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CityColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setCity(event.getNewValue());
            guest.getCustomer().setCity(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        CountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        CountryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CountryColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setCountry(event.getNewValue());
            guest.getCustomer().setCountry(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        ObservableList<String> allgender = FXCollections.observableArrayList(
       "male",
                "Female",      
                "Other"    
        );

        GenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        GenderColumn.setCellFactory(ComboBoxTableCell.forTableColumn(allgender));
        GenderColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            String newGuest = event.getNewValue();
            guest.setGender(newGuest);
            guest.getCustomer().setGender(newGuest);
            customerRepository.saveCustomerToCSV();
        });

        StatusColumn.setOnEditCommit(event -> {
            GuestsTableView selectedGuest = event.getRowValue();
            BookingStatus newStatus;

            try {
                newStatus = BookingStatus.valueOf(event.getNewValue());
            } catch (IllegalArgumentException e) {
                guestTable.refresh();
                return ;
            }

            BookingStatus oldStatus = selectedGuest.getBookings().getStatus();

            if (newStatus == BookingStatus.CHECKED_IN) {
                if (oldStatus == BookingStatus.CONFIRMED) {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "เช็คอินลูกค้าชื่อ : " + selectedGuest.getFirstName() + " กับห้อง : " + selectedGuest.getNumberRoom() + "?", ButtonType.YES, ButtonType.NO);
                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            selectedGuest.getBookings().setStatus(BookingStatus.CHECKED_IN);
                            bookingRepository.saveBookingToCSV();
                        }
                        guestTable.refresh();
                    });
                } else {
                    new Alert(Alert.AlertType.WARNING, "Can only check in a CONFIRMED booking.").showAndWait();
                    guestTable.refresh();
                }
            }
           
            else if (newStatus == BookingStatus.CHECKED_OUT) {
                if (oldStatus == BookingStatus.CHECKED_IN) {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "เช็คอินลูกค้าชื่อ : " + selectedGuest.getFirstName() + " จากห้อง : " + selectedGuest.getNumberRoom() + "?", ButtonType.YES, ButtonType.NO);
                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            selectedGuest.getBookings().setStatus(BookingStatus.CHECKED_OUT);
                            bookingRepository.saveBookingToCSV();
                            
                            Room room = selectedGuest.getRoom();
                            room.setStatus(RoomStatus.CLEANING);
                            room.setLastCheckouTime(LocalDateTime.now());
                            roomRepository.saveRoomToCSV();
                        }
                        guestTable.refresh();
                    });
                } else {
                    new Alert(Alert.AlertType.WARNING, "สามารถเช็คเอาท์ได้เฉพาะลูกค้าที่เช็คอินเท่านั้น").showAndWait();
                    guestTable.refresh();
                }
            }
            
            else {
                selectedGuest.getBookings().setStatus(newStatus);
                bookingRepository.saveBookingToCSV();
                guestTable.refresh();
            }
        });

    


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




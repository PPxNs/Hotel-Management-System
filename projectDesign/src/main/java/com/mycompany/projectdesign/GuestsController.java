/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package com.mycompany.projectdesign;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.function.Predicate;


import com.mycompany.projectdesign.Project.Model.*;
import com.mycompany.projectdesign.Project.ObserverPattern.HotelEvent;
import com.mycompany.projectdesign.Project.ObserverPattern.HotelEventManager;
import com.mycompany.projectdesign.Project.ObserverPattern.HotelObserver;
import com.mycompany.projectdesign.Project.ObserverPattern.RoomStatusUpdatedEvent;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * Controller GUI สำหรับหน้าจอจัดการข้อมูลลูกค้า (Guests.fxml)
 * ทำหน้าที่จัดการการแสดงผล, ค้นหา, กรอง, และแก้ไขข้อมูลแขกที่กำลังเข้าพักหรือยืนยันการจอง
 * รวมทั้งจัดการ Logic การเช็คอินและเช็คเอาท์ 
 */
public class GuestsController implements Initializable, HotelObserver{

    // @FXML Components: ส่วนเกี่ยวกับคอลลัมแสดงข้อมูล
    @FXML private TableView<GuestsTableView> guestTable;
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

    //  @FXML Components: ส่วนแสดงรายละเอียดด้านข้าง (ด้านขวา)
    @FXML private Label LabelFullname;
    @FXML private Label LabelFirstName;
    @FXML private Label LabelLastName;
    @FXML private Label LabelEmail;
    @FXML private Label LabelPhone;
    @FXML private Label LabelGender;
    @FXML private Label LabelCountry;
    @FXML private Label LabelCity;
    @FXML private Label LabelAddress;
    
    // @FXML Components: ส่วนกรองข้อมูล 
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField searchField;

    
    // แหล่งข้อมูลหลักของระบบ
    private CustomerRepository customerRepository = CustomerRepository.getInstance();
    private RoomRepository roomRepository = RoomRepository.getInstance();
    private BookingRepository bookingRepository = BookingRepository.getInstance();

    // masterData: เก็บข้อมูลทั้งหมดที่โหลดมา
    private ObservableList<GuestsTableView> masterData = FXCollections.observableArrayList();
    // filteredData: เลเยอร์สำหรับกรองข้อมูลจาก masterData
    private FilteredList<GuestsTableView> filteredData;

    /**
     * เมธอดหลักที่ถูกเรียกโดยอัตโนมัติเมื่อ FXML โหลดเสร็จ
     * ใช้สำหรับตั้งค่าเริ่มต้นทั้งหมดของหน้าจอ
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //set ข้อมูลที่จะแสดงในแต่ละคอลลัม
        setupTableColumns();

        CheckInColumn.setSortType(TableColumn.SortType.ASCENDING); // กำหนดให้เรียงจากน้อยไปมาก
        guestTable.getSortOrder().add(CheckInColumn); // สั่งให้ TableView ใช้คอลัมน์นี้ในการเรียงเป็นอันดับแรก
        //กำหนด item ใน combobox
        ObservableList<String> allStatus = FXCollections.observableArrayList(
       "All Status",
                "CONFIRMED",      
                "CHECKED_IN"    
        );

        //set item ใน combobox
        statusComboBox.setItems(allStatus);
        statusComboBox.setValue("All Status");

        //โหลดค่าไปใส่ในคอลลัม
        loadAndFilterData();

        // เมื่อมีการพิมพ์ในช่องค้นหา ให้ทำการกรองข้อมูลใหม่
        searchField.textProperty().addListener((obs,oldVal,newVal)-> applyFilters() );
        // เมื่อมีการเปลี่ยนค่าใน ComboBox status ให้ทำการกรองข้อมูลใหม่
        statusComboBox.valueProperty().addListener((obs,oldVal,newVal)-> applyFilters() );
        // เมื่อมีการคลิกเลือกแถวในตาราง ให้แสดงรายละเอียดด้านข้าง
        guestTable.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal)-> {
            if (newVal != null) {
                showCustomerDetails(newVal.getCustomer(), newVal.getRoom());
            }
        } );

        HotelEventManager.getInstance().addObserver(this);

    }

    /**
     * โหลดข้อมูลจาก BookingRepository แปลงเป็น GuestsTableView
     * (masterData -> filteredData -> sortedData -> Table)
     */
    private void loadAndFilterData() {
        masterData.clear();
        
        // โหลดข้อมูลทั้งหมดจาก BookingRepository แล้วแปลงเป็นรูปแบบที่ใช้ในตาราง
        bookingRepository.getAllBookings().stream()
            .map(booking -> new GuestsTableView(booking.getRoom(), booking.getCustomer(), booking))
            //หยิบชุดข้อมูลของการจอง เก็บเข้า masterData ทีละการจอง
            .forEach(masterData::add); // or item -> masterData.add(item)

        /* ตั้งค่าการกรองเริ่มต้นให้แสดงเฉพาะ CONFIRMED และ CHECKED_IN */ 
        // สร้าง FilteredList เพื่อกรองข้อมูลจาก masterData
        filteredData = new FilteredList<>(masterData, guest -> {
            BookingStatus status = guest.getBookings().getStatus();
            return status == BookingStatus.CONFIRMED || status == BookingStatus.CHECKED_IN;
        });     

        // สร้าง SortedList เพื่อจัดเรียงข้อมูลจาก filteredData
        SortedList<GuestsTableView> sortedData = new SortedList<>(filteredData);
        // ผูกการจัดเรียงของ SortedList ให้ตรงกับการจัดเรียงของ TableView
        sortedData.comparatorProperty().bind(guestTable.comparatorProperty());
        guestTable.setItems(sortedData);
    }

    /**
     * สั่งให้ FilteredList ทำการกรองข้อมูลใหม่โดยใช้เงื่อนไขจาก searchField และ statusComboBox
     */
    private void applyFilters(){
        String searchText = searchField.getText();
        String status = statusComboBox.getValue();
        filteredData.setPredicate(createPredicate(searchText, status));
    }


    /**
     * สร้าง Predicate (เงื่อนไข) สำหรับใช้ในการกรองข้อมูลใน FilteredList
     * @param searchText ข้อความจากช่องค้นหา
     * @param status สถานะที่เลือกจาก ComboBox
     * @return Predicate ที่รวมเงื่อนไขการค้นหาและสถานะ
     */
    private Predicate<GuestsTableView> createPredicate(String searchText, String status){
        //ส่งข้อมูลก็ต่อเมื่อ ....
        return guest -> {
            BookingStatus currentStatus = guest.getBookings().getStatus();
            if (currentStatus != BookingStatus.CONFIRMED && currentStatus != BookingStatus.CHECKED_IN) {
                return false; //ไม่ใช่คอนเฟิร์ม เช็คอินให้คัดออก
            }

            //ต้องตรงกับข้อความค้นหา (ถ้ามี)
            //ใช้ startsWith เพราะ ตอนค้นด้วย a มันดันเอาตัวที่ไม่ขึ้นต้นด้วย a มาด้วย 
            boolean searchMatch = true;
            if (searchText != null && !searchText.isEmpty() ) {
                String lowercase = searchText.toLowerCase();
                searchMatch = guest.getCustomer().getFirstnameCustomer().toLowerCase().startsWith(lowercase) ||
                              guest.getCustomer().getLastnameCustomer().toLowerCase().startsWith(lowercase) ||
                              guest.getNumberRoom().toLowerCase().startsWith(lowercase)||
                              guest.getCustomer().getidCard().toLowerCase().startsWith(lowercase); 
                }

            //ต้องตรงกับสถานะที่เลือก (ถ้ามี)
            boolean statusMatch = true;
            if (status != null && !status.equalsIgnoreCase("All status")) {
                statusMatch = guest.getStatus().equalsIgnoreCase(status);
            }

            return searchMatch && statusMatch;
        };
    }

    /**
     * ตั้งค่าการผูกข้อมูลระหว่างคอลัมน์กับ Property ของ GuestsTableView
     */
    private void setupTableColumns(){
        guestTable.setEditable(true);
        RoomNoColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("numberRoom"));
        IdCardColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("idCard"));
        CheckInColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("checkin"));
        CheckOutColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("checkout"));
        StatusColumn.setCellValueFactory(new PropertyValueFactory<GuestsTableView,String> ("status"));

         StatusColumn.setCellFactory(column -> {
        return new TableCell<GuestsTableView, String>() {
            private final Label statusLabel = new Label();
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty); 

                if (item == null || empty) {
                    setGraphic(null); // เคลียร์ Graphic เมื่อเซลล์ว่าง
                } else {
                    // กำหนดข้อความให้ Label
                    statusLabel.setText(item);
                    String style = "";

                    // กำหนดสไตล์สำหรับ Label ตามสถานะ
                    switch (item) {
                        case "CONFIRMED":
                            
                            style = "-fx-background-color: #77b9ff; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 3 10 3 10; -fx-font-weight: bold;";
                            break;
                        case "CHECKED_IN":
                            style = "-fx-background-color: #6eff90ff; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 3 10 3 10; -fx-font-weight: bold;";
                            break;
                        default:
                            return;
                    }
                    
                    // กำหนดสไตล์ให้กับ Label 
                    statusLabel.setStyle(style);

                    // นำ Label ไปใส่ในเซลล์ และจัดให้อยู่ตรงกลาง
                    setGraphic(statusLabel);
                    setText(null);
                    setAlignment(Pos.CENTER);
                }
            }
        };
    });

        setupEditabeColumns();
    }

    /**
     * ตั้งค่าคอลัมน์ที่สามารถแก้ไขได้ และ Logic การทำงานเมื่อแก้ไขเสร็จ
     */
    private void setupEditabeColumns(){
        
        // กำหนดสิ่งที่แก้ได้
        // ตั้งค่าการแก้ไข FirstName
        FirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        FirstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        FirstNameColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setFirstName(event.getNewValue());
            guest.getCustomer().setFirstnameCustomer(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        //ตั้งค่าการแก้ไข LastName
        LastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        LastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        LastNameColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setLastName(event.getNewValue());
            guest.getCustomer().setLastnameCustomer(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        //ตั้งค่าการแก้ไข Email
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        EmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        EmailColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setEmail(event.getNewValue());
            guest.getCustomer().setEmail(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        //ตั้งค่าการแก้ไข Phone
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        PhoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        PhoneColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setPhone(event.getNewValue());
            guest.getCustomer().setPhone(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        //ตั้งค่าการแก้ไข Address
        AddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        AddressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        AddressColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setAddress(event.getNewValue());
            guest.getCustomer().setAddress(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        //ตั้งค่าการแก้ไข City
        CityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        CityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CityColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setCity(event.getNewValue());
            guest.getCustomer().setCity(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        //ตั้งค่าการแก้ไข Country
        CountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        CountryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        CountryColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            guest.setCountry(event.getNewValue());
            guest.getCustomer().setCountry(event.getNewValue());
            customerRepository.saveCustomerToCSV();
        });

        //กำหนด item ใน combobox
        ObservableList<String> allgender = FXCollections.observableArrayList(
       "male",
                "Female",      
                "Other"    
        );

        //ตั้งค่าการแก้ไข Gender
        GenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        GenderColumn.setCellFactory(ComboBoxTableCell.forTableColumn(allgender));
        GenderColumn.setOnEditCommit(event -> {
            GuestsTableView guest = event.getRowValue();
            String newGuest = event.getNewValue();
            guest.setGender(newGuest);
            guest.getCustomer().setGender(newGuest);
            customerRepository.saveCustomerToCSV();
        });

        // ตั้งค่าการแก้ไข Status (Check-in / Check-out)
        StatusColumn.setOnEditCommit(event -> {
            GuestsTableView selectedGuest = event.getRowValue();    
            BookingStatus oldStatus = selectedGuest.getBookings().getStatus();
            BookingStatus newStatus;

            try {
                newStatus = BookingStatus.valueOf(event.getNewValue());
            } catch (IllegalArgumentException e) {
                guestTable.refresh(); // ถ้าใส่ค่าไม่ถูกต้อง ให้รีเฟรชกลับเป็นค่าเดิม
                return ;
            }

            // Logic การ Check-in 
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
           
            // Logic การ Check-out 
            else if (newStatus == BookingStatus.CHECKED_OUT) {
                if (oldStatus == BookingStatus.CHECKED_IN) {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "เช็คอินลูกค้าชื่อ : " + selectedGuest.getFirstName() + " จากห้อง : " + selectedGuest.getNumberRoom() + "?", ButtonType.YES, ButtonType.NO);
                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            selectedGuest.getBookings().setStatus(BookingStatus.CHECKED_OUT);
                            bookingRepository.saveBookingToCSV();
                            
                            Room room = selectedGuest.getRoom();
                            room.setStatus(RoomStatus.CLEANING);
                            room.setLastCheckoutTime(LocalDateTime.now());
                            roomRepository.saveRoomToCSV();
                        }
                        guestTable.refresh();
                    });
                } else {
                    new Alert(Alert.AlertType.WARNING, "สามารถเช็คเอาท์ได้เฉพาะลูกค้าที่เช็คอินเท่านั้น").showAndWait();
                    guestTable.refresh();
                }
            }
            // Logic การเปลี่ยนสถานะอื่นๆ 
            else {
                selectedGuest.getBookings().setStatus(newStatus);
                bookingRepository.saveBookingToCSV();
                guestTable.refresh();
            }
        });
    }

    /**
     * แสดงรายละเอียดของลูกค้าและห้องพักที่เลือกใน Panel ด้านข้าง (ข้านขวา)
     * @param customer ลูกค้าที่ถูกเลือก
     * @param room ห้องพักที่เกี่ยวข้อง
     */
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

    @Override
    public void update(HotelEvent event) {
            if (event instanceof RoomStatusUpdatedEvent) {
            // Platform.runLater คือคำสั่ง "บังคับ" ให้โค้ดข้างในไปทำงานบน UI Thread
            // ซึ่งเป็น Thread เดียวที่สามารถแก้ไขหน้าจอได้อย่างปลอดภัย
            Platform.runLater(()->{
                guestTable.refresh();
            });

        }
    }

}




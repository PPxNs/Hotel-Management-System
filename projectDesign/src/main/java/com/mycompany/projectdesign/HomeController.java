/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectdesign;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javafx.scene.control.Label;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.util.Callback; 

import com.mycompany.projectdesign.Project.DecoratorPattern.*;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.*;
import com.mycompany.projectdesign.Project.Model.*;
import com.mycompany.projectdesign.Project.ObserverPattern.*;
import com.mycompany.projectdesign.Project.Service.*;
import com.mycompany.projectdesign.Project.StrategyPattern.*;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

public class HomeController implements Initializable {

    @FXML private TextField minRangeField;
    @FXML private TextField maxRangeField;
    @FXML private TextField idCardField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField cityField;
    @FXML private TextField dayMealTextfield;

    @FXML private ComboBox<String> roomNoComboBox;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private ComboBox<Integer> numberPeopleComboBox;
    @FXML private ComboBox<String> checkinTimeCombobox;
    @FXML private ComboBox<String> checkoutTimeCombobox;
    @FXML private ComboBox<String> checkinTimeCombobox1;
    @FXML private ComboBox<String> checkoutTimeCombobox1;
    @FXML private ComboBox<String> countryCombobox;

    @FXML private RadioButton maleRadioButton;
    @FXML private RadioButton femaleRadioButton;
    @FXML private RadioButton otherRadioButton;

    @FXML private CheckBox jacuzziCheckBox;
    @FXML private CheckBox lakeViewCheckBox;
    @FXML private CheckBox petFriendlyCheckBox;
    @FXML private CheckBox privatePoolCheckBox;
    @FXML private CheckBox tvCheckBox;
    @FXML private CheckBox wifiCheckBox;
    @FXML private CheckBox cleaningRoomCheckBox;
    @FXML private CheckBox mealCheckBox;
    @FXML private CheckBox pickupServiceCheckbox;


    @FXML private DatePicker checkinDatePicker;
    @FXML private DatePicker checkoutDatePicker;
    @FXML private DatePicker checkinDatePicker1;
    @FXML private DatePicker checkoutDatePicker1;

    @FXML private Label totalLabel;
    @FXML private Label dayMealLabel;
    @FXML private Label selectedRoomTypeLabel; 
    @FXML private Label selectedRoomPeopleLabel;
    @FXML private Label selectedRoomPriceLabel;
    @FXML private Label selectedRoomPropertiesLabel;
 

    @FXML private TableView<HomeTableView> bookingTable;

    //ดึงแต่ละ coluumn
    @FXML private TableColumn<HomeTableView,String> bookingIDColumn;
    @FXML private TableColumn<HomeTableView,String> newGuestColumn;    
    @FXML private TableColumn<HomeTableView,String> roomNumberColumn;
    @FXML private TableColumn<HomeTableView,String> checkinColumn;
    @FXML private TableColumn<HomeTableView,String> checkoutColumn;
    @FXML private TableColumn<HomeTableView,String> amountPaidColumn;
    @FXML private TableColumn<HomeTableView,String> statusColumn;   

    private List<Room> allRooms;
    private RoomRepository roomRepository = RoomRepository.getInstance();
    private CustomerRepository customerRepository = CustomerRepository.getInstance();
    private BookingRepository bookingRepository = BookingRepository.getInstance();
    private AmountPaidRepository amountPaidRepository = AmountPaidRepository.getInstance();
    private List<CheckBox> allCheckbox;
    private ToggleGroup genderToggleGroup;
    private HotelEventManager eventManager = HotelEventManager.getInstance();
    private ObservableList<HomeTableView> homeBookingList = FXCollections.observableArrayList();
    private final RoomService roomService = new RoomService();
    private BookingScheduler bookingScheduler;
    
     /**
     * เมธอดหลักที่ถูกเรียกโดยอัตโนมัติเมื่อ FXML โหลดเสร็จ
     * ใช้สำหรับตั้งค่าเริ่มต้นทั้งหมดของหน้าจอ
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //โหลดข้อมูลห้องพักเข้าลิส
        allRooms = new ArrayList<>(roomRepository.getAllRooms());

        //เซตเวลาของ checkin
        ObservableList<String> timeCheckin = FXCollections.observableArrayList(
  "14:00" , "14:30", "15:00" , "15:30", "16:00" , "16:30", "17:00" , 
           "17:30", "18:00" , "18:30", "19:00" , "19:30", "20:00"
        );

        //เซตเวลาของ checkout
        ObservableList<String> timeCheckout = FXCollections.observableArrayList(
  "08:00" , "08:30", "09:00" , "09:30", "10:00" , "10:30", "11:00" , 
           "11:30", "12:00" , "12:30", "13:00","13:30", "14:00"
        );

        //เซตประเภทของห้องพัก
        ObservableList<String> roomType = FXCollections.observableArrayList(
   "Single room",
            "Double room",
            "Twin room",
            "suite"
        );

        //เซตจำนวนสูงสุดที่ห้องพักรองรับคนได้
        ObservableList<Integer> people = FXCollections.observableArrayList(
            1,2,3,4
        );

        //เซตตัวปุ่ม RadioButton
        genderToggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);
        otherRadioButton.setToggleGroup(genderToggleGroup);

        //รวมเช็คบล็อกทั้งหมด ไว้เอาไว้เคลียร๋
        allCheckbox = Arrays.asList(jacuzziCheckBox, lakeViewCheckBox,petFriendlyCheckBox,privatePoolCheckBox,tvCheckBox,wifiCheckBox,cleaningRoomCheckBox,mealCheckBox, pickupServiceCheckbox);

        //ตั้งค่าคอลัมน์และการผูกข้อมูลสำหรับ TableView หลัก
        bookingIDColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("bookingID"));
        newGuestColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("fullnameCustomer"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("numberRoom"));
        checkinColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("checkin"));
        checkoutColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("checkout"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("status"));
        amountPaidColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("amount"));
        
        // โหลดข้อมูลเริ่มต้นสำหรับตารางคอลลัม
        for (Bookings booking : bookingRepository.getAllBookings()) {
            AmountPaid paid = amountPaidRepository.getAmountByBookingID(booking.getBookingID());
            if (paid != null) {
                homeBookingList.add(new HomeTableView(booking, paid));
            } else {
                // ถ้า booking ยังไม่มีการจ่ายเงิน ให้สร้าง AmountPaid ที่มีค่าเป็น 0.0 เพื่อแสดงผล
                homeBookingList.add(new HomeTableView(booking, new AmountPaid(booking, 0.00)));
            }
        }


        bookingTable.setItems(homeBookingList);

        //เซตค่าที่แสดงในคอมโบบ็อก
        checkinTimeCombobox.setItems(timeCheckin);
        checkoutTimeCombobox.setItems(timeCheckout);
        checkinTimeCombobox1.setItems(timeCheckin);
        checkoutTimeCombobox1.setItems(timeCheckout);
        roomTypeComboBox.setItems(roomType);
        numberPeopleComboBox.setItems(people);
        loadCountriesFromCSV();
        addFiterListeners();
        updateAvailableRoom();

        roomNoComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            Callback<DatePicker, DateCell> factory;
            if (newVal != null && !newVal.isEmpty()) {
                Room selectedRoom = roomRepository.getRoom(newVal);
                factory = createDayCellFactoryForRoom(selectedRoom);
            }else{
                factory = createDefaultDayCellFactory();
            }

            checkinDatePicker.setDayCellFactory(factory);
            checkoutDatePicker.setDayCellFactory(factory);
            getDataFormRoomSelected(newVal);
            
        });

        addPriceUpdateListeners();
        setupDatePickers();

        clearSelectedRoomDetails();
        bookingScheduler = new BookingScheduler();
        bookingScheduler.start();
    }
    
    /**
     * เพิ่มการจองใหม่เข้าตารางสรุปด้านล่าง (ป้องกันการเพิ่มซ้ำ)
     */
    private void addBookingToTable(HomeTableView newEntry){

    // ใช้ Stream API เพื่อตรวจสอบว่ามี Booking ID นี้ใน List แล้วหรือยัง
    boolean exists = homeBookingList.stream()
        .anyMatch(h -> h.getBookingID().equals(newEntry.getBookingID()));

    // ถ้ายังไม่มี ให้เพิ่มข้อมูลใหม่เข้าไป
    if (!exists) {
        homeBookingList.add(newEntry);
        }
    }


    /**
     * โหลดรายชื่อประเทศจากไฟล์ CSV ภายในโปรเจค
     */
    private void loadCountriesFromCSV() {
        ObservableList<String> countryList = FXCollections.observableArrayList();
        //ทำแบบที่จารย์สอนมันหาไฟล์ไม่เจอ คิดว่าน่าจะเป็นมันเป็น JAVAFX มั้ง
        String csvFilePath = "/CSV/countryList.csv";
        try (
            InputStream is = getClass().getResourceAsStream(csvFilePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line != null && !line.trim().isEmpty()) {
                    countryList.add(line);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } 
        
        countryCombobox.setItems(countryList);
    }

    /**
     * ค้นหาห้องว่างตามเงื่อนไขในฟอร์ม Filter
     * และอัปเดตผลลัพธ์ลงใน roomNoComboBox
     */
    private void updateAvailableRoom(){
        //ดึงข้อมูลมาเพื่อจะเช็ค
        String selectedRoomType = roomTypeComboBox.getValue();
        Integer selectedNumberPeople = numberPeopleComboBox.getValue();

        //แปลงให้เป็น double ก่อน
        Double minPrice = null ;
        Double maxPrice = null;
        try {
            
            if (minRangeField.getText() != null && !minRangeField.getText().isEmpty()) {
                minPrice = Double.parseDouble(minRangeField.getText());
            }

            if (maxRangeField.getText() != null && !maxRangeField.getText().isEmpty()) {
                maxPrice = Double.parseDouble(maxRangeField.getText());
            }

        } catch (Exception e) {
            System.out.println("Invalid price format.");
            new Alert(Alert.AlertType.WARNING, "รูปแบบราคาห้องไม่ถูกต้อง กรุณากรอกเฉพาะตัวเลข").showAndWait();
        }

        List<String> selectedProperties = new ArrayList<>();
        if (jacuzziCheckBox.isSelected()) selectedProperties.add("Jacuzzi");
        if (lakeViewCheckBox.isSelected()) selectedProperties.add("Lake View");
        if (petFriendlyCheckBox.isSelected()) selectedProperties.add("Pet Friendly");
        if (privatePoolCheckBox.isSelected()) selectedProperties.add("Private Pool");
        if (tvCheckBox.isSelected()) selectedProperties.add("TV 58” 4K UHD LED");
        if (wifiCheckBox.isSelected()) selectedProperties.add("WIFI");

        LocalDate checkinDate = checkinDatePicker.getValue();
        LocalDate checkoutDate = checkoutDatePicker.getValue();

        //แปลงให้เป็น LocalTime ก่อน
        LocalTime checkinTime = null;
        LocalTime checkoutTime = null ;

        try {
            if (checkinTimeCombobox.getValue() != null) {
                checkinTime = LocalTime.parse(checkinTimeCombobox.getValue());
            }

            if (checkoutTimeCombobox.getValue() != null) {
                checkoutTime = LocalTime.parse(checkoutTimeCombobox.getValue());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // วน Loop เพื่อกรองห้องที่เข้าเงื่อนไข
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : allRooms){
        
                boolean match = true;

                if (selectedRoomType != null){ 
                    if(!room.getType().equalsIgnoreCase(selectedRoomType))
                        match = false;
                }

                if (selectedNumberPeople != null){
                    if (room.getPeople() < selectedNumberPeople) {
                        match = false;
                    }
                }  
                
                if ( minPrice != null) {
                    if (room.getPrice() < minPrice) {
                        match = false;
                    }
                }

                if (maxPrice !=null) {
                    if (room.getPrice() > maxPrice) {
                        match = false;
                    }
                }

                if (!selectedProperties.isEmpty()) {
                    if (!room.getProperties().containsAll(selectedProperties)) {
                        match = false;
                    }
                }

                RoomStatus realTimeStatus = roomService.getRealTimeStatus(room);
                //ข้ามห้องนี้ไปเลยถ้าซ่อมอยู่
                if (realTimeStatus == RoomStatus.MAINTENANCE){
                    continue;
                }
                
                if(realTimeStatus == RoomStatus.OCCUPIED) {
                    match = false;
                }

                if (checkinDate != null && checkoutDate != null && checkinTime != null && checkoutTime != null){
                    if (!isRoomAvailable(room, checkinDate, checkinTime, checkoutDate, checkoutTime)) {
                        match = false;
                    }
                }  

                if (match) {
                    availableRooms.add(room);
                }

            }

        // จัดการกรณีที่ไม่ได้เลือก Filter ใดๆ
        boolean FilterSelected = selectedRoomType != null || selectedNumberPeople !=null || minPrice != null 
                                || maxPrice != null || !selectedProperties.isEmpty() || (checkinDate != null && checkoutDate != null && checkinTime != null && checkoutTime != null) ;

        //ถ้าไม่ได้เลือกค้นห้อง ให้ข้ามห้องที่ซ่อมไปด้วย ให้มันไม่แสดง
        if (!FilterSelected) {
            availableRooms = allRooms.stream()
                            .filter(room -> room.getStatus() != RoomStatus.MAINTENANCE)
                            .collect(Collectors.toList());
        }

        // อัปเดตผลลัพธ์ใน ComboBox
        roomNoComboBox.setItems(FXCollections.observableArrayList(
            availableRooms.stream().map(Room::getNumberRoom).toList()
        ));
       

        
        
    }

    /**
     * ตรวจสอบว่าห้องที่ระบุว่างในช่วงเวลาที่ต้องการจองหรือไม่ โดยเช็คการจองซ้อนทับ
     * @param room          ห้องพักที่ต้องการตรวจสอบ
     * @param checkinDate   วันที่เช็คอินที่ต้องการ
     * @param checkinTime   เวลาเช็คอินที่ต้องการ
     * @param checkoutDate  วันที่เช็คเอาท์ที่ต้องการ
     * @param checkoutTime  เวลาเช็คเอาท์ที่ต้องการ
     * @return true ถ้าห้องว่างในช่วงเวลาดังกล่าว, false ถ้ามีการจองซ้อนทับ
     */
    private boolean isRoomAvailable(Room room, LocalDate checkinDate, LocalTime checkinTime, LocalDate checkoutDate, LocalTime checkoutTime){
        
        //ต่อวันกับเวลาเข้ากัน
        LocalDateTime checkin = checkinDate.atTime(checkinTime);
        LocalDateTime checkout = checkoutDate.atTime(checkoutTime);

        // วน Loop ตรวจสอบกับการจองทั้งหมดที่มีอยู่ในระบบ
        for(Bookings booking : bookingRepository.getAllBookings()){
            //เพิ่มกรณีช่วงทำความสะอาดห้องด้วยให้จองไม่ได้
            if (booking.getRoom().getNumberRoom().equals(room.getNumberRoom())) {
                if (booking.getStatus() == BookingStatus.CONFIRMED || booking.getStatus() == BookingStatus.CHECKED_IN || booking.getStatus() == BookingStatus.CHECKED_OUT) {
                    LocalDateTime existingStart = booking.getDateCheckin().atTime(booking.getTimeCheckin());
                    LocalDateTime existingEnd = booking.getDateCheckout().atTime(booking.getTimeCheckout());
        
                    // เพิ่มกฎ "ช่วงเวลาทำความสะอาด" 30 นาที ต่อท้ายการจองเดิม
                    LocalDateTime effectiveEnd = existingEnd.plusMinutes(30);
                    // เงื่อนไขการทับซ้อน: (StartA < EndB) and (EndA > StartB)
                    if (checkin.isBefore(effectiveEnd) && checkout.isAfter(existingStart)) {
                    return false; //ทับกัน
                    }                    
                }

            }
        }
        return true; //ว่าง ไม่มีเวลาทับซ้อน
    }


    /**
     * สร้าง DayCellFactory เริ่มต้น ปิดการเลือกวันในอดีต
     * @return Callback ที่สามารถนำไปใช้กับ DatePicker.setDayCellFactory()
     */
    //ให้ปฎิทินเลือกวันในอดีตไม่ได้
    private  Callback<DatePicker, DateCell> createDefaultDayCellFactory(){
        return picker -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                // สั่งให้ Disable เซลล์ ถ้าเซลล์นั้นว่าง หรือเป็นวันที่อยู่ก่อนหน้าวันปัจจุบัน
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
            
        };
    }

    /**
     * สร้าง DayCellFactory แบบไดนามิก ที่ปิดการเลือกวันในอดีตและวันที่ถูกจองไปแล้วสำหรับห้องพักที่ระบุ
     * @param roomToCheck ห้องพักที่ต้องการตรวจสอบตารางการจอง
     * @return Callback ที่สามารถสร้าง DateCell พร้อมกฎที่กำหนดเองได้
     */
    private  Callback<DatePicker, DateCell> createDayCellFactoryForRoom(Room roomToCheck){
        // ถ้าไม่มีห้องที่ถูกเลือก ให้ใช้ factory เริ่มต้น (ปิดแค่วันในอดีต)
        if (roomToCheck == null) {
            return createDefaultDayCellFactory(); //กรณีไม่มีห้อง
        }

        // กรองข้อมูลการจองของห้องนี้เก็บไว้ล่วงหน้า 
        // เพื่อไม่ต้องค้นหาใหม่ทุกครั้งที่วาดเซลล์แต่ละวัน
        List<Bookings> roomBookings = bookingRepository.getAllBookings().stream()
                                                       .filter(b -> b.getRoom().getNumberRoom().equals(roomToCheck.getNumberRoom())
                                                       && (b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.CHECKED_IN))
                                                       .collect(Collectors.toList());

        // คืนค่า Callback ที่จะถูกใช้ในการสร้าง DateCell
        return picker -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);

                //ปิดวันในอดีต
                if (empty || date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    return;
                }

                //ตรวจจองซ้อน
                //ตรวจสอบว่าวันที่นี้มีการจองซ้อนทับหรือไม่
                boolean isBooking = false;
                for(Bookings booking : roomBookings){
                    LocalDate checkinDate = booking.getDateCheckin();
                    LocalDate checkoutDate = booking.getDateCheckout();

                    // ตรวจสอบว่า 'date' อยู่ระหว่าง [checkinDate, checkoutDate)
                    if (!date.isBefore(checkinDate) && date.isBefore(checkoutDate)) {
                        isBooking = true;
                        break; // เจอการจองซ้อนแล้ว ไม่ต้องหาต่อ
                    }
                }

                if (isBooking) {
                    setDisable(true); // ทำให้คลิกไม่ได้
                    //แสดงให้เห็นสีวันจอง
                    setStyle("-fx-background-color: #ffc0cb;"); 
                }
            }
        };
            
    }

    /**
     * ตั้งค่า Logic การทำงานของ DatePickers
     */
    private void setupDatePickers(){
         // กำหนดกฎเริ่มต้นให้กับ DatePicker ทั้งหมด 
        Callback<DatePicker, DateCell> defaultFactory = createDefaultDayCellFactory();
        checkinDatePicker.setDayCellFactory(defaultFactory);
        checkoutDatePicker.setDayCellFactory(defaultFactory);
        checkinDatePicker1.setDayCellFactory(createDefaultDayCellFactory());

        // เพิ่ม Listener เพื่อสร้างความสัมพันธ์ระหว่าง check-in และ check-out ในฟอร์มการจอง 
        checkinDatePicker1.valueProperty().addListener((obs, oldDate, newDate) -> {
            checkoutDatePicker1.setValue(null);  // ล้างค่า checkout date เดิมทิ้ง

            // สร้างและติดตั้ง DayCellFactory ใหม่ให้กับ checkoutDatePicker1 ทันที
            final Callback<DatePicker, DateCell> dayCellFactory = picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (newDate != null) {
                         // ปิดการใช้งานทุกวันที่อยู่ก่อนหน้าหรือวันเดียวกับวัน check-in ที่เพิ่งเลือก
                        setDisable(empty || date.isBefore(newDate.plusDays(1)));
                    }else {
                        // ถ้ายังไม่ได้เลือก check-in ให้ใช้กฎเริ่มต้น
                        setDisable(empty || date.isBefore(LocalDate.now()));
                    }
                }
            };
            checkoutDatePicker1.setDayCellFactory(dayCellFactory);
        });
    }

    /**
     * ตั้งค่า Listeners สำหรับฟอร์มค้นหาห้องว่างทั้งหมด
     */
    private void addFiterListeners(){
       
        // เมื่อมีการเปลี่ยนแปลงค่าในฟอร์ม filter ให้เรียก updateAvailableRoom()
        checkinDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
        checkoutDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
        checkinTimeCombobox.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
        checkoutTimeCombobox.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
        roomTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
        numberPeopleComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());

        minRangeField.textProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
        maxRangeField.textProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());

        jacuzziCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
        lakeViewCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
        petFriendlyCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
        privatePoolCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
        tvCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
        wifiCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> updateAvailableRoom());
    }

    /**
     * ตั้งค่า Listeners สำหรับส่วนที่เกี่ยวข้องกับการคำนวณราคาสด
     */
    //เวลาคีย์พวกนี้จำนวนเงินก็จะขึ้นทันที
    private void addPriceUpdateListeners(){
        cleaningRoomCheckBox.selectedProperty().addListener((obs, o, n) -> updateTotalLabel());
        pickupServiceCheckbox.selectedProperty().addListener((obs, o, n) -> updateTotalLabel());
        roomNoComboBox.valueProperty().addListener((obs, o, n) -> updateTotalLabel());
        checkinDatePicker1.valueProperty().addListener((obs, o, n) -> updateTotalLabel());
        checkoutDatePicker1.valueProperty().addListener((obs, o, n) -> updateTotalLabel());
        mealCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            // เปิด/ปิด TextField จำนวนวัน ตามสถานะของ CheckBox
            dayMealTextfield.setDisable(!newVal);
            if(!newVal){
                dayMealTextfield.clear(); // ถ้าไม่เลือก ให้ล้างค่าทิ้ง
            }
            updateTotalLabel(); // เรียกคำนวณราคาใหม่
        });
        dayMealTextfield.textProperty().addListener((obs, oldVal, newVal) -> updateTotalLabel());
    }

    /**
     * ดึงข้อมูลจากฟอร์มส่วนของลูกค้า, ตรวจสอบความครบถ้วน, และสร้างเป็น Customer object
     * @return Customer object ที่มีข้อมูลครบ หรือ null หากข้อมูลไม่ครบถ้วน
     */
    public Customer getCustomerDatafromFome(){
        
        // ตรวจสอบว่ากรอกข้อมูลครบทุกช่องหรือไม่ 
        if (idCardField.getText().isEmpty() || firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
            emailField.getText().isEmpty() || phoneField.getText().isEmpty() || genderToggleGroup.getSelectedToggle() == null ||addressField.getText().isEmpty() ||
            cityField.getText().isEmpty() || countryCombobox.getValue() == null) {
                // ถ้าข้อมูลไม่ครบ แสดง Alert และส่งสัญญาณ `null` กลับไป
                Alert alert = new Alert(Alert.AlertType.WARNING, "โปรดกรอกข้อมูลให้ครบถ้วน");
                alert.showAndWait();
                return null ;

        } else{

        // ถ้าข้อมูลครบให้ดึงข้อมูลทั้งหมดที่ผู้ใช้กรอก
        String idCard = idCardField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        RadioButton selectedRadioButton = (RadioButton) genderToggleGroup.getSelectedToggle();
        String gender = selectedRadioButton.getText();

        String address = addressField.getText();
        String city = cityField.getText();
        String country = countryCombobox.getValue();

        // สร้างและคืนค่า Customer object ใหม่
        return new Customer(idCard, firstName, lastName, email, phone, gender, country , city, address);
        }
    }

    /**
     * ดึงข้อมูลจากฟอร์มส่วนของการจอง, ตรวจสอบความครบถ้วน, และสร้างเป็น Bookings object
     * @param customer Customer ที่จะใช้ในการสร้างการจองนี้
     * @return Bookings object ที่มีข้อมูลครบ หรือ null หากข้อมูลไม่ครบถ้วน
     */
    public Bookings getBookingDatafromFome(Customer customer){
        // ตรวจสอบว่ามีการเลือกห้องพักหรือไม่
        String roomNo = roomNoComboBox.getValue();
        if (roomNo == null || roomNo.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "กรุณาเลือกห้องพัก").showAndWait();
            return null;
        }

        Room room = roomRepository.getRoom(roomNo);

        // ตรวจสอบว่ามีการเลือก วัน-เวลา เช็คอิน/เช็คเอาท์ครบถ้วนหรือไม่
        if (checkinDatePicker1.getValue() == null || checkoutDatePicker1.getValue() == null ||
            checkinTimeCombobox1.getValue() == null || checkoutTimeCombobox1.getValue() == null) {
            
                new Alert(Alert.AlertType.WARNING, "กรุณากรอกข้อมูลวัน-เวลา เช็คอินและเช็คเอาท์ให้ครบถ้วน").showAndWait();
                return null ;

        } else{

        // ถ้าข้อมูลครบ ให้รวมและแปลงข้อมูล
        String bookingID = bookingRepository.generateNextBookingId(); // สร้าง ID การจองใหม่
        LocalDate checkinDate = checkinDatePicker1.getValue();
        LocalDate checkoutDate = checkoutDatePicker1.getValue();
        LocalTime checkinTime = LocalTime.parse(checkinTimeCombobox1.getValue()); // แปลง String เป็น LocalTime
        LocalTime checkoutTime = LocalTime.parse(checkoutTimeCombobox1.getValue()); // แปลง String เป็น LocalTime
        LocalDate bookingDate = LocalDate.now(); // สร้าง Timestamp ของวันที่จอง
        LocalTime bookingTime = LocalTime.now(); // สร้าง Timestamp ของเวลาที่จอง

        //สร้างและคืนค่า Bookings object ใหม่
        return new Bookings(room, customer, bookingID, checkinDate, checkinTime, checkoutDate, checkoutTime, bookingDate, bookingTime);
        }
    }

    /**
     * จัดการกระบวนการทำงานหลักในการตรวจสอบและบันทึกการจองใหม่
     * เมธอดนี้เป็น Workflow กลางที่ถูกเรียกโดย Action Handlers ต่างๆ 
     * โดยมีลำดับการทำงานดังนี้:
     * 1. ตรวจสอบข้อมูลลูกค้า
     * 2. ตรวจสอบข้อมูลการจอง
     * 3. ตรวจสอบสถานะห้องว่างครั้งสุดท้าย
     * 4. ตรวจสอบเงื่อนไขของบริการเสริม
     * 5. คำนวณราคารวม
     * 6. แสดงหน้าต่างขอคำยืนยันจากผู้ใช้
     * 7. หากผู้ใช้ยืนยัน จะทำการบันทึกข้อมูลทั้งหมด
     */
    private void processAndSaveBooking(){

        // ดึงและตรวจสอบข้อมูลลูกค้า
        Customer newCustomer = getCustomerDatafromFome();
        if (newCustomer == null) {
            System.out.println("ข้อมูล Customer ไม่ครบ ไม่สามารถบันทึกได้");
            return ; // หยุดทำงาน
        }

        // ดึงและตรวจสอบข้อมูลการจอง
        Bookings newBookings = getBookingDatafromFome(newCustomer);
        if (newBookings == null) {
            System.out.println("ข้อมูล Booking ไม่ครบ ไม่สามารถบันทึกได้");
            return; // หยุดทำงาน
        }

        // ตรวจสอบความว่างของห้องครั้งสุดท้าย
        if (!isRoomAvailable(newBookings.getRoom(), newBookings.getDateCheckin(), newBookings.getTimeCheckin(), newBookings.getDateCheckout(), newBookings.getTimeCheckout())) {
            new Alert(Alert.AlertType.ERROR, "ขอโทษค่ะ ห้องหมายเลข : " + newBookings.getRoom().getNumberRoom() + " ไม่สามารถจองได้อีกต่อไปในช่วงเวลาที่เลือก กรุณาเลือกอีกครั้ง").showAndWait();
            updateAvailableRoom(); // รีเฟรชรายการห้องว่างให้ผู้ใช้เห็น
            return ; // หยุดทำงาน
        }

        // ตรวจสอบความถูกต้องของข้อมูลบริการเสริม
        // ตรวจสอบข้อมูลบริการอาหาร
        if (mealCheckBox.isSelected()) {
            int mealDay = 0;   
            try {
                    mealDay = Integer.parseInt(dayMealTextfield.getText());
                    if (mealDay <=0) throw new NumberFormatException();
                
                
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "กรุณากรอกจำนวนวันสำหรับบริการอาหารเป็นตัวเลขที่มากกว่า 0");
                alert.showAndWait();
                return; // หยุดทำงาน
            }

            long stayDays = getNumberOfStayDay();
            if (mealDay > stayDays) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "จำนวนวันสำหรับบริการอาหาร (" + mealDay + " วัน) ต้องไม่เกินจำนวนวันที่เข้าพัก (" + stayDays + " วัน)");
                alert.showAndWait();
                return; // หยุดทำงาน
            }

        }
        
        // คำนวณราคาสุทธิ
        double totalCostAfterDiscount = calculateTotalCost();

        // แสดงหน้าต่างเพื่อขอคำยืนยันจากผู้ใช้
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("ยืนยันการจอง");
        confirmationDialog.setHeaderText("กรุณาตรวจสอบรายละเอียดการจองก่อนบันทึก");
        confirmationDialog.setContentText("ลูกค้า: " + newCustomer.getFullName() + "\n" +
                                     "ห้อง: " + newBookings.getRoom().getNumberRoom() + "\n\n" +
                                     "ยอดรวม: " + String.format("%.2f",totalCostAfterDiscount)+ " บาท\n\n" +
                                     "ยืนยันเพื่อบันทึกข้อมูล");

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        // บันทึกเมื่อผู้ใช้กดยืนยัน "OK" 
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // สร้าง AmountPaid object
            AmountPaid amountObj = new AmountPaid(newBookings, totalCostAfterDiscount);
            // บันทึกข้อมูลทั้งหมด
            saveBookingData(newCustomer, newBookings, amountObj);

            // แจ้งผู้ใช้ว่าการบันทึกสำเร็จ
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "บันทึกข้อมูลการจองเรียบร้อยแล้ว");
            successAlert.showAndWait();
        } 
    }

    /**
     * Action-Handler สำหรับปุ่ม "Proceed"
     * ทำหน้าที่ตรวจสอบและบันทึกการจอง
     */
    @FXML private void  handleProceedButtonAction(){
        processAndSaveBooking();
    }

    /**
     * Action-Handler สำหรับปุ่ม "Proceed" 
     * ทำหน้าที่ตรวจสอบและบันทึกการจอง
     * @param event ActionEvent จากการคลิกปุ่ม
     */
    @FXML private void saveBooking(ActionEvent event) {
        processAndSaveBooking();
    }

    /**
     * method สำหรับบันทึกข้อมูลทั้งหมดลงในระบบและอัปเดต UI
     * ลำดับการทำงาน:
     *  1. เพิ่มข้อมูลใหม่ลงใน Repositories ที่อยู่ในหน่วยความจำ
     *  2. สั่งให้ Repositories บันทึกข้อมูลทั้งหมดลงไฟล์ CSV
     *  3. อัปเดต UI (ตาราง, ล้างฟอร์ม, รีเฟรชรายการห้องว่าง)
     * @param customer ออบเจกต์ Customer ที่จะบันทึก
     * @param booking  ออบเจกต์ Bookings ที่จะบันทึก
     * @param amount   ออบเจกต์ AmountPaid ที่เกี่ยวข้องกับการจอง
     */
    private void saveBookingData(Customer customer, Bookings booking,AmountPaid amount) {
        customerRepository.addCustomer(booking.getRoom().getNumberRoom(), customer); // บันทึกลูกค้า
        bookingRepository.addBooking(booking); // บันทึกการจอง
        amountPaidRepository.addAmount(amount); // บันทึกการชำระเงิน

        // บันทึกข้อมูลลงไฟล์ CSV 
        customerRepository.saveCustomerToCSV();
        bookingRepository.saveBookingToCSV();
        amountPaidRepository.saveAmountPaidToCSV();

        addBookingToTable(new HomeTableView(booking, amount)); // เพิ่มข้อมูลเงินในคอลลัม
        clearForm(); // ล้างฟอร์มทั้งหมด
        updateAvailableRoom(); // รีเฟรชรายการห้องว่าง
    }


    /**
     * สร้าง DepositRoom object  สร้างค่ามัดจำพื้นฐาน (ใช้ Factory Pattern) ห่อด้วยบริการเสริมที่ผู้ใช้เลือก (ใช้ Decorator Pattern)
     * @param booking ออบเจกต์การจอง ซึ่งใช้สำหรับดึงข้อมูลห้องพัก
     * @return DepositRoom object ที่ผ่านการห่อด้วยบริการเสริมแล้ว
     */
    private DepositRoom builDepositRoom(Bookings booking){
        // Factory Pattern สร้างโรงงาน
        // ใช้ Factory เพื่อสร้าง DepositRoom object ตามประเภทของห้อง
        DepositFactory factory = new DepositFactory();
        DepositRoom deposit= factory.createSimpDepositRoom(booking.getRoom());
        
        // Decorator Pattern: "ห่อหุ้ม" ด้วยบริการเสริมต่างๆ 
        // ตรวจสอบ CheckBox ทีละอัน แล้วนำ Decorator มาห่อทับ `deposit` object ไปเรื่อยๆ

        if (cleaningRoomCheckBox.isSelected()) {
            // ห่อด้วยบริการทำความสะอาด
            deposit = new CleaningRoomDecorator(deposit);
        }

        if (mealCheckBox.isSelected()) {
            // ห่อด้วยบริการอาหาร
            int mealDay = 0;
            try {
                // อ่านจำนวนวันจาก TextField + ป้องกัน Error
                if (dayMealTextfield.getText() != null && !dayMealTextfield.getText().isEmpty()) {
                    mealDay = Integer.parseInt(dayMealTextfield.getText());
                }
            } catch (NumberFormatException e) {
                mealDay = 0; // ถ้ากรอกผิด ให้เป็น 0
            }
            deposit = new MealDecorator(deposit, mealDay);
        }

        if (pickupServiceCheckbox.isSelected()) {
            // ห่อด้วยบริการรถรับส่ง
            deposit = new PickupServiceDecorator(deposit);
        }
        // คืนค่าออบเจกต์ที่ผ่านการห่อ
        return deposit;
    }

    /**
     * คำนวณจำนวนคืนที่เข้าพักจากวันที่ที่เลือกในฟอร์มการจอง
     * @return จำนวนคืนที่เข้าพัก คืนค่า 0 หากยังเลือกข้อมูลไม่ครบหรือไม่ถูกต้อง (เช่น วันที่เช็คเอาท์อยู่ก่อนวันเช็คอิน)
     */
    private long getNumberOfStayDay(){
        // ดึงค่าจาก DatePicker ของฟอร์มการจอง
        LocalDate checkin = checkinDatePicker1.getValue();
        LocalDate checkout = checkoutDatePicker1.getValue();

        // ตรวจสอบว่าข้อมูลวันที่ถูกต้องหรือไม่
        if (checkin != null && checkout != null && !checkout.isBefore(checkin)) {
            // ถ้าถูกต้อง ให้คำนวณหาระยะห่างระหว่างวัน (จำนวนคืน)
            return ChronoUnit.DAYS.between(checkin, checkout); //long เพราะ between
            
        }
        //ในอนาคตอาจจะมี popup แจ้งเตือน
        return 0; // ถ้าข้อมูลไม่ถูกต้อง ให้คืนค่า 0
    }

     /**
     * คำนวณราคารวมทั้งหมดแบบสด โดยรวมราคาส่วนลดและบริการเสริม
     * เมธอดนี้เป็นศูนย์กลางการคำนวณที่นำ Design Pattern ต่าง ๆ มาทำงานร่วมกัน:
     * 1. Strategy Pattern เพื่อคำนวณส่วนลดของราคาห้องพัก
     * 2. Factory Pattern: เพื่อสร้างค่ามัดจำพื้นฐานตามประเภทห้อง
     * 3. Decorator Pattern เพื่อเพิ่มค่าบริการเสริมเข้าไปในค่ามัดจำ
     * @return ราคารวมทั้งหมด (ราคาสุทธิห้องพัก + ค่าบริการเสริม)
     */
    private double calculateTotalCost(){
        // รวบรวมข้อมูลพื้นฐาน
        String roomNo = roomNoComboBox.getValue();
        if (roomNo == null || roomNo.isEmpty()) {
            return 0.0; // ถ้ายังไม่เลือกห้อง ให้ราคารวมเป็น 0
        }

        Room selectedRoom = roomRepository.getRoom(roomNo);
        long nights = getNumberOfStayDay();
        if (nights == 0) {
            nights = 1; // คำนวณอย่างน้อย 1 คืนเสมอ
        }

        LocalDate checkinDate;
        LocalDate checkoutDate;

        if (checkinDatePicker1.getValue() != null) {
            checkinDate = checkinDatePicker1.getValue();
        }else{
            checkinDate = LocalDate.now();
        }

        if (checkinDatePicker1.getValue() != null) {
            checkoutDate = checkinDatePicker1.getValue().plusDays(nights);
        }else{
            checkoutDate = LocalDate.now().plusDays(nights);
        }

        Bookings booking = new Bookings(selectedRoom, null, "", checkinDate, LocalTime.MIDNIGHT, checkoutDate, LocalTime.MIDNIGHT, LocalDate.now(), LocalTime.now());


        // คำนวณราคาห้องพักสุทธิ (ใช้ Strategy Pattern)
        DiscountStrategy discountStrategy = DiscountSelector.getStrategy(selectedRoom, booking);
        HotelCalculator calculator = new HotelCalculator();
        double finalPrice = calculator.calculateFinalPrice(selectedRoom, booking, discountStrategy);
        
        // คำนวณค่ามัดจำและบริการเสริม (ใช้ Factory + Decorator Pattern)
        // สร้างค่ามัดจำพื้นฐานจาก Factory
        DepositFactory factory = new DepositFactory();
        DepositRoom deposit = factory.createSimpDepositRoom(selectedRoom);    

        // ห่อด้วยบริการเสริมที่ถูกเลือก
        if (cleaningRoomCheckBox.isSelected()) {
            deposit = new CleaningRoomDecorator(deposit);
        }
        if (mealCheckBox.isSelected()) {
            int mealDays = 0;
            try {
                mealDays = Integer.parseInt(dayMealTextfield.getText());
            } catch (NumberFormatException e) {
                mealDays = 0;
            }
            deposit = new MealDecorator(deposit, mealDays);
        }
        if (pickupServiceCheckbox.isSelected()) {
            deposit = new PickupServiceDecorator(deposit);
        }  
    
        // คืนค่ายอดรวมสุดท้าย
        return finalPrice + deposit.getCost();
    } 
    
    /**
     * อัปเดต Label ที่แสดงราคารวมและราคาย่อยของบริการเสริมแบบ Real-time
     */
    private void updateTotalLabel(){
        // เรียกใช้เมธอดหลักเพื่อคำนวณราคารวมทั้งหมด
        double totalCost = calculateTotalCost();

        // อัปเดต Label ที่แสดงยอดรวมสุดท้าย
        totalLabel.setText("฿ " + String.format("%.2f", totalCost));

        // Logic เฉพาะสำหรับการแสดงผลราคาย่อยของ meal CheckBox
        if (mealCheckBox.isSelected()) {
            int mealDay = 0;
            try {
                // แปลงค่าใน TextField เป็นตัวเลข
                mealDay = Integer.parseInt(dayMealTextfield.getText());

            } catch (NumberFormatException e) {
                // ถ้าแปลงไม่ได้ (TextField ว่าง หรือไม่ใช่ตัวเลข) ให้ mealDay เป็น 0
                mealDay = 0;
            }

            if (mealDay > 0) {

                // ถ้ามีจำนวนวันที่ถูกต้อง ให้คำนวณและแสดงราคาของบริการ meal CheckBox
                dayMealLabel.setText("฿" + String.format("%.2f", 500.0*mealDay));
                dayMealLabel.setVisible(true);
            }else {
                // ถ้าจำนวนวันเป็น 0 หรือไม่ถูกต้อง ให้แสดง Label แต่ไม่เปลี่ยนข้อความ
                dayMealLabel.setVisible(true);
            }
        }else{
            // ถ้าไม่ได้เลือก meal CheckBox ให้รีเซ็ต Label กลับเป็นค่าเริ่มต้น
            dayMealLabel.setText("฿500/day" );
            dayMealLabel.setVisible(true);
        }


    }

    /**
     * จัดการ Logic ที่เกิดขึ้นเมื่อผู้ใช้เลือกห้องพัก
     * 1. แสดงรายละเอียดของห้องที่เลือก
     * 2. คัดลอกข้อมูล วัน-เวลา จากฟอร์มค้นหาไปยังฟอร์มการจอง
     * @param roomNumber หมายเลขห้องที่ผู้ใช้เลือก
     */
    private void getDataFormRoomSelected(String roomNumber){
        // ถ้าไม่มีการเลือกห้อง ให้ล้างรายละเอียดแล้วจบการทำงาน
        if (roomNumber == null || roomNumber.isEmpty()) {
            clearSelectedRoomDetails();
            return ;
        }

        // 1. แสดงรายละเอียดห้องที่เลือก
        Room selectedRoom = roomRepository.getRoom(roomNumber);
        if (selectedRoom != null) {
            selectedRoomTypeLabel.setText(selectedRoom.getType());
            selectedRoomPeopleLabel.setText(String.valueOf(selectedRoom.getPeople()) + " people");
            selectedRoomPriceLabel.setText("฿ " + String.format("%.2f", selectedRoom.getPrice()));

            if (selectedRoom.getProperties() != null && !selectedRoom.getProperties().isEmpty()) {
                selectedRoomPropertiesLabel.setText(String.join(", ", selectedRoom.getProperties()));
            }else{
                selectedRoomPropertiesLabel.setText("-");
            }

        }

         // 2. คัดลอก วัน-เวลา จากฟอร์ม Filter ไปยังฟอร์มกรอกข้อมูลการจองของลูกค้า
        LocalDate checkinDate = checkinDatePicker.getValue();
        LocalDate checkoutDate = checkoutDatePicker.getValue();
        String checkinTime = checkinTimeCombobox.getValue();
        String checkoutTime = checkoutTimeCombobox.getValue();

        if (checkinDate != null) {
            checkinDatePicker1.setValue(checkinDate);
        }

        if (checkinTime != null) {
            checkinTimeCombobox1.setValue(checkinTime);
        }

        if (checkoutDate != null) {
            checkoutDatePicker1.setValue(checkoutDate);
        }

        if (checkoutTime != null) {
            checkoutTimeCombobox1.setValue(checkoutTime);
        }
        
        
    }

    /**
     * method สำหรับรีเซ็ต Label ใน Panel แสดงรายละเอียดห้องที่เลือก
     */
    private void clearSelectedRoomDetails(){
        selectedRoomTypeLabel.setText("-");
        selectedRoomPeopleLabel.setText("-");
        selectedRoomPriceLabel.setText("-");
        selectedRoomPropertiesLabel.setText("-");
    }

    /**
     * method สำหรับล้างข้อมูลในฟอร์มทั้งหมดบนหน้าจอ
     * ถูกเรียกใช้หลังจากทำการจองสำเร็จ เพื่อเตรียมพร้อมสำหรับการจองครั้งใหม่
     */
    private void clearForm(){
        // เคลียร์ฟอร์มค้นหา
        roomNoComboBox.getSelectionModel().clearSelection();
        roomTypeComboBox.getSelectionModel().clearSelection();
        numberPeopleComboBox.getSelectionModel().clearSelection();
        checkinDatePicker.setValue(null);
        checkoutDatePicker.setValue(null);
        checkinTimeCombobox.getSelectionModel().clearSelection();
        checkoutTimeCombobox.getSelectionModel().clearSelection();
        minRangeField.clear();
        maxRangeField.clear();

        // เคลียร์ฟอร์มลูกค้า
        idCardField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        genderToggleGroup.selectToggle(null);
        addressField.clear();
        cityField.clear();
        countryCombobox.getSelectionModel().clearSelection(); 
        
         // เคลียร์ฟอร์มการจอง 
        checkinDatePicker1.setValue(null);
        checkinTimeCombobox1.getSelectionModel().clearSelection();
        checkoutDatePicker1.setValue(null);
        checkoutTimeCombobox1.getSelectionModel().clearSelection();

        //  เคลียร์ Checkbox ที่มีทั้งหมด
        clearSelectedRoomDetails();
        for(CheckBox cb : allCheckbox){
            cb.setSelected(false);
        }
    }

    /**
     * Action-Handler สำหรับปุ่ม "Clear"
     * ทำหน้าที่ล้างค่าในฟอร์มค้นหาทั้งหมด แล้วสั่งให้ค้นหาห้องว่างใหม่อีกครั้ง
     */
    @FXML
    public void handleclearFilterButton(){
        roomTypeComboBox.getSelectionModel().clearSelection();
        numberPeopleComboBox.getSelectionModel().clearSelection();
        checkinDatePicker.setValue(null);
        checkoutDatePicker.setValue(null);
        checkinTimeCombobox.getSelectionModel().clearSelection();
        checkoutTimeCombobox.getSelectionModel().clearSelection();
        minRangeField.clear();
        maxRangeField.clear();
        jacuzziCheckBox.setSelected(false);
        lakeViewCheckBox.setSelected(false);
        petFriendlyCheckBox.setSelected(false);
        privatePoolCheckBox.setSelected(false);
        tvCheckBox.setSelected(false);
        wifiCheckBox.setSelected(false);
        roomNoComboBox.getSelectionModel().clearSelection();

        clearSelectedRoomDetails();

        // เมื่อล้าง Filter แล้ว ให้ทำการค้นหาใหม่ทั้งหมด
        updateAvailableRoom();
    }

    /**
     * Action-Handler สำหรับปุ่ม "Print Summary and Save"
     * เมธอดนี้จะจัดการกระบวนการทั้งหมดตั้งแต่การรวบรวมข้อมูล, การคำนวณราคา,
     * การขอคำยืนยัน, การบันทึกข้อมูล, และสุดท้ายคือการสั่งการให้ระบบพิมพ์ใบเสร็จ
     * ผ่าน Observer Pattern
     */
    @FXML private void  handlePrintBillButtonAction(){
        // รวบรวมและตรวจสอบข้อมูลจากฟอร์ม
        Customer newCustomer = getCustomerDatafromFome();
        if (newCustomer == null) { return; } // หยุดทำงานถ้าข้อมูลลูกค้าไม่ครบ

        Bookings newBookings = getBookingDatafromFome(newCustomer);
        if (newBookings == null) {
            return; // หยุดทำงานถ้าข้อมูลลูกค้าไม่ครบ
        }

        // คำนวณราคาสุทธิโดยใช้ Design Pattern ต่าง ๆ
        Room roomForBill = newBookings.getRoom();

        // Factory + Decorator Pattern 
        // สร้างออบเจกต์ Deposit ที่ห่อด้วยบริการเสริมทั้งหมดแล้ว
        DepositRoom depositRoom = builDepositRoom(newBookings);

        // Strategy Pattern
        // เลือกกลยุทธ์ส่วนลดและคำนวณราคาห้องพักสุทธิ
        DiscountStrategy discountStrategy = DiscountSelector.getStrategy(roomForBill, newBookings);
        HotelCalculator calculator = new HotelCalculator();
        double finalPriceRoom = calculator.calculateFinalPrice(roomForBill, newBookings, discountStrategy);
        
        // คำนวณยอดรวมสุดท้าย
        double totalCostAfterDiscount = finalPriceRoom + depositRoom.getCost();  

        // แสดงหน้าต่างเพื่อขอคำยืนยันจากผู้ใช้
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("ยืนยันการจองและต้องการปรินต์ใบเสร็จ");
        confirmationDialog.setHeaderText("กรุณาตรวจสอบรายละเอียดการจองก่อนบันทึก");
        confirmationDialog.setContentText("ลูกค้า: " + newCustomer.getFullName() + "\n" +
                                     "ห้อง: " + newBookings.getRoom().getNumberRoom() + "\n\n" +
                                     "ยอดรวม: " + String.format("%.2f",totalCostAfterDiscount)+ " บาท\n\n" +
                                     "ยืนยันเพื่อบันทึกข้อมูลและพิมพ์ใบเสร็จ?");

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        // ดำเนินการหลังผู้ใช้กดยืนยัน "OK"
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // บันทึกข้อมูลทั้งหมดลงในระบบ (Repositories และ CSV)
            AmountPaid amountObj = new AmountPaid(newBookings, totalCostAfterDiscount);
            saveBookingData(newCustomer, newBookings, amountObj);

            // สั่งการพิมพ์ใบเสร็จผ่าน Observer Pattern
            // สร้าง Event Object ที่บรรจุข้อมูลทั้งหมดที่จำเป็น
            System.out.println("Printing bill...");
            BillEvent event = new BillEvent(newBookings.getRoom(), newBookings, depositRoom, LocalDateTime.now());
            
            // ส่ง Event ออกไป ให้ HotelEventManager แจ้งเตือน Observer ทั้งหมด
            eventManager.notifyObserver(event);

        } else {

                // กรณีผู้ใช้กด Cancel
                System.out.println("User cancelled the action.");
        }
    }

}

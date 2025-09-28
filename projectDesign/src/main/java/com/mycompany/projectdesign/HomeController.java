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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javafx.util.Callback; 

import com.mycompany.projectdesign.Project.DecoratorPattern.CleaningRoomDecorator;
import com.mycompany.projectdesign.Project.DecoratorPattern.MealDecorator;
import com.mycompany.projectdesign.Project.DecoratorPattern.PickupServiceDecorator;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.DepositFactory;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.DepositRoom;
import com.mycompany.projectdesign.Project.Model.AmountPaid;
import com.mycompany.projectdesign.Project.Model.AmountPaidRepository;
import com.mycompany.projectdesign.Project.Model.BookingRepository;
import com.mycompany.projectdesign.Project.Model.BookingStatus;
import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.Model.Customer;
import com.mycompany.projectdesign.Project.Model.CustomerRepository;
import com.mycompany.projectdesign.Project.Model.Room;
import com.mycompany.projectdesign.Project.Model.RoomRepository;
import com.mycompany.projectdesign.Project.Model.RoomStatus;
import com.mycompany.projectdesign.Project.ObserverPattern.BillEvent;
import com.mycompany.projectdesign.Project.ObserverPattern.BillObserver;
import com.mycompany.projectdesign.Project.ObserverPattern.HotelEventManager;
import com.mycompany.projectdesign.Project.Service.BookingScheduler;
import com.mycompany.projectdesign.Project.Service.RoomService;
import com.mycompany.projectdesign.Project.StrategyPattern.DiscountSelector;
import com.mycompany.projectdesign.Project.StrategyPattern.DiscountStrategy;
import com.mycompany.projectdesign.Project.StrategyPattern.HotelCalculator;

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
import javafx.scene.control.Alert.AlertType;
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
    private List<Bookings> bookings = new ArrayList<>();
    private RoomRepository roomRepository = RoomRepository.getInstance();
    private CustomerRepository customerRepository = CustomerRepository.getInstance();
    private BookingRepository bookingRepository = BookingRepository.getInstance();
    private AmountPaidRepository amountPaidRepository = AmountPaidRepository.getInstance();
    private List<CheckBox> allCheckbox;
    private ToggleGroup genderToggleGroup;
    private HotelEventManager eventManager = HotelEventManager.getInstance();
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ObservableList<HomeTableView> homeBookingList = FXCollections.observableArrayList();
    private final RoomService roomService = new RoomService();

    //แก้บัค
    private boolean isUpdatingFromSelection = false;
    private BookingScheduler bookingScheduler;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {


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

        ObservableList<String> roomType = FXCollections.observableArrayList(
   "Single room",
            "Double room",
            "Twin room",
            "suite"
        );

        ObservableList<Integer> people = FXCollections.observableArrayList(
            1,2,3,4
        );

        genderToggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);
        otherRadioButton.setToggleGroup(genderToggleGroup);

        allCheckbox = Arrays.asList(jacuzziCheckBox, lakeViewCheckBox,petFriendlyCheckBox,privatePoolCheckBox,tvCheckBox,wifiCheckBox,cleaningRoomCheckBox,mealCheckBox, pickupServiceCheckbox);

        bookingIDColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("bookingID"));
        newGuestColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("fullnameCustomer"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("numberRoom"));
        checkinColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("checkin"));
        checkoutColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("checkout"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("status"));
        amountPaidColumn.setCellValueFactory(new PropertyValueFactory<HomeTableView,String>("amount"));
        
        for (Bookings booking : bookingRepository.getAllBookings()) {
            AmountPaid paid = amountPaidRepository.getAmountByBookingID(booking.getBookingID());
            if (paid != null) {
                homeBookingList.add(new HomeTableView(booking, paid));
            } else {
                // ถ้า booking นี้ยังไม่มีการจ่ายแสดง 0
                homeBookingList.add(new HomeTableView(booking, new AmountPaid(booking, 0.0)));
            }
        }

        bookingTable.setItems(homeBookingList);
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
            if (newVal != null) {
                getDataFormRoomSelected(newVal);
            }
        });

        addPriceUpdateListeners();
        setupDatePickers();

        
        bookingScheduler = new BookingScheduler();
        bookingScheduler.start();
    }
    
    private void addBookingToTable(HomeTableView newEntry){
    boolean exists = homeBookingList.stream()
        .anyMatch(h -> h.getBookingID().equals(newEntry.getBookingID()));
    if (!exists) {
        homeBookingList.add(newEntry);
        }
    }


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

    private void updateAvailableRoom(){
        
        if (isUpdatingFromSelection) {
            return;
        }

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
            //รอทำ pop up
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
                if (realTimeStatus == RoomStatus.MAINTENANCE || realTimeStatus == RoomStatus.OCCUPIED) {
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
        boolean FilterSelected = selectedRoomType != null || selectedNumberPeople !=null || minPrice != null 
                                || maxPrice != null || !selectedProperties.isEmpty() || (checkinDate != null && checkoutDate != null && checkinTime != null && checkoutTime != null) ;

        if (!FilterSelected) {
            availableRooms = new ArrayList<>(allRooms);
        }

        roomNoComboBox.setItems(FXCollections.observableArrayList(
            availableRooms.stream().map(Room::getNumberRoom).toList()
        ));
       

        
        
    }

    private boolean isRoomAvailable(Room room, LocalDate checkinDate, LocalTime checkinTime, LocalDate checkoutDate, LocalTime checkoutTime){
        
        //ต่อวันกับเวลาเข้ากัน
        LocalDateTime checkin = checkinDate.atTime(checkinTime);
        LocalDateTime checkout = checkoutDate.atTime(checkoutTime);
        for(Bookings booking : bookingRepository.getAllBookings()){
            
            if (booking.getRoom().getNumberRoom().equals(room.getNumberRoom())) {
                if (booking.getStatus() == BookingStatus.CONFIRMED || booking.getStatus() == BookingStatus.CHECKED_IN) {
                    LocalDateTime existingStart = booking.getDateCheckin().atTime(booking.getTimeCheckin());
                    LocalDateTime existingEnd = booking.getDateCheckout().atTime(booking.getTimeCheckout());
                
                    // เงื่อนไขการทับซ้อน: (StartA < EndB) and (EndA > StartB)
                    if (checkin.isBefore(existingEnd) && checkout.isAfter(existingStart)) {
                    return false; //ทับกัน
                    }                    
                }

            }
        }
        return true; //ว่าง ไม่มีเวลาทับซ้อน
    }

    //ให้ปฎิทินเลือกวันในอดีตไม่ได้
    private void setupDatePickers(){
        checkinDatePicker1.setDayCellFactory(picker -> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
            
        });

        checkinDatePicker1.valueProperty().addListener((obs, oldDate, newDate) -> {
            final Callback<DatePicker, DateCell> dayCellFactory = picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (newDate != null) {
                        setDisable(empty || !date.isAfter(newDate));
                    }
                }
            };
            checkoutDatePicker1.setDayCellFactory(dayCellFactory);
        });
    }

    private void addFiterListeners(){

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

        //เวลาคีย์พวกนี้จำนวนเงินก็จะขึ้นทันที
    private void addPriceUpdateListeners(){
        cleaningRoomCheckBox.selectedProperty().addListener((obs, o, n) -> updateTotalLabel());
        pickupServiceCheckbox.selectedProperty().addListener((obs, o, n) -> updateTotalLabel());
        roomNoComboBox.valueProperty().addListener((obs, o, n) -> updateTotalLabel());
        checkinDatePicker1.valueProperty().addListener((obs, o, n) -> updateTotalLabel());
        checkoutDatePicker1.valueProperty().addListener((obs, o, n) -> updateTotalLabel());
        mealCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            dayMealTextfield.setDisable(!newVal);
            if(!newVal){
                dayMealTextfield.clear();
            }
            updateTotalLabel();
        });
        dayMealTextfield.textProperty().addListener((obs, oldVal, newVal) -> updateTotalLabel());
    }

    public Customer getCustomerDatafromFome(){
        
        if (idCardField.getText().isEmpty() || firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
            emailField.getText().isEmpty() || phoneField.getText().isEmpty() || genderToggleGroup.getSelectedToggle() == null ||addressField.getText().isEmpty() ||
            cityField.getText().isEmpty() || countryCombobox.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "โปรดกรอกข้อมูลให้ครบถ้วน");
                alert.showAndWait();
                return null ;

        } else{

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

        return new Customer(idCard, firstName, lastName, email, phone, gender, country , city, address);
        }
    }



    public Bookings getBookingDatafromFome(Customer customer){
        String roomNo = roomNoComboBox.getValue();
        if (roomNo == null || roomNo.isEmpty()) {
            //รอทำ pop up
            return null;
        }

        Room room = roomRepository.getRoom(roomNo);

        if (checkinDatePicker1.getValue() == null || checkoutDatePicker1.getValue() == null ||
            checkinTimeCombobox1.getValue() == null || checkoutTimeCombobox1.getValue() == null) {
            
                System.out.println("เขียนข้อมูลไม่ครบ"); //อาจจะเพิ่ม pop up แจ้ง 
                return null ;

        } else{

        String bookingID = bookingRepository.generateNextBookingId();
        LocalDate checkinDate = checkinDatePicker1.getValue();
        LocalDate checkoutDate = checkoutDatePicker1.getValue();
        LocalTime checkinTime = LocalTime.parse(checkinTimeCombobox1.getValue());
        LocalTime checkoutTime = LocalTime.parse(checkoutTimeCombobox1.getValue());
        LocalDate bookingDate = LocalDate.now();
        LocalTime bookingTime = LocalTime.now();

        return new Bookings(room, customer, bookingID, checkinDate, checkinTime, checkoutDate, checkoutTime, bookingDate, bookingTime);
        }
    }


    private void processAndSaveBooking(){
        Customer newCustomer = getCustomerDatafromFome();
        if (newCustomer == null) {
            //รอทำ pop up แจ้ง
            return ;
        }

        Bookings newBookings = getBookingDatafromFome(newCustomer);
        if (newBookings == null) {
            System.out.println("ข้อมูล Booking ไม่ครบ ไม่สามารถบันทึกได้");
            return;
        }

        if (!isRoomAvailable(newBookings.getRoom(), newBookings.getDateCheckin(), newBookings.getTimeCheckin(), newBookings.getDateCheckout(), newBookings.getTimeCheckout())) {
            new Alert(Alert.AlertType.ERROR, "ขอโทษค่ะ ห้องหมายเลข : " + newBookings.getRoom().getNumberRoom() + " ไม่สามารถจองได้อีกต่อไปในช่วงเวลาที่เลือก กรุณาเลือกอีกครั้ง").showAndWait();
            updateAvailableRoom();
            return ;
        }

        if (mealCheckBox.isSelected()) {
            int mealDay = 0;   
            try {
                    mealDay = Integer.parseInt(dayMealTextfield.getText());
                    if (mealDay <=0) throw new NumberFormatException();
                
                
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "กรุณากรอกจำนวนวันสำหรับบริการอาหารเป็นตัวเลขที่มากกว่า 0");
                alert.showAndWait();
                return;
            }

            long stayDays = getNumberOfStayDay();
            if (mealDay > stayDays) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "จำนวนวันสำหรับบริการอาหาร (" + mealDay + " วัน) ต้องไม่เกินจำนวนวันที่เข้าพัก (" + stayDays + " วัน)");
                alert.showAndWait();
                return;
            }

        }
        

        double totalCostAfterDiscount = calculateTotalCost();

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("ยืนยันการจอง");
        confirmationDialog.setHeaderText("กรุณาตรวจสอบรายละเอียดการจองก่อนบันทึก");
        confirmationDialog.setContentText("ลูกค้า: " + newCustomer.getFullName() + "\n" +
                                     "ห้อง: " + newBookings.getRoom().getNumberRoom() + "\n\n" +
                                     "ยอดรวม: " + totalCostAfterDiscount+ " บาท\n\n" +
                                     "ยืนยันเพื่อบันทึกข้อมูล");

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // บันทึกข้อมูลทั้งหมด
            AmountPaid amountObj = new AmountPaid(newBookings, totalCostAfterDiscount);
            saveBookingData(newCustomer, newBookings, amountObj);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "บันทึกข้อมูลการจองเรียบร้อยแล้ว");
            successAlert.showAndWait();
        } 
    }


    @FXML private void  handleProceedButtonAction(){
        processAndSaveBooking();
    }

    @FXML private void saveBooking(ActionEvent event) {
        processAndSaveBooking();
    }

    private void saveBookingData(Customer customer, Bookings booking,AmountPaid amount) {
        customerRepository.addCustomer(booking.getRoom().getNumberRoom(), customer);
        bookingRepository.addBooking(booking);
        amountPaidRepository.addAmount(amount);

        customerRepository.saveCustomerToCSV();
        bookingRepository.saveBookingToCSV();
        amountPaidRepository.saveAmountPaidToCSV();

        addBookingToTable(new HomeTableView(booking, amount));
        clearForm();
        updateAvailableRoom();
    }

    @FXML private void  handlePrintBillButtonAction(){
        Customer newCustomer = getCustomerDatafromFome();
        if (newCustomer == null) { return; }

        Bookings newBookings = getBookingDatafromFome(newCustomer);
        if (newBookings == null) {
            return;
        }

        Room roomForBill = newBookings.getRoom();
        DepositRoom depositRoom = builDepositRoom(newBookings);
        DiscountStrategy discountStrategy = DiscountSelector.getStrategy(roomForBill, newBookings);
        HotelCalculator calculator = new HotelCalculator();
        double finalPriceRoom = calculator.calculateFinalPrice(roomForBill, newBookings, discountStrategy);
        double totalCostAfterDiscount = finalPriceRoom + depositRoom.getCost();  

        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("ยืนยันการจองและต้องการปรินต์ใบเสร็จ");
        confirmationDialog.setHeaderText("กรุณาตรวจสอบรายละเอียดการจองก่อนบันทึก");
        confirmationDialog.setContentText("ลูกค้า: " + newCustomer.getFullName() + "\n" +
                                     "ห้อง: " + newBookings.getRoom().getNumberRoom() + "\n\n" +
                                     "ยอดรวม: " + totalCostAfterDiscount+ " บาท\n\n" +
                                     "ยืนยันเพื่อบันทึกข้อมูลและพิมพ์ใบเสร็จ?");

        Optional<ButtonType> result = confirmationDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // บันทึกข้อมูลทั้งหมด
            AmountPaid amountObj = new AmountPaid(newBookings, totalCostAfterDiscount);
            saveBookingData(newCustomer, newBookings, amountObj);

            System.out.println("Printing bill...");
            BillEvent event = new BillEvent(newBookings.getRoom(), newBookings, depositRoom, LocalDateTime.now());
            eventManager.notifyObserver(event);

        } else {
                System.out.println("User cancelled the action.");
        }
    }

    private DepositRoom builDepositRoom(Bookings booking){
        DepositFactory factory = new DepositFactory();
        DepositRoom deposit= factory.createSimpDepositRoom(booking.getRoom());
        
        if (cleaningRoomCheckBox.isSelected()) {
            deposit = new CleaningRoomDecorator(deposit);
        }

        if (mealCheckBox.isSelected()) {
            int mealDay = 0;
            try {
                if (dayMealTextfield.getText() != null && !dayMealTextfield.getText().isEmpty()) {
                    mealDay = Integer.parseInt(dayMealTextfield.getText());
                }
            } catch (NumberFormatException e) {
                mealDay = 0;
            }
            deposit = new MealDecorator(deposit, mealDay);
        }

        if (pickupServiceCheckbox.isSelected()) {
            deposit = new PickupServiceDecorator(deposit);
        }

        return deposit;
    }

    private long getNumberOfStayDay(){
        LocalDate checkin = checkinDatePicker1.getValue();
        LocalDate checkout = checkoutDatePicker1.getValue();
        if (checkin != null && checkout != null && !checkout.isBefore(checkin)) {
            return ChronoUnit.DAYS.between(checkin, checkout);
            
        }
        //ในอนาคตอาจจะมี popup แจ้งเตือน
        return 0;
    }

    private double calculateTotalCost(){
        String roomNo = roomNoComboBox.getValue();
        if (roomNo == null || roomNo.isEmpty()) {
            return 0.0;
        }

        Room selectedRoom = roomRepository.getRoom(roomNo);
        long nights = getNumberOfStayDay();
        if (nights == 0) {
            nights = 1;
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

        //Room roomForBill = booking.getRoom();
        //DepositRoom deposit = builDepositRoom(booking);
        DepositFactory factory = new DepositFactory();
        DepositRoom deposit = factory.createSimpDepositRoom(selectedRoom);
        DiscountStrategy discountStrategy = DiscountSelector.getStrategy(selectedRoom, booking);
        HotelCalculator calculator = new HotelCalculator();
        double finalPrice = calculator.calculateFinalPrice(selectedRoom, booking, discountStrategy);
        
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
    
        return finalPrice + deposit.getCost();
    } 
    
    private void updateTotalLabel(){
        double totalCost = calculateTotalCost();
        totalLabel.setText("฿ " + String.format("%.2f", totalCost));

        if (mealCheckBox.isSelected()) {
            int mealDay = 0;
            try {
                
                mealDay = Integer.parseInt(dayMealTextfield.getText());

            } catch (NumberFormatException e) {
                mealDay = 0;
            }

            if (mealDay > 0) {
                dayMealLabel.setText("฿" + String.format("%.2f", 500.0*mealDay));
                dayMealLabel.setVisible(true);
            }else {
                dayMealLabel.setVisible(true);
            }
        }else{
            dayMealLabel.setText("฿500/day" );
            dayMealLabel.setVisible(true);
        }


    }



    private void clearForm(){
        //เคลียร์หลัง save
        roomNoComboBox.getSelectionModel().clearSelection();
        roomTypeComboBox.getSelectionModel().clearSelection();
        numberPeopleComboBox.getSelectionModel().clearSelection();
        checkinDatePicker.setValue(null);
        checkoutDatePicker.setValue(null);
        checkinTimeCombobox.getSelectionModel().clearSelection();
        checkoutTimeCombobox.getSelectionModel().clearSelection();
        minRangeField.clear();
        maxRangeField.clear();
        idCardField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneField.clear();
        genderToggleGroup.selectToggle(null);
        addressField.clear();
        cityField.clear();
        countryCombobox.getSelectionModel().clearSelection(); 
        
        checkinDatePicker1.setValue(null);
        checkinTimeCombobox1.getSelectionModel().clearSelection();
        checkoutDatePicker1.setValue(null);
        checkoutTimeCombobox1.getSelectionModel().clearSelection();

        for(CheckBox cb : allCheckbox){
            cb.setSelected(false);
        }
    }

    //set หลังเลือกห้อง
    private void getDataFormRoomSelected(String roomNumber){
        //ดักบัค 
        isUpdatingFromSelection = true;
        try{
       
        String roomtype = roomRepository.getRoom(roomNumber).getType();
        int numPeople = roomRepository.getRoom(roomNumber).getPeople();
        List<String> properties = roomRepository.getRoom(roomNumber).getProperties();

        for(CheckBox cb : allCheckbox){
            cb.setSelected(false);
        }

        roomTypeComboBox.setValue(roomtype);
        numberPeopleComboBox.setValue(numPeople);
        if (properties != null) {
            if(properties.contains("Jacuzzi")) jacuzziCheckBox.setSelected(true);
            if(properties.contains("Lake View")) lakeViewCheckBox.setSelected(true);
            if(properties.contains("Pet-Friendly")) petFriendlyCheckBox.setSelected(true);
            if(properties.contains("Private Pool")) privatePoolCheckBox.setSelected(true);
            if(properties.contains("TV 58” 4K UHD LED")) tvCheckBox.setSelected(true);
            if(properties.contains("WIFI")) wifiCheckBox.setSelected(true);
        }

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
        }finally {
            isUpdatingFromSelection = false;
        }
        
    }

}

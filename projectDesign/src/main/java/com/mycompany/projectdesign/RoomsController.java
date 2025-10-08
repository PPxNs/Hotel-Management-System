/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectdesign;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mycompany.projectdesign.Project.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.layout.HBox;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.util.Callback; 

/**
 * Controller สำหรับหน้าจอจัดการห้องพัก (Rooms.fxml)
 * ทำหน้าที่จัดการข้อมูลของห้องพักทั้งหมด
 * รวมถึงการแสดงผล, กรองข้อมูล, และการโต้ตอบกับผู้ใช้ผ่านฟอร์มและตาราง
 */

public class RoomsController implements Initializable {

    // @FXML Components: ส่วนเกี่ยวกับคอลลัมแสดงข้อมูล
    @FXML private TableView<RoomsTableView> roomTable;

    
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private ComboBox<String> allTypeComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField searchField;
    @FXML private TextField roomNoField;
    @FXML private TextField priceField;
    @FXML private ComboBox<Integer> peopleCombobox;
    @FXML private TextField imagePartField;
    @FXML private CheckBox jacuzziCheckBox;
    @FXML private CheckBox lakeViewCheckBox;
    @FXML private CheckBox petFriendlyCheckBox;
    @FXML private CheckBox privatePoolCheckBox;
    @FXML private CheckBox tvCheckBox;
    @FXML private CheckBox wifiCheckBox;

    // @FXML Components: ส่วนเกี่ยวกับคอลลัมแสดงข้อมูล
    @FXML private TableColumn<RoomsTableView,String> roomNoColumn;
    @FXML private TableColumn<RoomsTableView,String> imageColumn;    
    @FXML private TableColumn<RoomsTableView,String> typeColumn;
    @FXML private TableColumn<RoomsTableView,String> priceColumn;
    @FXML private TableColumn<RoomsTableView,String> peopleColumn;
    @FXML private TableColumn<RoomsTableView,String> propertyColumn;
    @FXML private TableColumn<RoomsTableView,String> statusColumn;
    @FXML private TableColumn<RoomsTableView, Void> actionColumn;

    //ปุ่ม
    @FXML private Button addImgButton;
    @FXML private Button AddRoomButton;
    @FXML private Button SaveEditButton;

    private List<CheckBox> allCheckbox;
    private final List<String> seselectedProperties = new ArrayList<>();
    private RoomRepository roomRepository = RoomRepository.getInstance();
    private ObservableList<RoomsTableView> roomList = FXCollections.observableArrayList();
    private Room currentEditRoom = null ; //ตัวแปรเก็บข้อมูลเพื่อแก้ไข

    /**
     * เมธอดหลักที่ถูกเรียกโดยอัตโนมัติเมื่อ FXML โหลดเสร็จ
     * ทำหน้าที่ตั้งค่าเริ่มต้นทั้งหมดของหน้าจอจัดการห้องพัก
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        System.out.println("ขนาดของข้อมูลห้องพักที่โหลดมาคือ: " + roomRepository.getAllRooms().size());

        //อันนี้ set item ใน combobox     
        ObservableList<String> roomType = FXCollections.observableArrayList(
   "Single room",
            "Double room",
            "Twin room",
            "suite"
        );

        ObservableList<String> allType = FXCollections.observableArrayList(
   "All Types",
            "Single room",
            "Double room",
            "Twin room",
            "suite"
        );

        ObservableList<String> allStatus = FXCollections.observableArrayList(
       "All Status",
                "AVAILABLE",     
                "OCCUPIED",      
                "CLEANING",        
                "MAINTENANCE"      
        );

         ObservableList<Integer> numberPeople = FXCollections.observableArrayList(
   1,
            2,
            3,
            4,
            5
        );

        //อันนี้เซตค่าใน combobox 
        roomTypeComboBox.setItems(roomType);
        allTypeComboBox.setItems(allType);
        allTypeComboBox.setValue("All Types");
        statusComboBox.setItems(allStatus);
        statusComboBox.setValue("All status");
        peopleCombobox.setItems(numberPeople);


        allCheckbox = Arrays.asList(jacuzziCheckBox, lakeViewCheckBox,petFriendlyCheckBox,privatePoolCheckBox,tvCheckBox,wifiCheckBox);

        // ตั้งค่าคอลัมน์ของ TableView
        // ผูกคอลัมน์พื้นฐานกับ Properties ใน RoomsTableView
        roomNoColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("numberRoom"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("image"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("roomType"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("price"));
        peopleColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("people"));
        propertyColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("property"));

        // ตั้งค่าคอลัมน์ Status ให้แก้ไขได้ด้วย ComboBox
        ObservableList<String> statusObtions = FXCollections.observableArrayList();
        for(RoomStatus status : RoomStatus.values()){
            statusObtions.add(status.name());
        }

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(statusObtions));
        
    

        // กำหนด Logic ที่จะทำงานหลังจากแก้ไขสถานะเสร็จ
        statusColumn.setOnEditCommit(event -> {
            RoomsTableView roomView = event.getRowValue();
            Room roomUpdate = roomView.getRoom();
            RoomStatus newStatus = RoomStatus.valueOf(event.getNewValue());
            
            // ตรวจสอบเงื่อนไขพิเศษ: ป้องกันการเปลี่ยนสถานะห้องที่มีคนพักอยู่ให้เป็น "ว่าง"
            if (newStatus == RoomStatus.AVAILABLE && isRoomCurrentlyOccupied(roomUpdate)) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("ยืนยันการจอง");
                confirmationAlert.setHeaderText("คำเตือน : อาจมีการจองซ้ำ");
                confirmationAlert.setContentText("ห้องนี้ปัจจุบันมีผู้เข้าพักแล้ว การเปลี่ยนสถานะเป็นว่างอาจทำให้เกิดการจองซ้ำ คุณแน่ใจหรือว่าต้องการดำเนินการต่อ?"); 
            
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    //ยืนยันเจ้าก็เปลี่ยนได้
                    roomUpdate.setStatus(newStatus); // ถ้าผู้ใช้ยืนยัน ก็ให้เปลี่ยน
                    roomRepository.saveRoomToCSV();
                }
            }else{
                    roomUpdate.setStatus(newStatus); // กรณีอื่นๆ ให้เปลี่ยนได้เลย
                    roomRepository.saveRoomToCSV(); 
            }

            roomTable.refresh();
        });

        // ตั้งค่าคอลัมน์ Image ให้แสดงเป็นรูปภาพ
        //Lambda Expression หลักการ (parameter) -> { statements }
        imageColumn.setCellFactory(col -> new TableCell<RoomsTableView,String>() {
            private final ImageView imageView = new ImageView();{
                imageView.setFitHeight(80);
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);
            }

            protected void updateItem(String path, boolean empty){
                super.updateItem(path, empty);
                if(empty || path == null || path.isBlank()){
                    setGraphic(null);
                }else{
                    try{
                        Image image = new Image(new File(path).toURI().toString(), 100, 80, true, true);
                        imageView.setImage(image);
                        setGraphic(imageView);
                    }catch (Exception e){
                        setGraphic(null);
                    }
                }
            }
        }
        );

        // โหลดข้อมูลจาก Repository มาใส่ใน List
        for (Room room : roomRepository.getAllRooms()) {
            roomList.add(new RoomsTableView(room)); 
        }

        roomTable.setEditable(true); //แก้ตารางได้
        statusColumn.setEditable(true); //แก้สเตตัสได้

        //set เรื่องการค้นหา
        //สร้าง FilteredList เพื่อกรองข้อมูลจาก `roomList`
        FilteredList<RoomsTableView> filteredData = new FilteredList<>(roomList, p -> true);

        //ติดตั้ง Listeners ให้กับ Filter Controls
        searchField.textProperty().addListener((obs, oldVal,newVal) ->{
            filteredData.setPredicate(createPredicate(newVal, allTypeComboBox.getValue(), statusComboBox.getValue()));
        });

        allTypeComboBox.valueProperty().addListener((obs, oldVal,newVal) ->{
            filteredData.setPredicate(createPredicate(searchField.getText(), newVal, statusComboBox.getValue()));
        });

        statusComboBox.valueProperty().addListener((obs, oldVal,newVal) ->{
            filteredData.setPredicate(createPredicate(searchField.getText(), allTypeComboBox.getValue(), newVal));
        });
        
        // สร้าง SortedList เพื่อจัดเรียงข้อมูล และผูกเข้ากับ TableView
        SortedList<RoomsTableView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(roomTable.comparatorProperty());
        //เราข้อมูลทั้งหมดใส่ใน tableview
        roomTable.setItems(sortedData);
        
        //สร้างปุ่มใน action ตั้งค่าคอลัมน์ Actions
        setupActionColumn();
    } 

    /**
     * สร้าง ฟังก์ชันทดสอบ สำหรับใช้ในการกรองข้อมูลใน TableView
     * @param searchText ข้อความจากช่องค้นหา
     * @param type       ประเภทห้องที่เลือกจาก ComboBox
     * @param status     สถานะที่เลือกจาก ComboBox
     * @return Predicate ที่รวมเงื่อนไขการกรองทั้งหมด
     */
    private Predicate<RoomsTableView> createPredicate(String searchText, String type, String status){
        //ส่งข้อมูลก็ต่อเมื่อ ....
        return room -> {
            // ค้นหาด้วยข้อความ
            boolean searchMatch = true;
            if (searchText != null && !searchText.isEmpty()) {
                searchMatch = room.getNumberRoom().toLowerCase().contains(searchText.toLowerCase());
            }

            // กรองด้วยประเภทห้อง
            boolean typeMatch = true;
            if (type != null && !type.equals("All Types")) {
                typeMatch = room.getRoomType().equalsIgnoreCase(type);
            }

            // กรองด้วยสถานะ
            boolean statusMatch = true;
            if (status != null && !status.equalsIgnoreCase("All status")) {
                statusMatch = room.getStatus().equalsIgnoreCase(status);
            }

            return searchMatch && typeMatch && statusMatch;
        };
    }

    /**
     * ตั้งค่าคอลัมน์ "Actions" ใน TableView ให้มีปุ่ม Edit และ Delete
     * ใช้วิธีการสร้าง Custom TableCell ที่บรรจุ HBox ซึ่งมีปุ่มอยู่ข้างใน
     */
    private void setupActionColumn() {
        // Callback คือ "โรงงาน" ที่ TableView จะใช้เพื่อสร้าง Cell ในแต่ละแถว
        Callback<TableColumn<RoomsTableView, Void>, TableCell<RoomsTableView, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<RoomsTableView, Void> call(final TableColumn<RoomsTableView, Void> param) {
                // สร้าง Custom TableCell ขึ้นมาใหม่
                final TableCell<RoomsTableView, Void> cell = new TableCell<>() {

                    private final Button editButton = new Button();
                    private final Button deleteButton = new Button();

                    // โค้ดในปีกกาจะทำงานแค่ครั้งเดียวตอนที่ Cell ถูกสร้าง
                    //ออกแบบตัวปุ่ม
                    {
                       // ออกแบบและตั้งค่าปุ่ม 
                        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/img/icons8-edit-64.png")));
                        editIcon.setFitHeight(16);
                        editIcon.setFitWidth(16);

                        ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/img/icons8-trash-60.png")));
                        deleteIcon.setFitHeight(16);
                        deleteIcon.setFitWidth(16);

                        editButton.setGraphic(editIcon);
                        deleteButton.setGraphic(deleteIcon);

                        //ใส่ใน styclass ส่งต่อให้เจ้เจ๊แต่ง css
                        editButton.getStyleClass().add("action-button");
                        deleteButton.getStyleClass().add("action-button");
                    }

	                //ใช้ Instance Initializer Block {...} เพื่อกำหนด Action ให้กับปุ่ม //ทำงานครั้งเดียว
                    {
                        editButton.setOnAction(event -> {
                            RoomsTableView roomView = getTableView().getItems().get(getIndex());
                            populateFormForEdit(roomView.getRoom()); 
                        });

                        deleteButton.setOnAction(event -> {
                            // ดึงข้อมูลของแถวที่ปุ่มอยู่
                            RoomsTableView roomView = getTableView().getItems().get(getIndex());
                            deleteRoom(roomView); // เรียกเมธอดเพื่อทำการลบ
                        });
                    }

                    private final HBox pane = new HBox(10, editButton, deleteButton);

                    // อัปเดตการแสดงผลของ Cell 
                    // ถูกเรียกทุกครั้งที่ตารางมีการเปลี่ยนแปลง
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null); // ถ้าเป็นแถวว่าง ให้ซ่อนปุ่ม
                        } else {
                            setGraphic(pane); // ถ้าเป็นแถวที่มีข้อมูล ให้แสดงปุ่ม
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);
        
    }

    /**
     * นำข้อมูลของห้องพักที่เลือกมาเติมลงในฟอร์มด้านข้าง และเปลี่ยน UI ให้แก้ไขได้
     * @param room Room object ที่ต้องการแก้ไข
     */
    private void populateFormForEdit(Room room){

        // เก็บห้องที่กำลังแก้ไขไว้
        currentEditRoom = room;

        // นำข้อมูลจาก Room object ไปใส่ใน UI Controls
        roomNoField.setText(room.getNumberRoom());
        roomTypeComboBox.setValue(room.getType());
        priceField.setText(String.valueOf(room.getPrice()));
        peopleCombobox.setValue((room.getPeople()));
        imagePartField.setText(room.getImagePath());

        // ตั้งค่า CheckBox ตามคุณสมบัติของห้อง
        List<String> features = room.getProperties();
        for(CheckBox cb : allCheckbox){
            cb.setSelected(features.contains(cb.getText()));
        }

        // เปลี่ยนสถานะของ UI ให้อยู่ใน "โหมดแก้ไข"
        roomNoField.setDisable(true);       // ไม่ให้แก้เลขห้อง
        AddRoomButton.setDisable(true);     // ปิดปุ่ม Add
        SaveEditButton.setDisable(false);   // เปิดปุ่ม Save Edit
    }

    /**
     * ทำการลบห้องพักที่เลือกออกจากระบบ
     * @param roomsTableViewToDelete RoomsTableView object ของแถวที่ต้องการลบ
     */
    private void deleteRoom(RoomsTableView roomsTableViewToDelete){
        Room roomToDelete = roomsTableViewToDelete.getRoom();

        // แสดงหน้าต่างยืนยันเพื่อป้องกันการลบโดยไม่ตั้งใจ
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("ห้องที่ต้องการลบ " + roomsTableViewToDelete.getNumberRoom());
        alert.setContentText("คุณแน่ใจใช่ไหมที่จะลบห้องนี้ !?");

        Optional <ButtonType> result = alert.showAndWait();

        // ดำเนินการลบเมื่อผู้ใช้กดยืนยัน "OK"
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // ลบออกจาก Repository 
            roomRepository.removeRoom(roomToDelete.getNumberRoom());
            // บันทึกการเปลี่ยนแปลงลงไฟล์ CSV
            roomRepository.saveRoomToCSV();
            // ลบออกจาก ObservableList เพื่ออัปเดต TableView ทันที
            roomList.remove(roomsTableViewToDelete);
        }
    }

    /**
     * ดึงข้อมูลจากฟอร์ม, ตรวจสอบข้อมูลว่าครบมั้ย, และสร้างเป็น Room object ใหม่
     * @return Room object ที่มีข้อมูลครบ หรือ null หากข้อมูลไม่ครบถ้วน
     */
    public Room getRoomDatafromFome(){
        // ตรวจสอบว่ากรอกข้อมูลที่จำเป็นครบหรือไม่
        if (roomNoField.getText().isEmpty() || roomTypeComboBox.getValue() == null ||
            imagePartField.getText().isEmpty() ||priceField.getText().isEmpty() || peopleCombobox.getValue() == null ) {
            
                Alert alert = new Alert(Alert.AlertType.WARNING, "โปรดกรอกข้อมูลให้ครบถ้วน");
                alert.showAndWait();
                return null; // คืนค่า null เพื่อหยุด

        } else{

        // ถ้าข้อมูลครบ รวมข้อมูลทั้งหมด
        String rooomNo = roomNoField.getText().toUpperCase();
        String roomType = roomTypeComboBox.getValue();
        String roomImage = imagePartField.getText();
        Integer selectpeople = peopleCombobox.getValue();
        double roomPrice = 0.0;
        int numberOfpeople = 0;
        


        try {
            // แปลง String เป็นตัวเลข
            roomPrice = Double.parseDouble(priceField.getText());
            numberOfpeople = Integer.valueOf(peopleCombobox.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            System.out.println("Error: Invalid price or number of people format.");
            new Alert(Alert.AlertType.ERROR, "ราคาและจำนวนคนต้องเป็นตัวเลขเท่านั้น").showAndWait();
            return null; // หยุดถ้าแปลงค่าไม่ได้
        }

        // รวบรวมคุณสมบัติจาก CheckBox
        seselectedProperties.clear();
        for(CheckBox cb : allCheckbox){
            if (cb != null && cb.isSelected()) {
                seselectedProperties.add(cb.getText());
            }
        }
        // สร้างและคืนค่า Room object ใหม่
        return new Room(rooomNo, roomType, roomPrice, roomImage ,numberOfpeople,seselectedProperties,RoomStatus.AVAILABLE);
        }
    }

    /**
     * Action-Handler หลักสำหรับปุ่ม Add Room
     * ทำหน้าที่ตรวจสอบข้อมูลซ้ำ, บันทึกห้องใหม่, และอัปเดต UI
     */
    @FXML private void  handleSaveButtonAction(){
        // ดึงข้อมูลจากฟอร์ม
        Room newRoom = getRoomDatafromFome();

        // ตรวจสอบว่ามีหมายเลขห้องนี้อยู่แล้วหรือไม่
        if (roomRepository.findByRoomNo(newRoom.getNumberRoom())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ข้อมูลซ้ำ");
            alert.setHeaderText(null);
            alert.setContentText("ห้องเลข " + newRoom.getNumberRoom() + " ถูกลงทะเบียนแล้ว");
            alert.showAndWait();
            return ;
        }

        // ถ้าข้อมูลถูกต้องและไม่ซ้ำ ให้ทำการบันทึก
        if (newRoom != null) { 
            roomRepository.addRoom(newRoom);
            roomRepository.saveRoomToCSV();

            roomList.add(new RoomsTableView(newRoom)); // เพิ่มแถวใหม่ในคอลลัมอัตโนมัติ

            //เคลียร์หลัง save
            clearForm();
        }

    }

    @FXML private void saveRoom(ActionEvent event) {
        handleSaveButtonAction();
    }

    //สามารถแอดรูปได้
    /**
     * Action-Handler สำหรับปุ่ม Browse Image
     * ทำหน้าที่เปิด FileChooser ให้ผู้ใช้เลือกไฟล์รูปภาพ
     */
    @FXML private void handleChooseImg(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select img");
        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
);

        File selectedFile = fileChooser.showOpenDialog(addImgButton.getScene().getWindow());
        if (selectedFile != null) {
            //เก็บ path ใน textfield
            imagePartField.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * method สำหรับล้างข้อมูลในฟอร์มทั้งหมด
     */
    private void clearForm(){
        roomNoField.clear();
        priceField.clear();
        peopleCombobox.getSelectionModel().clearSelection();
        imagePartField.clear();
        roomTypeComboBox.getSelectionModel().clearSelection();

        for(CheckBox cb : allCheckbox){
            cb.setSelected(false);
        }
    }

    /**
     * Action-Handler สำหรับปุ่ม "Save Edit"
     * ทำหน้าที่บันทึกข้อมูลที่ถูกแก้ไขในฟอร์ม
     */
    @FXML private void handleSaveEditButtonAction(){
        updateRoomDataFromForm();       // อัปเดตข้อมูลใน object ที่กำลังแก้ไข
        roomRepository.saveRoomToCSV(); // บันทึกการเปลี่ยนแปลงลงไฟล์
        roomTable.refresh();            // คอลลัมทำข้อมูลใหม่เพื่อแสดงผลการแก้ไข
        clearFormAndResetMode();        

    }

    /**
     * method ที่อ่านข้อมูลจากฟอร์มและอัปเดตลงใน `currentEditRoom` object
     */
    private void updateRoomDataFromForm(){
        // ตรวจสอบว่าอยู่ในโหมดแก้ไขจริง
        if (currentEditRoom != null) {
            try {
                // อัพเดทข้อมูล
                currentEditRoom.setRoomType(roomTypeComboBox.getValue());
                currentEditRoom.setPrice(Double.parseDouble(priceField.getText()));
                currentEditRoom.setPeople(Integer.valueOf(peopleCombobox.getValue()));
                currentEditRoom.setImagePath(imagePartField.getText());

                // รวมคุณสมบัติจาก CheckBox ที่เลือกใหม่
                seselectedProperties.clear();
                for(CheckBox cb : allCheckbox){
                    if (cb != null && cb.isSelected()) {
                        seselectedProperties.add(cb.getText());
                    }
                }

                currentEditRoom.setProperties(new ArrayList<>(seselectedProperties));

            } catch (Exception e) {
                System.out.println(e.getMessage()); 
                new Alert(Alert.AlertType.ERROR, "ราคาและจำนวนคนต้องเป็นตัวเลขเท่านั้น").showAndWait();
            }
        }
    }

    /**
     * method สำหรับล้างฟอร์มและรีเซ็ตสถานะ UI เพื่อเพิ่มข้อมูล
     */
    private void clearFormAndResetMode(){
        clearForm();
        currentEditRoom = null;
        roomNoField.setDisable(false); //ให้กรอกเลขห้องได้
        AddRoomButton.setDisable(false);
        SaveEditButton.setDisable(true);

    }

    //มีใครอยู่บ่
    /**
     * ตรวจสอบว่าห้องที่ระบุมีผู้เข้าพักอยู่จริงในเวลาปัจจุบันหรือไม่
     * @param room ห้องที่ต้องการตรวจสอบ
     * @return true ถ้ามีผู้เข้าพักอยู่, false ถ้าไม่มี
     */
    private boolean isRoomCurrentlyOccupied(Room room){
        BookingRepository bookingRepository = BookingRepository.getInstance();
        LocalDateTime now = LocalDateTime.now();

        for(Bookings booking : bookingRepository.getAllBookings()){
            if (booking.getRoom().getNumberRoom().equals(room.getNumberRoom())) {
                if (booking.getStatus() == BookingStatus.CHECKED_IN) {
                    LocalDateTime  checkin = booking.getCheckinDateTime();
                    LocalDateTime checkout = booking.getCheckoutDateTime();

                    if (!now.isBefore(checkin) && now.isBefore(checkout)) {
                        return true; // เจอคนพักอยู่
                    }
                }
            }
        }
        return false; // ไม่เจอใครพักอยู่
    }
}

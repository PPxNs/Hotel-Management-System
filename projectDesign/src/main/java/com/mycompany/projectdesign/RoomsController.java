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

import com.mycompany.projectdesign.Project.Model.BookingRepository;
import com.mycompany.projectdesign.Project.Model.BookingStatus;
import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.Model.Room;
import com.mycompany.projectdesign.Project.Model.RoomRepository;
import com.mycompany.projectdesign.Project.Model.RoomStatus;

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



public class RoomsController implements Initializable {

    //ดึง javaFX มา
    @FXML private TableView<RoomsTableView> roomTable;

    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private ComboBox<String> allTypeComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField searchField;
    @FXML private TextField roomNoField;
    @FXML private TextField priceField;
    @FXML private TextField peopleField;
    @FXML private TextField imagePartField;
    @FXML private CheckBox jacuzziCheckBox;
    @FXML private CheckBox lakeViewCheckBox;
    @FXML private CheckBox petFriendlyCheckBox;
    @FXML private CheckBox privatePoolCheckBox;
    @FXML private CheckBox tvCheckBox;
    @FXML private CheckBox wifiCheckBox;

    //ดึงแต่ละ coluumn
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

            System.out.println("ขนาดของข้อมูลห้องพักที่โหลดมาคือ: " + roomRepository.getAllRooms().size());


        /*roomRepository.loadRoomFromCSV();*/

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

        //อันนี้เซตค่าใน combobox 
        roomTypeComboBox.setItems(roomType);
        allTypeComboBox.setItems(allType);
        allTypeComboBox.setValue("All Types");
        statusComboBox.setItems(allStatus);
        statusComboBox.setValue("All status");
        
        allCheckbox = Arrays.asList(jacuzziCheckBox, lakeViewCheckBox,petFriendlyCheckBox,privatePoolCheckBox,tvCheckBox,wifiCheckBox);

        roomNoColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("numberRoom"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("image"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("roomType"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("price"));
        peopleColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("people"));
        propertyColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("property"));

        ObservableList<String> statusObtions = FXCollections.observableArrayList();
        for(RoomStatus status : RoomStatus.values()){
            statusObtions.add(status.name());
        }

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(ComboBoxTableCell.forTableColumn(statusObtions));
        
        //ปรับการกรอง
        statusColumn.setOnEditCommit(event -> {
            RoomsTableView roomView = event.getRowValue();
            Room roomUpdate = roomView.getRoom();
            RoomStatus newStatus = RoomStatus.valueOf(event.getNewValue());
            
            if (newStatus == RoomStatus.AVAILABLE && isRoomCurrentlyOccupied(roomUpdate)) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("ยืนยันการจอง");
                confirmationAlert.setHeaderText("คำเตือน : อาจมีการจองซ้ำ");
                confirmationAlert.setContentText("ห้องนี้ปัจจุบันมีผู้เข้าพักแล้ว การเปลี่ยนสถานะเป็นว่างอาจทำให้เกิดการจองซ้ำ คุณแน่ใจหรือว่าต้องการดำเนินการต่อ?"); 
            
                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    //ยืนยันเจ้าก็เปลี่ยนได้
                    roomUpdate.setStatus(newStatus);
                    roomRepository.saveRoomToCSV();
                }
            }else{
                    roomUpdate.setStatus(newStatus);
                    roomRepository.saveRoomToCSV(); 
            }

            roomTable.refresh();
        });

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


        for (Room room : roomRepository.getAllRooms()) {
            roomList.add(new RoomsTableView(room)); 
        }

        roomTable.setEditable(true); //แก้ตารางได้
        statusColumn.setEditable(true); //แก้สเตตัสได้
        //set เรื่องการค้นหา
        FilteredList<RoomsTableView> filteredData = new FilteredList<>(roomList, p -> true);
        searchField.textProperty().addListener((obs, oldVal,newVal) ->{
            filteredData.setPredicate(createPredicate(newVal, allTypeComboBox.getValue(), statusComboBox.getValue()));
        });

        allTypeComboBox.valueProperty().addListener((obs, oldVal,newVal) ->{
            filteredData.setPredicate(createPredicate(searchField.getText(), newVal, statusComboBox.getValue()));
        });

        statusComboBox.valueProperty().addListener((obs, oldVal,newVal) ->{
            filteredData.setPredicate(createPredicate(searchField.getText(), allTypeComboBox.getValue(), newVal));
        });
        
        SortedList<RoomsTableView> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(roomTable.comparatorProperty());
        //เราข้อมูลทั้งหมดใส่ใน tableview

        roomTable.setItems(sortedData);
        
        //สร้างปุ่มใน action
        setupActionColumn();
    } 

    
    private Predicate<RoomsTableView> createPredicate(String searchText, String type, String status){
        //ส่งข้อมูลก็ต่อเมื่อ ....
        return room -> {
            boolean searchMatch = true;
            if (searchText != null && !searchText.isEmpty()) {
                searchMatch = room.getNumberRoom().toLowerCase().contains(searchText.toLowerCase());
            }

            boolean typeMatch = true;
            if (type != null && !type.equals("All Types")) {
                typeMatch = room.getRoomType().equalsIgnoreCase(type);
            }

            boolean statusMatch = true;
            if (status != null && !status.equalsIgnoreCase("All status")) {
                statusMatch = room.getStatus().equalsIgnoreCase(status);
            }

            return searchMatch && typeMatch && statusMatch;
        };
    }

    private void setupActionColumn() {
        Callback<TableColumn<RoomsTableView, Void>, TableCell<RoomsTableView, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<RoomsTableView, Void> call(final TableColumn<RoomsTableView, Void> param) {
                final TableCell<RoomsTableView, Void> cell = new TableCell<>() {

                    private final Button editButton = new Button();
                    private final Button deleteButton = new Button();

                    //ออกแบบตัวปุ่ม
                    {
                       
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
                            RoomsTableView roomView = getTableView().getItems().get(getIndex());
                            deleteRoom(roomView); //
                        });
                    }

                    private final HBox pane = new HBox(10, editButton, deleteButton);

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void populateFormForEdit(Room room){
        currentEditRoom = room;
        roomNoField.setText(room.getNumberRoom());
        roomTypeComboBox.setValue(room.getType());
        priceField.setText(String.valueOf(room.getPrice()));
        peopleField.setText(String.valueOf(room.getPeople()));
        imagePartField.setText(room.getImagePath());

        List<String> features = room.getProperties();
        for(CheckBox cb : allCheckbox){
            cb.setSelected(features.contains(cb.getText()));
        }

        roomNoField.setDisable(true); // ไม่ให้แก้เลขห้อง
        AddRoomButton.setDisable(true);
        SaveEditButton.setDisable(false);
    }

    private void deleteRoom(RoomsTableView roomsTableViewToDelete){
        Room roomToDelete = roomsTableViewToDelete.getRoom();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("ห้องที่ต้องการลบ " + roomsTableViewToDelete.getNumberRoom());
        alert.setContentText("คุณแน่ใจใช่ไหมที่จะลบห้องนี้ !?");

        Optional <ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            roomRepository.removeRoom(roomToDelete.getNumberRoom());
            roomRepository.saveRoomToCSV();
            roomList.remove(roomsTableViewToDelete);
        }
    }



    
    public Room getRoomDatafromFome(){
        if (roomNoField.getText().isEmpty() || roomTypeComboBox.getValue() == null ||
            imagePartField.getText().isEmpty() ||priceField.getText().isEmpty() || peopleField.getText().isEmpty() ) {
            
                System.out.println("เขียนข้อมูลไม่ครบ"); //อาจจะเพิ่ม pop up แจ้ง 
                return null ;

        } else{

        String rooomNo = roomNoField.getText().toUpperCase();
        String roomType = roomTypeComboBox.getValue();
        String roomImage = imagePartField.getText();
        double roomPrice = 0.0;
        int numberOfpeople = 0;

        try {
            roomPrice = Double.parseDouble(priceField.getText());
            numberOfpeople = Integer.parseInt(peopleField.getText());
        } catch (Exception e) {
             System.out.println("Error: Invalid price or number of people format.");
             //อาจขึ้น pop up แจ้ง
        }

        seselectedProperties.clear();
        for(CheckBox cb : allCheckbox){
            if (cb != null && cb.isSelected()) {
                seselectedProperties.add(cb.getText());
            }
        }



        return new Room(rooomNo, roomType, roomPrice, roomImage ,numberOfpeople,seselectedProperties,RoomStatus.AVAILABLE);
        }
    }

    @FXML private void  handleSaveButtonAction(){
        Room newRoom = getRoomDatafromFome();

        if (roomRepository.findByRoomNo(newRoom.getNumberRoom())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ข้อมูลซ้ำ");
            alert.setHeaderText(null);
            alert.setContentText("ห้องเลข " + newRoom.getNumberRoom() + " ถูกลงทะเบียนแล้ว");
            alert.showAndWait();
            return ;
        }

        if (newRoom != null) { 
            roomRepository.addRoom(newRoom);
            roomRepository.saveRoomToCSV();

            roomList.add(new RoomsTableView(newRoom));

            //เคลียร์หลัง save
            clearForm();
        }

    }

    @FXML private void saveRoom(ActionEvent event) {
        handleSaveButtonAction();
        //หลัง save ให้นางแสดงผล
        /*ObservableList<RoomsTableView> updatedList = FXCollections.observableArrayList();
        for (Room room : roomRepository.getAllRooms()) {
            updatedList.add(new RoomsTableView(room));
        }
        
        roomTable.setItems(updatedList);*/

    }

    //สามารถแอดรูปได้
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

    private void clearForm(){
        roomNoField.clear();
        priceField.clear();
        peopleField.clear();
        imagePartField.clear();
        roomTypeComboBox.getSelectionModel().clearSelection();

        for(CheckBox cb : allCheckbox){
            cb.setSelected(false);
        }
    }

    @FXML private void handleSaveEditButtonAction(){
        updateRoomDataFromForm();
        roomRepository.saveRoomToCSV();
        roomTable.refresh();
        clearFormAndResetMode();

    }

    private void updateRoomDataFromForm(){
        if (currentEditRoom != null) {
            try {
                // อัพเดทข้อมูล
                currentEditRoom.setRoomType(roomTypeComboBox.getValue());
                currentEditRoom.setPrice(Double.parseDouble(priceField.getText()));
                currentEditRoom.setPeople(Integer.parseInt(peopleField.getText()));
                currentEditRoom.setImagePath(imagePartField.getText());

                seselectedProperties.clear();
                for(CheckBox cb : allCheckbox){
                    if (cb != null && cb.isSelected()) {
                        seselectedProperties.add(cb.getText());
                    }
                }

                currentEditRoom.setProperties(new ArrayList<>(seselectedProperties));

            } catch (Exception e) {
                System.out.println(e.getMessage()); // ค่อยทำ popup 
            }
        }
    }

    private void clearFormAndResetMode(){
        clearForm();
        currentEditRoom = null;
        roomNoField.setDisable(false); //ให้กรอกเลขห้องได้
        AddRoomButton.setDisable(false);
        SaveEditButton.setDisable(true);

    }

    //มีใครอยู่บ่
    private boolean isRoomCurrentlyOccupied(Room room){
        BookingRepository bookingRepository = BookingRepository.getInstance();
        LocalDateTime now = LocalDateTime.now();

        for(Bookings booking : bookingRepository.getAllBookings()){
            if (booking.getRoom().getNumberRoom().equals(room.getNumberRoom())) {
                if (booking.getStatus() == BookingStatus.CHECKED_IN) {
                    LocalDateTime  checkin = booking.getCheckinDateTime();
                    LocalDateTime checkout = booking.getCheckoutDateTime();

                    if (!now.isBefore(checkin) && now.isBefore(checkout)) {
                        return true; //มีคนเด้อ
                    }
                }
            }
        }
        return false;
    }



    
    
}

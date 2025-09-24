/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectdesign;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.mycompany.projectdesign.Project.Model.Room;
import com.mycompany.projectdesign.Project.Model.RoomRepository;
import com.mycompany.projectdesign.Project.Model.RoomStatus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;



public class RoomsController implements Initializable {

    //ดึง javaFX มา
    @FXML private TableView<RoomsTableView> roomTable;

    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private ComboBox<String> allTypeComboBox;
    @FXML private ComboBox<String> statusComboBox;
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

    //ปุ่ม
    @FXML private Button addImgButton;

    private List<CheckBox> allCheckbox;
    private final List<String> seselectedProperties = new ArrayList<>();
    private RoomRepository roomRepository = RoomRepository.getInstance();
    private ObservableList<RoomsTableView> roomList = FXCollections.observableArrayList();

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

        ObservableList<String> statusRoom = FXCollections.observableArrayList(
       "AVAILABLE",     
                "OCCUPIED",      
                "CLEANING",        
                "MAINTENANCE"      
        );

        //อันนี้เซตค่าใน combobox 
        roomTypeComboBox.setItems(roomType);
        allTypeComboBox.setItems(roomType);
        statusComboBox.setItems(statusRoom);
        
        allCheckbox = Arrays.asList(jacuzziCheckBox, lakeViewCheckBox,petFriendlyCheckBox,privatePoolCheckBox,tvCheckBox,wifiCheckBox);

        roomNoColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("numberRoom"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("image"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("roomType"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("price"));
        peopleColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("people"));
        propertyColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("property"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<RoomsTableView,String>("status"));


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

        //เราข้อมูลทั้งหมดใส่ใน tableview
        roomTable.setItems(roomList);
        
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

        if (roomRepository.findByRoomNo(rooomNo)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ข้อมูลซ้ำ");
            alert.setHeaderText(null);
            alert.setContentText("ห้องเลข " + rooomNo + " ถูกลงทะเบียนแล้ว");
            alert.showAndWait();
            return null;
        }

        return new Room(rooomNo, roomType, roomPrice, roomImage ,numberOfpeople,seselectedProperties,RoomStatus.AVAILABLE);
        }
    }

    @FXML private void  handleSaveButtonAction(){
        Room newRoom = getRoomDatafromFome();
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

    
    
}

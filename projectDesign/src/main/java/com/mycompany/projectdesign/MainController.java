package com.mycompany.projectdesign;

import javafx.scene.Node;
import javafx.scene.control.Button;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import com.mycompany.projectdesign.Project.ObserverPattern.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


/**
 * Controller หลักสำหรับหน้าต่างโปรแกรม 
 * ทำหน้าที่ควบคุมการทำงานหลักๆ ของแอปพลิเคชัน ได้แก่:
 * 1. การเปลี่ยนหน้า (Page Navigation) ในส่วนกลางของโปรแกรม
 * 2. การรับและจัดการระบบแจ้งเตือน (Notification System)
 * คลาสนี้ยังทำหน้าที่เป็น Observer เพื่อดักฟัง Event ที่เกิดขึ้นในระบบ
 */

public class MainController implements Initializable, HotelObserver{

    @FXML private AnchorPane rootPane;          // Pane หลักสุดของหน้าต่าง
    @FXML private Button notificationButton;    // ปุ่มกระดิ่งแจ้งเตือน
    @FXML private BorderPane mainContentPane;   // พื้นที่ส่วนกลางสำหรับแสดงเนื้อหาของแต่ละหน้า

    private List<Button> menuButtons; // List สำหรับเก็บปุ่มเมนูทั้งหมด
    private Button currentSelectedButton; // ปุ่มที่ถูกเลือกอยู่ปัจจุบัน
    @FXML private Button homeButton;
    @FXML private Button roomsButton;
    @FXML private Button reservationsButton;
    @FXML private Button guestsButton;

    private final HotelEventManager eventManager = HotelEventManager.getInstance();
    private List<HotelEvent> notifications = new ArrayList<>(); // List สำหรับเก็บ Event ที่ยังไม่ได้อ่าน
    private Node notificationPopup = null;  // Node สำหรับเก็บ Pop-up ที่แสดงอยู่ (ถ้ามี)
    

    /**
     * เมธอดที่ถูกเรียกเมื่อ Controller ถูกสร้างและเชื่อมกับ FXML เรียบร้อยแล้ว
     * ใช้สำหรับตั้งค่าเริ่มต้นของแอปพลิเคชัน
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuButtons = Arrays.asList(homeButton, guestsButton, reservationsButton, roomsButton);
        // ตั้งค่าปุ่ม Home เป็นปุ่มเริ่มต้นเมื่อเปิดโปรแกรม
        currentSelectedButton = homeButton;
        updateselectedButton();
        loadPage("Home.fxml");
        eventManager.addObserver(this);
        eventManager.addObserver(new BillObserver());
    }

    //เมธอดไปหน้าต่อ ๆ ไป
    @FXML private void showRoomsPage(ActionEvent event){
        updateCurrentButton(roomsButton);
        loadPage("Rooms.fxml");
    }

    @FXML private void showHomePage(ActionEvent event) {
        updateCurrentButton(homeButton);
        loadPage("Home.fxml");
    }

    @FXML private void showReservationsPage(ActionEvent event) {
        updateCurrentButton(reservationsButton);
        loadPage("Reservations.fxml");
    }

    @FXML private void showGuestsPage(ActionEvent event) {
        updateCurrentButton(guestsButton);
        loadPage("Guests.fxml");
    }
    

    /**
     * Helper method สำหรับโหลดไฟล์ FXML ที่ต้องการมาแสดงในพื้นที่ส่วนกลาง (mainContentPane)
     * @param fxmlFile ชื่อไฟล์ FXML ที่ต้องการโหลด (เช่น "Home.fxml")
     */

    //loader.load() มันบังคับ try catch
    @FXML private void loadPage(String fxmlFile){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node page = loader.load();
            mainContentPane.setCenter(page);
        } catch (IOException e) {
            System.err.println("ไม่สามารถโหลดไฟล์: " + fxmlFile);
            e.printStackTrace();
        }
    }

    /**
     * เมธอดจาก Interface HotelObserver ที่จะถูกเรียกโดย HotelEventManager เมื่อมี Event ใหม่เกิดขึ้น
     * @param event ออบเจกต์ของ Event ที่เกิดขึ้น
     */
    @Override
    public void update(HotelEvent event) {
        // สนใจเฉพาะ Event ที่เกี่ยวกับการแจ้งเตือน Check-in และ Check-out
        if (event instanceof MissedCheckinEvent || event instanceof MissedCheckoutEvent || event instanceof MaintenanceRoomEvent) {
             // ใช้ Platform.runLater เพื่อให้แน่ใจว่าการแก้ไข List และ UI ทำงานบน Thread ที่ถูกต้อง
            Platform.runLater(() ->{
                notifications.add(event);
                updateNotificationUI();
            });
        }
    }

    /**
     * อัปเดต UI ของปุ่มแจ้งเตือน (แสดงจำนวน, เพิ่มเอฟเฟกต์)
     */
    private void updateNotificationUI(){
        // ถ้าไม่มีการแจ้งเตือน
        if (notifications.isEmpty()) {
            notificationButton.setText("");
            notificationButton.getStyleClass().remove("has-notification");
        } else { // ถ้ามีการแจ้งเตือน
            notificationButton.setText(String.valueOf(notifications.size()));
            if (!notificationButton.getStyleClass().contains("has-notification")) {
                notificationButton.getStyleClass().add("has-notification");
            }
        }
        System.out.println("มีการแจ้งเตือนทั้งหมด : " + notifications.size() + " รายการ");
    }

    /**
     * เมธอดที่ผูกกับปุ่มกระดิ่ง ทำหน้าที่สลับการแสดงผล (Toggle) ของ Pop-up แจ้งเตือน
     */
    @FXML private void handleBellCheck(){
        // ถ้า Pop-up ยังไม่ถูกสร้าง (ปิดอยู่)
        if (notificationPopup == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Notifications.fxml"));
                notificationPopup = loader.load(); // โหลด FXML ของ Pop-up
                NotificationsController controller = loader.getController(); // ดึง Controller ของ Pop-up
                
                // ส่งข้อมูล (List notifications) และ Callback Action (เมธอด hideNotificationPopup)
                // ไปให้ NotificationsController
                controller.setNotifications(notifications, this::deleteNotification, this::hideNotificationPopup, this::clearNotifications);
                
                // ตั้งค่าตำแหน่งของ Pop-up
                AnchorPane.setTopAnchor(notificationPopup, 40.0);
                AnchorPane.setLeftAnchor(notificationPopup, 200.0);
                
                rootPane.getChildren().add(notificationPopup); // แสดง Pop-up

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            hideNotificationPopup(); // ให้ปิด Pop-up
        }
    }

    /**
     * ซ่อน/ปิด Pop-up การแจ้งเตือน และเคลียร์สถานะการแจ้งเตือนทั้งหมด
     */
    private void hideNotificationPopup() {
        if (notificationPopup != null) {
            rootPane.getChildren().remove(notificationPopup);
            notificationPopup = null;
        }
    }

    /**
     * เคลียร์ข้อมูลการแจ้งเตือนและรีเซ็ต UI ของปุ่ม
     */
    private void clearNotifications() {
        notifications.clear();
        notificationButton.setText("");
        notificationButton.getStyleClass().remove("has-notification");
    }

    /**
     * เมธอดสำหรับลบการแจ้งเตือนทีละรายการ (จะถูกเรียกจาก NotificationsController)
     * @param eventToDelete Event ที่ต้องการลบ
     */
    private void deleteNotification(HotelEvent eventToDelete) {
        notifications.remove(eventToDelete); // ลบออกจาก List หลัก
        updateNotificationUI(); // อัปเดต UI ปุ่มกระดิ่ง
    }



    /**
     * อัปเดตปุ่มที่ถูกเลือกล่าสุด โดยจะลบคลาส "selected" ออกจากปุ่มเก่าก่อน
     * @param selectedBtn ปุ่มที่เพิ่งถูกคลิก
     */
    private void updateCurrentButton(Button selectedButton){
         // ลบคลาส selected ออกจากปุ่มเดิมที่เคยถูกเลือก
        if (currentSelectedButton != null) {
            currentSelectedButton.getStyleClass().remove("selected");
        }
        // อัปเดตปุ่มที่ถูกเลือกล่าสุดให้เป็นปุ่มใหม่
        currentSelectedButton = selectedButton;
        // เรียกเมธอดเพื่อเพิ่มคลาส selected ให้ปุ่มใหม่
        updateselectedButton();
    }

    /**
     * เพิ่มคลาส "selected" ให้กับปุ่มที่ถูกเลือกในปัจจุบัน
     */
    private void updateselectedButton(){
        if (currentSelectedButton != null) {
            currentSelectedButton.getStyleClass().add("selected");
        }
    }
}

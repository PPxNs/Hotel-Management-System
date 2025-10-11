/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectdesign;


import java.util.List;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.ObserverPattern.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import java.util.function.Consumer; //Import Consumer สำหรับ Callback
import javafx.scene.control.Button; //Import Button
import javafx.scene.layout.HBox; //Import HBox
import javafx.scene.layout.Priority; //Import Priority


/**
 * Controller สำหรับหน้าจอ/พาเนลแสดงรายการแจ้งเตือน (Notifications.fxml)
 * ทำหน้าที่เป็น UI Component ที่สามารถนำไปใช้ซ้ำได้ โดยรับรายการของ HotelEvent เข้ามาแล้วสร้าง UI เพื่อแสดงผลแบบไดนามิก
 */
public class NotificationsController  {

    //Container หลักใน FXML ที่ใช้สำหรับบรรจุรายการแจ้งเตือนทั้งหมด 
    @FXML private VBox notificationVBox;

    /**
     * Callback Action ที่จะถูกเรียกใช้เมื่อผู้ใช้กดปุ่มปิด
     * ได้รับมาจากคลาสที่สร้างและควบคุม View นี้
     */
    private Runnable closeAction;
    private Runnable clearAllAction;

    /**
     * ตั้งค่าและแสดงผลรายการแจ้งเตือนใน View
     * เมธอดนี้เป็น Entry Point หลักสำหรับ Controller นี้ โดยจะรับข้อมูลที่จะแสดงและ Action ที่จะทำเมื่อปิดเข้ามาจากภายนอก
     * @param events      รายการของ HotelEvent ที่ต้องการแสดงผล
     * @param deleteAction โค้ดที่จะถูกรันเมื่อผู้ใช้กดปุ่มลบรายการแจ้งเตือน (Callback)
     * @param closeAction โค้ดที่จะถูกรันเมื่อมีการเรียกเมธอด closeWindow (Callback)
     */
    public void setNotifications(List<HotelEvent> events, Consumer<HotelEvent> deleteAction,Runnable clearAllAction, Runnable closeAction){
        this.closeAction = closeAction;
        this.clearAllAction = clearAllAction;
        notificationVBox.getChildren().clear(); // ล้างข้อมูลเก่าทิ้งก่อน

        if (events == null || events.isEmpty()) {
            showNoNotificationsLabel();
        }else {
            // วน Loop เพื่อสร้าง UI สำหรับแต่ละ Event
            for(HotelEvent event : events){
                VBox contentVBox = new VBox(5); // VBox สำหรับการแจ้งเตือน 1 รายการ
                contentVBox.getStyleClass().add("notification-entry-content");

                String titleText = "";
                String detailsText = "";

                // แยกประเภทของ Event เพื่อสร้างข้อความที่แตกต่างกัน
                if (event instanceof MissedCheckinEvent) {
                    MissedCheckinEvent mcEvent = (MissedCheckinEvent) event;
                    Bookings booking = mcEvent.getBookings();
                    titleText = "แจ้งเตือนก่อนเช็คอิน";
                    detailsText = "ห้อง: " + booking.getRoom().getNumberRoom() + "\n"
                                + "ลูกค้า: " + booking.getCustomer().getFullName() + "\n"
                                + "เวลา: " + booking.getDateCheckin() + " " + booking.getTimeCheckin();
                } else if (event instanceof MissedCheckoutEvent) {
                    MissedCheckoutEvent mcEvent = (MissedCheckoutEvent) event;
                    Bookings booking = mcEvent.getBookings();
                    titleText = "แจ้งเตือนก่อนเช็คเอาต์";
                     detailsText = "ห้อง: " + booking.getRoom().getNumberRoom() + "\n"
                                + "ลูกค้า: " + booking.getCustomer().getFullName() + "\n"
                                + "เวลา: " + booking.getDateCheckout() + " " + booking.getTimeCheckout();
                }else if (event instanceof MaintenanceRoomEvent) {
                    MaintenanceRoomEvent mEvent = (MaintenanceRoomEvent) event;
                    Bookings booking = mEvent.getBookings();
                    titleText = "แจ้งเตือนห้องซ่อมบำรุง";
                    detailsText = "ห้อง: " + booking.getRoom().getNumberRoom() + " กระทบการจองของ\n"
                                + "ลูกค้า: " + booking.getCustomer().getFullName() + "\n"
                                + "เบอร์: " + booking.getCustomer().getPhone();
                }


                // สร้าง Labels และกำหนด Style
                Label titleLabel = new Label(titleText);
                titleLabel.getStyleClass().add("notification-title");

                Label detailsLabel = new Label(detailsText);
                detailsLabel.getStyleClass().add("notification-details");
                detailsLabel.setWrapText(true);
                
                // นำ Labels ใส่ใน VBox ของรายการแจ้งเตือน
                contentVBox.getChildren().addAll(titleLabel, detailsLabel);

                // 2. สร้างปุ่มลบ
                Button deleteButton = new Button("X");
                deleteButton.getStyleClass().add("delete-notification-button");

                // 3. สร้าง HBox เพื่อจัดวางเนื้อหากับปุ่มลบให้อยู่ในแถวเดียวกัน
                HBox notificationEntryHBox = new HBox(10, contentVBox, deleteButton);
                notificationEntryHBox.getStyleClass().add("notification-entry");
                notificationEntryHBox.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(contentVBox, Priority.ALWAYS); // ทำให้ contentVBox ขยายเต็มพื้นที่

                // 4. กำหนด Action ให้กับปุ่มลบ
                deleteButton.setOnAction(e -> {
                    // เรียก Callback ที่ส่งมาจาก Controller หลัก เพื่อลบข้อมูล Event จริงๆ
                    deleteAction.accept(event);
                    // ลบ UI ของรายการนี้ออกจากหน้าจอ
                    notificationVBox.getChildren().remove(notificationEntryHBox);

                    // ตรวจสอบว่าถ้าลบจนหมดแล้ว ให้แสดงข้อความว่า "ไม่มีการแจ้งเตือน"
                    if (notificationVBox.getChildren().isEmpty()) {
                        showNoNotificationsLabel();
                    }
                });

                // นำ VBox ของรายการแจ้งเตือนไปใส่ใน VBox หลัก
                notificationVBox.getChildren().add(notificationEntryHBox);

            }
        }
    }

    /**
     * เมธอดสำหรับแสดงข้อความว่าไม่มีการแจ้งเตือน
     */
    private void showNoNotificationsLabel() {
        Label noNotifi = new Label("ไม่มีการแจ้งเตือน");
        noNotifi.getStyleClass().add("no-notification-label");
        notificationVBox.getChildren().add(noNotifi);
    }

    /**
     *
     * เมธอดนี้จะถูกเรียกโดยปุ่ม "Clear All"
     * ทำหน้าที่ 2 อย่าง: เคลียร์ข้อมูล และ ปิดหน้าต่าง
     */
    @FXML
    private void clearAllNotifications() {
        // 1. เรียก Callback เพื่อล้างข้อมูลใน MainController
        if (clearAllAction != null) {
            clearAllAction.run();
        }
        // 2. เรียก Callback เพื่อปิด/ซ่อนหน้าต่าง
        if (closeAction != null) {
            closeAction.run();
        }
    }

    /**
     * เมธอดที่ผูกกับปุ่มปิดใน FXML (onAction="#closeWindow")
     * ทำหน้าที่เรียกใช้ Callback Action ที่ได้รับมา
     * @param event ActionEvent จากการคลิกปุ่ม
     */
    @FXML private void closeWindow(ActionEvent event){
        if (closeAction != null) {
            closeAction.run();
        }
    }
}

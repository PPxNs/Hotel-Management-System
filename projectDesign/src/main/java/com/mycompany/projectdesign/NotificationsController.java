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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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


    /**
     * ตั้งค่าและแสดงผลรายการแจ้งเตือนใน View
     * เมธอดนี้เป็น Entry Point หลักสำหรับ Controller นี้ โดยจะรับข้อมูลที่จะแสดงและ Action ที่จะทำเมื่อปิดเข้ามาจากภายนอก
     * @param events      รายการของ HotelEvent ที่ต้องการแสดงผล
     * @param closeAction โค้ดที่จะถูกรันเมื่อมีการเรียกเมธอด closeWindow (Callback)
     */
    public void setNotifications(List<HotelEvent> events, Runnable closeAction){
        this.closeAction = closeAction;
        notificationVBox.getChildren().clear(); // ล้างข้อมูลเก่าทิ้งก่อน

        if (events.isEmpty()) {
            Label noNotifi = new Label("ไม่มีการแจ้งเตือน");
            noNotifi.setFont(new Font(16));
            notificationVBox.getChildren().add(noNotifi);
        }else {
            // วน Loop เพื่อสร้าง UI สำหรับแต่ละ Event
            for(HotelEvent event : events){
                VBox notificationEntry = new VBox(5); // VBox สำหรับการแจ้งเตือน 1 รายการ
                notificationEntry.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-padding: 10; -fx-border-radius: 5;");

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
                }

                // สร้าง Labels และกำหนด Style
                Label titleLabel = new Label(titleText);
                titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                Label detailsLabel = new Label(detailsText);
                detailsLabel.setWrapText(true);

                // นำ Labels ใส่ใน VBox ของรายการแจ้งเตือน
                notificationEntry.getChildren().addAll(titleLabel, detailsLabel);
                // นำ VBox ของรายการแจ้งเตือนไปใส่ใน VBox หลัก
                notificationVBox.getChildren().add(notificationEntry);

            }
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

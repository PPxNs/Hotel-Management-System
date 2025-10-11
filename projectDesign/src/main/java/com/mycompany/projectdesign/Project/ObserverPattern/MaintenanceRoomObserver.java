package com.mycompany.projectdesign.Project.ObserverPattern;

import com.mycompany.projectdesign.Project.Model.Bookings;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class MaintenanceRoomObserver implements HotelObserver{
    /**
     * เมธอดที่จะถูกเรียกโดย Subject เมื่อมีเหตุการณ์ใหม่เกิดขึ้น
     * @param event ออบเจกต์ของเหตุการณ์ที่เกิดขึ้น
     */
    
    public void update(HotelEvent event) {

        // Observer นี้จะทำงานเฉพาะเมื่อเหตุการณ์ที่ได้รับเป็น MaintenanceRoomEvent เท่านั้น
        if (event instanceof MaintenanceRoomEvent) {
            Bookings MaintenanceRoomEvent = ((MaintenanceRoomEvent) event).getBookings();
            
            // ใช้ Platform.runLater เพื่อให้แน่ใจว่าการอัปเดต UI (การแสดง Alert)
            // จะถูกประมวลผลบน JavaFX Application Thread ซึ่งเป็น Thread ที่ถูกต้อง
            // และป้องกันการเกิด IllegalStateException 
                Platform.runLater(() ->{
                    // สร้างหน้าต่างแจ้งเตือน
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("แจ้งเตือนห้องซ่อมบำรุง");
                    alert.setHeaderText("ห้อง: " + MaintenanceRoomEvent.getRoom().getNumberRoom());
                    alert.setContentText(
                        "การเปลี่ยนแปลงนี้อาจมีผลกระทบต่อการจองของ:\n\n" +
                        "ลูกค้า : " + MaintenanceRoomEvent.getCustomer().getFullName() + "\n" +
                        "เบอร์โทรศัพท์ : " + MaintenanceRoomEvent.getCustomer().getPhone() + "\n" +
                        "กำหนดการเช็คอิน: " + MaintenanceRoomEvent.getTimeCheckin().toString()
                    );
                    // แสดงหน้าต่างและรอให้ผู้ใช้กดปิด
                    alert.showAndWait();
                });
            
        }
    }
}

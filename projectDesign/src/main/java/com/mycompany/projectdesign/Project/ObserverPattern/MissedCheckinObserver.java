package com.mycompany.projectdesign.Project.ObserverPattern;

import com.mycompany.projectdesign.Project.Model.Bookings;

import javafx.application.Platform;
import javafx.scene.control.Alert;


/**
 * เป็น Concrete Observer ที่ทำหน้าที่แสดงหน้าต่างแจ้งเตือน (UI Alert)
 * เมื่อได้รับเหตุการณ์เกี่ยวกับลูกค้าที่ยังไม่มาเช็คอิน(MissedCheckinEvent) แจ้งเตือนก่อนถึงเวลา checkin 10 นาที 
 */

public class MissedCheckinObserver implements HotelObserver {
    /**
     * เมธอดที่จะถูกเรียกโดย Subject เมื่อมีเหตุการณ์ใหม่เกิดขึ้น
     * @param event ออบเจกต์ของเหตุการณ์ที่เกิดขึ้น
     */
    
    public void update(HotelEvent event) {

        // Observer นี้จะทำงานเฉพาะเมื่อเหตุการณ์ที่ได้รับเป็น MissedCheckinEvent เท่านั้น
        if (event instanceof MissedCheckinEvent) {
            Bookings missedEvent = ((MissedCheckinEvent) event).getBookings();
            
            // ใช้ Platform.runLater เพื่อให้แน่ใจว่าการอัปเดต UI (การแสดง Alert)
            // จะถูกประมวลผลบน JavaFX Application Thread ซึ่งเป็น Thread ที่ถูกต้อง
            // และป้องกันการเกิด IllegalStateException 
                Platform.runLater(() ->{
                    // สร้างหน้าต่างแจ้งเตือน
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("ตรวจสอบลูกค้าเช็คอิน");
                    alert.setHeaderText("ห้องที่จะมีการเช็คอินเร็ว ๆ นี้ : " + missedEvent.getRoom().getNumberRoom());
                    alert.setContentText(
                        "ลูกค้า : " + missedEvent.getCustomer().getFullName() + "\n" +
                        "เบอร์โทรศัพท์ : " + missedEvent.getCustomer().getPhone() + "\n" +
                        "กำหนดเวลาที่เช็คอิน : " + missedEvent.getTimeCheckin().toString()
                    );
                    // แสดงหน้าต่างและรอให้ผู้ใช้กดปิด
                    alert.showAndWait();
                });
            
        }
        
        /*if (event instanceof MissedCheckinEvent) {
            MissedCheckinEvent missedEvent = (MissedCheckinEvent) event;
            Customer customer = missedEvent.getCustomer();
            Room room = missedEvent.getRoom();
            Bookings bookings = missedEvent.getBookings();

            LocalDateTime now = LocalDateTime.now();

            //แปลง String เวลา ให้เป็น LocalDateTime
            LocalDateTime checkIn = LocalDateTime.of(bookings.getDateCheckin(), bookings.getTimeCheckin());

            //เวลาเป็นนาทีที่เหลือ
            long minutesCheckin = ChronoUnit.MINUTES.between(now, checkIn);

            if (minutesCheckin <= 10) {
                System.out.println("ALERT TO STAFF!!!");
                System.out.println("TIME ALERT : " + missedEvent.getFormattedTimestamp());
                System.out.println("ACTION: Please call customer who missed checkin.");
                System.out.println("  Room: " + room.getNumberRoom());
                System.out.println("  Customer: " + customer.getFullName());
                System.out.println("  Phone: " + customer.getPhone());
                System.out.println("=============================\n");  
            }
              
        }*/
    }
}

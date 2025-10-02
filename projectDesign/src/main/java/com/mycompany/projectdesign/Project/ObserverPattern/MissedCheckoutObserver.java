package com.mycompany.projectdesign.Project.ObserverPattern;


import com.mycompany.projectdesign.Project.Model.*;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * เป็น Concrete Observer ที่ทำหน้าที่แสดงหน้าต่างแจ้งเตือน (UI Alert)
 * เมื่อได้รับเหตุการณ์เกี่ยวกับลูกค้าที่ยังไม่มาเช็คเอาท์ (MissedCheckoutEvent) แจ้งเตือนก่อนถึงเวลา checkout 5 นาที
 */
public class MissedCheckoutObserver implements HotelObserver {
    
    /**
     * เมธอดที่จะถูกเรียกโดย Subject เมื่อมีเหตุการณ์ใหม่เกิดขึ้น
     * @param event ออบเจกต์ของเหตุการณ์ที่เกิดขึ้น
     */
    @Override
    public void update(HotelEvent event) {
        // Observer นี้จะทำงานเฉพาะเมื่อเหตุการณ์ที่ได้รับเป็น MissedCheckoutEvent เท่านั้น
        if (event instanceof MissedCheckoutEvent) {
            Bookings missedEvent = ((MissedCheckoutEvent) event).getBookings();

            // ใช้ Platform.runLater เพื่อให้แน่ใจว่าการอัปเดต UI (การแสดง Alert)
            // จะถูกประมวลผลบน JavaFX Application Thread ซึ่งเป็น Thread ที่ถูกต้อง
                Platform.runLater(() ->{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("ตรวจสอบลูกค้าเช็คเอาส์");
                    alert.setHeaderText("ห้องที่จะมีการเช็คเอาส์เร็ว ๆ นี้ : " + missedEvent.getRoom().getNumberRoom());
                    alert.setContentText(
                        "ลูกค้า : " + missedEvent.getCustomer().getFullName() + "\n" +
                        "เบอร์โทรศัพท์ : " + missedEvent.getCustomer().getPhone() + "\n" +
                        "กำหนดเวลาที่เช็คเอาส์ : " + missedEvent.getTimeCheckout().toString()
                    );

                    // แสดงหน้าต่างและรอให้ผู้ใช้กดปิด
                    alert.showAndWait();
                });
            }
        }

        /*if (event instanceof MissedCheckoutEvent) {
            MissedCheckoutEvent missedEvent = (MissedCheckoutEvent) event;
            Customer customer = missedEvent.getCustomer();
            Room room = missedEvent.getRoom();
            Bookings bookings = missedEvent.getBookings();

            LocalDateTime now = LocalDateTime.now();

            //แปลง String เวลา ให้เป็น LocalDateTime
            LocalDateTime checkOut = LocalDateTime.of(bookings.getDateCheckout(), bookings.getTimeCheckout());

            //เวลาเป็นนาทีที่เหลือ
            long minutesCheckout = ChronoUnit.MINUTES.between(now, checkOut);

            if (minutesCheckout <= 5) {
                System.out.println("ALERT TO STAFF!!!");
                System.out.println("TIME ALERT : " + missedEvent.getFormattedTimestamp());
                System.out.println("ACTION: Please call customer who missed checkout.");
                System.out.println("  Room: " + room.getNumberRoom());
                System.out.println("  Customer: " + customer.getFullName());
                System.out.println("  Phone: " + customer.getPhone());
                System.out.println("=============================\n");  
            }
              
        }*/
    }


    

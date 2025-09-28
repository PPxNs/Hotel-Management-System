package com.mycompany.projectdesign.Project.ObserverPattern;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.mycompany.projectdesign.Project.Model.*;

import javafx.application.Platform;
import javafx.scene.control.Alert;
public class MissedCheckoutObserver implements HotelObserver {
    //แจ้งเตือนก่อนถึงเวลา checkout 5 นาที
    @Override
    public void update(HotelEvent event) {
        if (event instanceof MissedCheckoutEvent) {
            Bookings missedEvent = ((MissedCheckoutEvent) event).getBookings();
            LocalDateTime checkout = LocalDateTime.of(missedEvent.getDateCheckout(), missedEvent.getTimeCheckout());
            long minutesCheckout = ChronoUnit.MINUTES.between(LocalDateTime.now(), checkout);

            if (minutesCheckout <=5 && minutesCheckout >=0) {
                Platform.runLater(() ->{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("ตรวจสอบลูกค้าเช็คเอาส์");
                    alert.setHeaderText("ห้องที่จะมีการเช็คเอาส์เร็ว ๆ นี้ : " + missedEvent.getRoom().getNumberRoom());
                    alert.setContentText(
                        "ลูกค้า : " + missedEvent.getCustomer().getFullName() + "\n" +
                        "เบอร์โทรศัพท์ : " + missedEvent.getCustomer().getPhone() + "\n" +
                        "กำหนดเวลาที่เช็คเอาส์ : " + missedEvent.getTimeCheckin().toString()
                    );
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
}

    

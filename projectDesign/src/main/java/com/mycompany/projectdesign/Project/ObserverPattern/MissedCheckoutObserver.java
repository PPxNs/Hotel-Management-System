package com.mycompany.projectdesign.Project.ObserverPattern;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.mycompany.projectdesign.Project.Model.*;
public class MissedCheckoutObserver implements HotelObserver {
    //แจ้งเตือนก่อนถึงเวลา checkout 5 นาที
    @Override
    public void update(HotelEvent event) {
        if (event instanceof MissedCheckoutEvent) {
            MissedCheckoutEvent missedEvent = (MissedCheckoutEvent) event;
            Customer customer = missedEvent.getCustomer();
            Room room = missedEvent.getRoom();

            LocalDateTime now = LocalDateTime.now();

            //แปลง String เวลา ให้เป็น LocalDateTime
            LocalDateTime checkOut = LocalDateTime.of(customer.getDateCheckout(), customer.getTimeCheckout());

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
              
        }
    }
}

    

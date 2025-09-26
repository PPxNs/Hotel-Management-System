package com.mycompany.projectdesign.Project.ObserverPattern;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.Model.Customer;
import com.mycompany.projectdesign.Project.Model.Room;

public class MissedCheckinObserver implements HotelObserver {
    
    //แจ้งเตือนก่อนถึงเวลา checkin 10 นาที
    public void update(HotelEvent event) {
        if (event instanceof MissedCheckinEvent) {
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
              
        }
    }
}

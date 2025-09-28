package com.mycompany.projectdesign.Project.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.mycompany.projectdesign.Project.Model.BookingRepository;
import com.mycompany.projectdesign.Project.Model.BookingStatus;
import com.mycompany.projectdesign.Project.Model.Bookings;
import com.mycompany.projectdesign.Project.ObserverPattern.HotelEventManager;
import com.mycompany.projectdesign.Project.ObserverPattern.MissedCheckinEvent;
import com.mycompany.projectdesign.Project.ObserverPattern.MissedCheckoutEvent;

public class BookingScheduler {
    
    private final BookingRepository bookingRepository = BookingRepository.getInstance();
    private final HotelEventManager eventManager = HotelEventManager.getInstance();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public BookingScheduler(){}

    public void start(){
        //ให้เช็คว่ามีอะไรต้องแจ้งเตือนมั้ยทุก ๆ 1 นาที
        scheduler.scheduleAtFixedRate(this :: runChecks, 0, 1, TimeUnit.MINUTES);
    }

    public void stop(){
        scheduler.shutdown();
    }

    private void runChecks(){
        LocalDateTime now = LocalDateTime.now();
        for(Bookings booking : bookingRepository.getAllBookings()){
            LocalDateTime checkinTime = booking.getDateCheckin().atTime(booking.getTimeCheckin());
            LocalDateTime checkoutTime = booking.getDateCheckout().atTime(booking.getTimeCheckout());
            
            // วางแผนว่าให้โทรเช็คคนจองก่อน 10 นาที
            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                if (now.isAfter(checkinTime.minusMinutes(10))&& now.isBefore(checkinTime)) {
                    MissedCheckinEvent event = new MissedCheckinEvent(booking.getRoom(), booking.getCustomer(), booking , now);
                    eventManager.notifyObserver(event);
                }   
                
                //ถ้าเลยเวลาเช็คอินก็ยกเลิกออโต้
                if (now.isAfter(checkinTime)) {
                    booking.setStatus(BookingStatus.CANCELLED);
                    bookingRepository.saveBookingToCSV();
                    
                    //แจ้งย้ำในช่องกระดิ่ง
                }
            }

            // วางแผนว่าให้โทรเช็คคน checkout ก่อน 5 นาที
            if (booking.getStatus() == BookingStatus.CHECKED_IN) {
                if (now.isBefore(checkoutTime.minusMinutes(5))&& now.isBefore(checkoutTime)) {
                    MissedCheckoutEvent event = new MissedCheckoutEvent(booking.getRoom(), booking.getCustomer(), booking, now);
                    eventManager.notifyObserver(event);
                }
            }
        }
    }
}

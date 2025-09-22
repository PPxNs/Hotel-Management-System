package com.mycompany.projectdesign.Project.ObserverPattern;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.DepositRoom;
import com.mycompany.projectdesign.Project.Model.*;
import com.mycompany.projectdesign.Project.StrategyPattern.*;

public class BillObserver implements HotelObserver{

    @Override
    public void update(HotelEvent event) {

        if (event instanceof BillEvent) {
        BillEvent billevent = (BillEvent) event;
            
        Room room = billevent.getRoom();
        Customer customer = billevent.getCustomer();
        DepositRoom deposit = billevent.getDepositRoom();

        System.out.println("===== HOTEL BILL =====");
        System.out.println("Time Print: " + billevent.getFormattedTimestamp());
        System.out.println("Number Room: " + room.getNumberRoom());
        System.out.println("Type: " + room.getType());
        System.out.println("Booking: " + customer.getDateBooking() + " " + customer.getTimeBooking());
        System.out.println("Check-in: " + customer.getDateCheckin() + " " + customer.getTimeCheckin()) ;
        System.out.println("Check-out: " + customer.getDateCheckout() + " " + customer.getTimeCheckout());
        System.out.println("----------------------");


        System.out.println("Room Price: " + room.getPrice()); 

        DiscountStrategy discountStrategy = DiscountSelector.getStrategy(room, customer);
        HotelCalculator calculator = new HotelCalculator();
        double finalPriceRoom = calculator.calculateFinalPrice(room, customer, discountStrategy);
        double discount = room.getPrice() - finalPriceRoom ;
        System.out.printf("Discount: %.2f\n" ,discount);
        System.out.println("Deposit & Services Add-on: " );
        System.out.println(" " + deposit.getDescription());
        System.out.printf("Deposit & Services Add-on Price: %.2f\n", deposit.getCost());

        System.out.printf("Total Price: %.2f\n", room.getPrice() + deposit.getCost()-discount);  
        System.out.println("Payment Method: Cash Payment");
        System.out.println("======================\n");

        }
    }    
}


    
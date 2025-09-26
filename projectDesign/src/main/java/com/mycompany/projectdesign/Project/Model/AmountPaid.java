package com.mycompany.projectdesign.Project.Model;

public class AmountPaid {
    private final Bookings booking ;
    private final double amountPaid ;

    public AmountPaid(Bookings booking, Double amount ){
        this.booking = booking;
        this.amountPaid = amount;
    }

    public Bookings getBooking(){
        return booking;
    }

    public Double getAmount(){
        return amountPaid;
    }

}

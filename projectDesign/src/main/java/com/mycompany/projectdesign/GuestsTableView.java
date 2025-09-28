package com.mycompany.projectdesign;

import com.mycompany.projectdesign.Project.Model.*;

import javafx.beans.property.*;

public class GuestsTableView {
    private final StringProperty numberRoom;
    private final StringProperty idCard;
    private final StringProperty bookingID;
    private final StringProperty firstName ;
    private final StringProperty lastName ;
    private final StringProperty checkin;
    private final StringProperty checkout;
    private final StringProperty status;
    private final StringProperty email ;
    private final StringProperty phone ;
    private final StringProperty gender ;
    private final StringProperty address ;
    private final StringProperty city ;
    private final StringProperty country ;

    

    // เก็บ object ต้นฉบับไว้ด้วย
    private final Room room;
    private final Customer customer;
    private final Bookings bookings;

    public GuestsTableView(Room room, Customer customer, Bookings bookings) {
        this.room = room;
        this.customer = customer;
        this.bookings = bookings;
        this.bookingID = new SimpleStringProperty(bookings.getBookingID());
        this.numberRoom = new SimpleStringProperty(room.getNumberRoom());
        this.idCard = new SimpleStringProperty(customer.getidCard());
        this.firstName = new SimpleStringProperty(customer.getFirstnameCustomer());
        this.lastName = new SimpleStringProperty(customer.getLastnameCustomer());
        this.checkin = new SimpleStringProperty(bookings.getCheckin());
        this.checkout = new SimpleStringProperty(bookings.getCheckOut());
        this.status = new SimpleStringProperty(bookings.getStatus().name());
        this.email = new SimpleStringProperty(customer.getEmail());
        this.phone = new SimpleStringProperty(customer.getPhone());
        this.gender = new SimpleStringProperty(customer.getGender());
        this.address = new SimpleStringProperty(customer.getAddress());
        this.city = new SimpleStringProperty(customer.getCity());
        this.country = new SimpleStringProperty(customer.getCountry());

    }

    //มันเป็น StringProperty จะต้องใช้ .get
    public String getBookingID() { return bookingID.get();}
    public String getNumberRoom() { return numberRoom.get(); }
    public String getIdCard() { return idCard.get(); }
    public String getFirstName() { return firstName.get(); }
    public String getLastName() { return lastName.get(); }    
    public String getCheckin() { return checkin.get(); }
    public String getCheckout() { return checkout.get(); }
    public String getStatus() { return status.get(); }
    public String getEmail() { return email.get(); }
    public String getPhone() { return phone.get(); }
    public String getGender() { return gender.get(); }    
    public String getAddress() { return address.get(); }
    public String getCity() { return city.get(); }
    public String getCountry() { return country.get(); }
    
    public void setFirstName(String firstName) { this.firstName.set(firstName); }
    public void setLastName(String lastName) { this.lastName.set(lastName); }    
    public void setEmail(String email) { this.email.set(email); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public void setGender(String gender) { this.gender.set(gender); }    
    public void setAddress(String address) { this.address.set(address); }
    public void setCity(String city) { this.city.set(city); }
    public void setCountry(String country) { this.country.set(country); }   

    // getter object จริง
    public Room getRoom() { return room; }
    public Customer getCustomer() { return customer; }
    public Bookings getBookings(){ return bookings;}
}

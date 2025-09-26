package com.mycompany.projectdesign.Project.ObserverPattern;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mycompany.projectdesign.Project.FactoryMethodPattern.DepositRoom;
import com.mycompany.projectdesign.Project.Model.*;
import com.mycompany.projectdesign.Project.StrategyPattern.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

public class BillObserver implements HotelObserver{

    @Override
    public void update(HotelEvent event) {

        if (event instanceof BillEvent) {
        BillEvent billevent = (BillEvent) event;
            
        Room room = billevent.getRoom();
        Bookings bookings = billevent.getBookings();
        DepositRoom deposit = billevent.getDepositRoom();

        DiscountStrategy discountStrategy = DiscountSelector.getStrategy(room, bookings);
        HotelCalculator calculator = new HotelCalculator();
        double finalPriceRoom = calculator.calculateFinalPrice(room, bookings, discountStrategy);
        double discount = room.getPrice() - finalPriceRoom ;  
        double totalPrice = finalPriceRoom + deposit.getCost();
        
        String filename = "bill_" + bookings.getBookingID().replace(":", "-") + ".pdf";

                try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filename));
                document.open();
                
                Paragraph title = new Paragraph("HOTEL BILL");
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                document.add(new Paragraph("Time Print: " + billevent.getFormattedTimestamp()));
                document.add(new Paragraph("Customer: " + bookings.getCustomer().getFullName()));
                document.add(new Paragraph("Room: " + room.getNumberRoom() + " (" + room.getType() + ")"));
                document.add(new Paragraph("Booking: " + bookings.getDateBooking() + " " + bookings.getTimeBooking()));
                document.add(new Paragraph("Check-in: " + bookings.getDateCheckin() + " " + bookings.getTimeCheckin()));
                document.add(new Paragraph("Check-out: " + bookings.getDateCheckout() + " " + bookings.getTimeCheckout()));
                document.add(new Paragraph("------------------------------"));

                document.add(new Paragraph(String.format("Room Price: %.2f", room.getPrice())));
                document.add(new Paragraph(String.format("Discount: %.2f", discount)));
                document.add(new Paragraph("Deposit: " + deposit.getDescription()));
                document.add(new Paragraph(String.format("Deposit Price: %.2f", deposit.getCost())));
                document.add(new Paragraph("------------------------------"));

                
                document.add(new Paragraph(String.format("TOTAL: %.2f", totalPrice)));
                document.add(new Paragraph("\nPayment Method: Cash Payment"));
                document.close();

                System.out.println("บิลถูกสร้างเรียบร้อย: " + filename);

            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }

        //เวอร์ซ้อมปรินต์เทส
        /*System.out.println("===== HOTEL BILL =====");
        System.out.println("Time Print: " + billevent.getFormattedTimestamp());
        System.out.println("Number Room: " + room.getNumberRoom());
        System.out.println("Type: " + room.getType());
        System.out.println("Booking: " + bookings.getDateBooking() + " " + bookings.getTimeBooking());
        System.out.println("Check-in: " + bookings.getDateCheckin() + " " + bookings.getTimeCheckin()) ;
        System.out.println("Check-out: " + bookings.getDateCheckout() + " " + bookings.getTimeCheckout());
        System.out.println("----------------------");


        System.out.println("Room Price: " + room.getPrice()); 


        System.out.printf("Discount: %.2f\n" ,discount);
        System.out.println("Deposit & Services Add-on: " );
        System.out.println(" " + deposit.getDescription());
        System.out.printf("Deposit & Services Add-on Price: %.2f\n", deposit.getCost());

        System.out.printf("Total Price: %.2f\n", room.getPrice() + deposit.getCost()-discount);  
        System.out.println("Payment Method: Cash Payment");
        System.out.println("======================\n");*/

            }
        }    
}



    
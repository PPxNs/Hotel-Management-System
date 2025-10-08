package com.mycompany.projectdesign.Project.ObserverPattern;

import java.io.IOException;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.DepositRoom;
import com.mycompany.projectdesign.Project.Model.*;
import com.mycompany.projectdesign.Project.StrategyPattern.*;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * เป็น Concrete Observer ที่ทำหน้าที่สร้างใบเสร็จ (Bill) ในรูปแบบไฟล์ PDF
 * คลาสนี้จะทำงานเมื่อได้รับแจ้งเหตุการณ์ (Event) ที่เป็นประเภท BillEvent
 * โดยจะดึงข้อมูลจาก Event มาคำนวณราคาสุทธิและสร้างเป็นเอกสาร PDF
 */
public class BillObserver implements HotelObserver {

    /**
     * เมธอดที่จะถูกเรียกโดย Subject เมื่อมีเหตุการณ์ใหม่เกิดขึ้น
     * @param event ออบเจกต์ของเหตุการณ์ที่เกิดขึ้น
     */
    @Override
    public void update(HotelEvent event) {
        // Observer นี้จะทำงานเฉพาะเมื่อเหตุการณ์ที่ได้รับเป็น BillEvent เท่านั้น
        if (event instanceof BillEvent) {
            BillEvent billevent = (BillEvent) event;

            // ดึงข้อมูลที่จำเป็นทั้งหมดจาก Event Object
            Room room = billevent.getRoom();
            Bookings bookings = billevent.getBookings();
            DepositRoom deposit = billevent.getDepositRoom();

            //ใช้ Strategy Pattern เพื่อคำนวณราคาส่วนลดของห้องพัก
            DiscountStrategy discountStrategy = DiscountSelector.getStrategy(room, bookings);
            HotelCalculator calculator = new HotelCalculator();

            //คำนวณยอดต่าง ๆ เพื่อแสดงในบิล
            double finalPriceRoom = calculator.calculateFinalPrice(room, bookings, discountStrategy);

            //ใช้ Decorator Pattern เพื่อดึงยอดรวมของเงินมัดจำและบริการเสริม
            double discount = room.getPrice() - finalPriceRoom;
            double totalPrice = finalPriceRoom + deposit.getCost();

            // === ส่วนใหม่: เตรียมข้อมูลเพื่อเติมลงในเทมเพลต ===
            Map<String, String> data = new HashMap<>();

            // Key ของ Map ต้องตรงกับ "Name" ของฟิลด์ในไฟล์ PDF Template
            data.put("booking_id", bookings.getBookingID());
            data.put("booking_date", billevent.getFormattedTimestamp()); 
            data.put("check_in", bookings.getDateCheckin() + " " + bookings.getTimeCheckin());
            data.put("check_out", bookings.getDateCheckout() + " " + bookings.getTimeCheckout());

            data.put("customer_name", bookings.getCustomer().getFullName());
            data.put("customer_tel", bookings.getCustomer().getPhone() != null ? bookings.getCustomer().getPhone() : "");
            data.put("customer_address", bookings.getCustomer().getAddress() != null ? bookings.getCustomer().getAddress() : "");

            data.put("room_number", room.getNumberRoom());
            data.put("room_type", room.getType());
            data.put("room_price", String.format("%.2f บาท/คืน", room.getPrice()));

            data.put("deposit_amount", String.format("%.2f", deposit.getCost()));
            data.put("special_service", deposit.getDescription());

            // คำนวณราคารวมก่อนหักส่วนลด
            double totalBeforeDiscount = room.getPrice() + deposit.getCost();
            data.put("price_amount", String.format("%.2f", totalBeforeDiscount));
            data.put("price_discount", String.format("%.2f", discount));

            // grand_total คือราคาสุทธิที่ต้องจ่าย
            data.put("total_charge", String.format("%.2f บาท", totalPrice)); 


        // === นี่คือโค้ดส่วนสร้าง PDF (ฉบับแก้ไขและตรวจสอบปัญหา) ===
            try {
                String templatePath = "/PDF/billv2.pdf";

                // --- ส่วนแก้ไขที่แนะนำและแก้ปัญหาได้แน่นอน ---
                
                // 1. สร้างโฟลเดอร์ชื่อ "GeneratedBills" ที่ Root ของโปรเจกต์
                java.io.File outputDir = new java.io.File("GeneratedBills");
                if (!outputDir.exists()) {
                    System.out.println("กำลังสร้างโฟลเดอร์สำหรับเก็บใบเสร็จที่: " + outputDir.getAbsolutePath());
                    outputDir.mkdirs(); // mkdirs() จะสร้างโฟลเดอร์ให้ถ้ายังไม่มี
                }

                // 2. สร้างชื่อไฟล์ให้สมบูรณ์
                String fileName = "receipt_" + bookings.getBookingID().replaceAll("[:/]", "-") + ".pdf";
                java.io.File outputFile = new java.io.File(outputDir, fileName);
                
                // 3. ดึงตำแหน่งเต็มๆ (Absolute Path) ที่จะบันทึกไฟล์
                String outputPath = outputFile.getAbsolutePath();
                
                // พิมพ์ตำแหน่งที่จะบันทึกไฟล์ออกมาใน Console เพื่อให้เรารู้ว่าโปรแกรมทำงานถูกต้อง
                System.out.println("โปรแกรมกำลังพยายามบันทึกใบเสร็จที่ตำแหน่ง: " + outputPath);
                // --- สิ้นสุดส่วนแก้ไข ---


                try (InputStream templateInputStream = BillObserver.class.getResourceAsStream(templatePath)) {
                    if (templateInputStream == null) {
                        System.err.println("CRITICAL ERROR: ไม่พบไฟล์เทมเพลตใน resources: " + templatePath);
                        throw new IOException("Template file not found in resources: " + templatePath);
                    }

                    PdfDocument pdfDoc = new PdfDocument(new PdfReader(templateInputStream), new PdfWriter(outputPath));
                    PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        if (form.getField(entry.getKey()) != null) {
                            form.getField(entry.getKey()).setValue(entry.getValue());
                        }
                    }

                    form.flattenFields();
                    pdfDoc.close();

                    System.out.println("ใบเสร็จถูกสร้างเรียบร้อยแล้ว!");

                }
            } catch (IOException e) {
                System.err.println("เกิดข้อผิดพลาดร้ายแรงระหว่างการสร้าง PDF!");
                // พิมพ์รายละเอียดของ Error ทั้งหมดออกมาใน Console
                e.printStackTrace();
            }
        }
    }
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


    
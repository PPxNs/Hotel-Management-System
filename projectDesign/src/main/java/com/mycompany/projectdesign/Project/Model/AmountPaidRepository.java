package com.mycompany.projectdesign.Project.Model;

import java.io.*;
import java.util.*;

/**
 * เป็นคลาส Repository สำหรับจัดการข้อมูลการชำระเงิน (AmountPaid) ทั้งหมดในระบบ
 * ทำหน้าที่จัดเก็บและเชื่อมโยงข้อมูลการชำระเงินเข้ากับการจองแต่ละรายการ
 */

public class AmountPaidRepository {
    private static final AmountPaidRepository instance = new AmountPaidRepository();
    private Map<String, AmountPaid> amountPaid = new HashMap<>();    //สำหรับเก็บข้อมูลการชำระเงิน Key: Booking ID (String) Value: ออบเจกต์การชำระเงิน (AmountPaid)
    private BookingRepository bookingRepository; //สำหรับเข้าถึงข้อมูลการจอง 

    /**
     * Private Constructor เพื่อป้องกันการสร้างอินสแตนซ์จากภายนอก
     */
    private AmountPaidRepository(){}

    /**
     * ดึงอินสแตนซ์เดียวของ AmountPaidRepository
     * @return อินสแตนซ์ของ AmountPaidRepository
     */
    public static AmountPaidRepository getInstance(){
        return instance;
    }

    /**
     * ตั้งค่าเริ่มต้น
     * @param bookingRepo อินสแตนซ์ของ BookingRepository
     */
    public void initialize(BookingRepository bookingRepo) {
        this.bookingRepository = bookingRepo;
        loadAmountPaidFromCSV();
    }

    /**
     * เพิ่มข้อมูลการชำระเงินเข้าสู่ hash
     * @param amount ออบเจกต์การชำระเงินที่ต้องการเพิ่ม
     */
    public void addAmount(AmountPaid amount) {
        amountPaid.put(amount.getBooking().getBookingID(), amount);
    }

    /**
     * ดึงข้อมูลการชำระเงินจาก Booking ID
     * @param bookingID Booking ID ที่ต้องการค้นหา
     * @return ออบเจกต์ AmountPaid ที่พบ หรือ null ถ้าไม่พบ
     */
    public AmountPaid getAmountByBookingID(String bookingID) {
    for (AmountPaid amount : amountPaid.values()) {
        if (amount.getBooking().getBookingID().equals(bookingID)) {
            return amount;
        }
    }
        return null; 
    }

    /**
     * ดึงรายการข้อมูลการชำระเงินจาก Booking ID
     * เนื่องจากโครงสร้างข้อมูลปัจจุบันเก็บได้แค่ 1 การชำระเงินต่อ 1 Booking ID
     * ดังนั้น List ที่คืนค่ากลับไปจะมีสมาชิกได้มากที่สุดแค่ 1 ตัวเท่านั้น
     * @param bookingID Booking ID ที่ต้องการค้นหา
     * @return รายการของ AmountPaid ที่พบ (มีสมาชิก 0 หรือ 1 ตัว)
     */
    public List<AmountPaid> getAmountsByBookingID(String bookingID) {
        List<AmountPaid> result = new ArrayList<>();
        for (AmountPaid amount : amountPaid.values()) {
            if (amount.getBooking().getBookingID().equals(bookingID)) {
                result.add(amount);
        }
    }
        return result;
    }
    
    /**
     * ดึงข้อมูลการชำระเงินทั้งหมดที่อยู่ใน Repository
     * @return รายการการชำระเงินทั้งหมดในรูปแบบ ArrayList
     */
    public List<AmountPaid> getAllAmountPaid(){
        return new ArrayList<>(amountPaid.values());
    }

    /**
     * ตั้งค่าหรือเปลี่ยนแปลง BookingRepository 
     * @param bookingRepo อินสแตนซ์ใหม่ของ BookingRepository
     */
    public void setBookingRepository(BookingRepository bookingRepo) {
        this.bookingRepository = bookingRepo;
    }

    /**
     * บันทึกข้อมูลการจองทั้งหมดจาก hash ลงในไฟล์ CSV (เขียนทับไฟล์เดิม)
     * @throws IOException หากเกิดข้อผิดพลาดระหว่างการเขียนไฟล์
     */
    public void saveAmountPaidToCSV() {
    File fi = new File("File/AmountPaid.csv");
    try {
        // ถ้าโฟลเดอร์ยังไม่มี → สร้างขึ้นมา
        if (!fi.getParentFile().exists()) {
            fi.getParentFile().mkdirs();
        }
        // ถ้าไฟล์ยังไม่มี → สร้างไฟล์ว่าง
        if (!fi.exists()) {
            fi.createNewFile();
        }

        // เขียนไฟล์ทับใหม่ทุกครั้ง
        FileWriter fw = new FileWriter(fi, false);
        BufferedWriter bw = new BufferedWriter(fw);

        for (String key : amountPaid.keySet()) {
            AmountPaid amount = amountPaid.get(key);
            bw.write(amount.getBooking().getBookingID() + "," +
                     amount.getAmount());
            bw.newLine();
        }

        bw.close();
        fw.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}



    /**
    * โหลดข้อมูลการจองจากไฟล์ CSV เข้าสู่ hash
    * @throws IOException หากเกิดข้อผิดพลาดระหว่างการอ่านไฟล์
    */
   public boolean loadAmountPaidFromCSV() {
    File fi = new File("File/AmountPaid.csv");
    try {
        if (!fi.getParentFile().exists()) {
            fi.getParentFile().mkdirs();
        }
        if (!fi.exists()) {
            fi.createNewFile();
            System.out.println("สร้างไฟล์ AmountPaid.csv ใหม่ (ว่าง)");
            return false;
        }
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }

    FileReader fr = null;
    BufferedReader br = null;

    try {
        fr = new FileReader(fi);
        br = new BufferedReader(fr);
        String s;
        while ((s = br.readLine()) != null) {
            String[] parts = s.split(",");
            if (parts.length == 2) {
                String bookingID = parts[0];
                Double amountpaid = Double.parseDouble(parts[1]);
               

                Bookings bookings = this.bookingRepository.getBookingByID(bookingID);
                if (bookings != null) {
                    AmountPaid amount = new AmountPaid(bookings, amountpaid);
                    this.addAmount(amount);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return true;
}


}

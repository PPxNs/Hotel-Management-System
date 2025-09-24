package com.mycompany.projectdesign.Project.Model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class BookingRepository {

    private static final BookingRepository instance = new BookingRepository();
    private Map<String, Bookings> bookingByID = new HashMap<>();

    private CustomerRepository customerRepository;
    private RoomRepository roomRepository;

    private BookingRepository(){}
    
    public static BookingRepository getInstance(){
        return instance;
    }

    public void initialize(CustomerRepository customerRepo, RoomRepository roomRepo) {
        this.customerRepository = customerRepo;
        this.roomRepository = roomRepo;
        loadBookingFromCSV();
    }

    public void addBooking(Bookings booking){
        bookingByID.put(booking.getBookingID(), booking);
    }

    public Bookings getBookingByRoom(String roomNo) {
    for (Bookings booking : bookingByID.values()) {
        if (booking.getRoom().getNumberRoom().equals(roomNo)) {
            return booking;
        }
    }
        return null; 
    }

    public List<Bookings> getBookingsByRoom(String roomNo) {
        List<Bookings> result = new ArrayList<>();
        for (Bookings booking : bookingByID.values()) {
            if (booking.getRoom().getNumberRoom().equals(roomNo)) {
                result.add(booking);
        }
    }
        return result;
    }


    public Bookings getBookingByID(String bookinID){
        return bookingByID.get(bookinID);
    }

    public List<Bookings> getAllBookings(){
        return new ArrayList<>(bookingByID.values());
    }
    // เมธอดสำหรับตรวจสอบการจองที่ซ้อนทับกัน
    public boolean hasOverlappingBooking(Room room, LocalDate newCheckIn , LocalDate newCheckOut){
        for(Bookings existBookings : bookingByID.values()){
            boolean isActiveBooking = existBookings.getStatus() == BookingStatus.CONFIRMED || 
                                      existBookings.getStatus() == BookingStatus.CHECKED_IN;
            
            if (existBookings.getRoom().equals(room) && isActiveBooking) {
                if (newCheckIn.isBefore(existBookings.getDateCheckout()) && newCheckOut.isAfter(existBookings.getDateCheckin())) {
                    return true; // พบการจองที่ซ้อนทับ
                }
            }
        }
        return false ; //ไม่ทับกัน
    }

    public String generateNextBookingId() {
    String prefix = "LUNA";

    int maxId = bookingByID.values().stream()
        .map(Bookings::getBookingID)                     
        .filter(Objects::nonNull)                         
        .filter(id -> id.startsWith(prefix))             
        .map(id -> id.substring(prefix.length()))       
        .filter(num -> num.matches("\\d+"))            
        .mapToInt(Integer::parseInt)                      
        .max()
        .orElse(0);                                      

        int nextId = maxId + 1;
        return prefix + String.format("%016d", nextId);       
    }




    public void saveBookingToCSV() {
    File fi = new File("File/Booking.csv");
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

        for (String key : bookingByID.keySet()) {
            Bookings booking = bookingByID.get(key);
            bw.write(booking.getRoom().getNumberRoom() + "," +
                     booking.getCustomer().getidCard() + "," +
                     booking.getBookingID() + "," +
                     booking.getDateCheckin().toString() + "," +
                     booking.getTimeCheckin().toString() + "," +
                     booking.getDateCheckout().toString() + "," +
                     booking.getTimeCheckout().toString() + "," +
                     booking.getDateBooking().toString() + "," +
                     booking.getTimeBooking().toString() + "," +
                     booking.getStatus().name());
            bw.newLine();
        }

        bw.close();
        fw.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}



    //ดึงข้อมูลมาดึงข้อมูลของ csv เข้ามาเก็บใน hash
    // ค่อยเพิ่มเติมตรงจุด แจ้ง exception กรณีไม่เจอไฟล์
   public boolean loadBookingFromCSV() {
    File fi = new File("File/Booking.csv");
    try {
        if (!fi.getParentFile().exists()) {
            fi.getParentFile().mkdirs();
        }
        if (!fi.exists()) {
            fi.createNewFile();
            System.out.println("สร้างไฟล์ Room.csv ใหม่ (ว่าง)");
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
            if (parts.length == 10) {
                String numberRoom = parts[0];
                String idCard = parts[1];
                String bookingID = parts[2];
                LocalDate dateCheckin = LocalDate.parse(parts[3]);
                LocalTime timeCheckin = LocalTime.parse(parts[4]);
                LocalDate dateCheckout = LocalDate.parse(parts[5]);
                LocalTime timeCheckout = LocalTime.parse(parts[6]);
                LocalDate dateBooking = LocalDate.parse(parts[7]);
                LocalTime timeBooking = LocalTime.parse(parts[8]);
                BookingStatus status = BookingStatus.valueOf(parts[9]);

                Customer customer = this.customerRepository.getCustomerById(idCard);
                Room room = this.roomRepository.getRoom(numberRoom);
               
                if (customer != null && room != null) {
                    Bookings booking = new Bookings(room, customer, bookingID, dateCheckin, timeCheckin, dateCheckout, timeCheckout, dateBooking, timeBooking);
                    booking.setStatus(status);
                    this.addBooking(booking);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (br != null) br.close();
            if (fr != null) fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return true;
}


}

package com.mycompany.projectdesign.Project.Model;
import java.io.*;
import java.time.*;
import java.util.*;


public class BookingRepository {

    private static final BookingRepository instance = new BookingRepository();
    private Map<String, Bookings> bookingByID = new HashMap<>();

    private CustomerRepository customerRepository;  //สำหรับเข้าถึงข้อมูลลูกค้า
    private RoomRepository roomRepository;          //สำหรับเข้าถึงข้อมูลห้องพัก

    /**
     * Private Constructor เพื่อป้องกันการสร้างอินสแตนซ์จากภายนอก
     */
    private BookingRepository(){}

    /**
     * ดึงอินสแตนซ์เดียวของ BookingRepository
     * @return อินสแตนซ์ของ BookingRepository
     */
    public static BookingRepository getInstance(){
        return instance;
    }

    /**
     * ตั้งค่าเริ่มต้น
     * @param customerRepo อินสแตนซ์ของ CustomerRepository
     * @param roomRepo     อินสแตนซ์ของ RoomRepository
     */
    public void initialize(CustomerRepository customerRepo, RoomRepository roomRepo) {
        this.customerRepository = customerRepo;
        this.roomRepository = roomRepo;
        loadBookingFromCSV();
    }

    /**
     * เพิ่มการจองใหม่เข้าสู่ BookingRepository
     * @param booking ออบเจกต์การจองที่ต้องการเพิ่ม
     */
    public void addBooking(Bookings booking){
        bookingByID.put(booking.getBookingID(), booking);
    }

    /**
     * ค้นหาการจองที่ตรงกับหมายเลขห้องที่ระบุ
     * @param roomNo หมายเลขห้องที่ต้องการค้นหา
     * @return ออบเจกต์การจอง (Bookings) ที่พบ หรือ null ถ้าไม่พบ
     */
    public Bookings getBookingByRoom(String roomNo) {
    for (Bookings booking : bookingByID.values()) {
        if (booking.getRoom().getNumberRoom().equals(roomNo)) {
            return booking;
        }
    }
        return null; 
    }

    /**
     * ค้นหาการจองทุกรายการที่ตรงกับหมายเลขห้องที่ระบุ
     * @param roomNo หมายเลขห้องที่ต้องการค้นหา
     * @return รายการของการจอง (List<Bookings>) ทั้งหมดที่พบสำหรับห้องนั้น
     */
    public List<Bookings> getBookingsByRoom(String roomNo) {
        List<Bookings> result = new ArrayList<>();
        for (Bookings booking : bookingByID.values()) {
            if (booking.getRoom().getNumberRoom().equals(roomNo)) {
                result.add(booking);
        }
    }
        return result;
    }

    /**
     * ดึงข้อมูลการจองโดยตรงจาก Booking ID
     * @param bookinID Booking ID ที่ต้องการค้นหา 
     * @return ออบเจกต์การจอง (Bookings) ที่พบ หรือ null ถ้าไม่พบ
     */
    public Bookings getBookingByID(String bookinID){
        return bookingByID.get(bookinID);
    }

    /**
     * ดึงข้อมูลการจองทั้งหมดที่อยู่ใน BookingRepository
     * @return รายการการจองทั้งหมดในรูปแบบ ArrayList
     */
    public List<Bookings> getAllBookings(){
        return new ArrayList<>(bookingByID.values());
    }

    /**
     * ตรวจสอบว่าช่วงวันที่จองใหม่สำหรับห้องที่ระบุ ซ้อนทับกับการจองอื่นที่มีสถานะเป็น CONFIRMED หรือ CHECKED_IN หรือไม่
     * @param room        ห้องที่ต้องการตรวจสอบ
     * @param newCheckIn  วันที่เช็คอินของการจองใหม่
     * @param newCheckOut วันที่เช็คเอาท์ของการจองใหม่
     * @return true ถ้าพบการจองที่ซ้อนทับ, false ถ้าไม่พบ
     */
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

    /**
     * สร้าง Booking ID ใหม่ที่ไม่ซ้ำกับของเดิมในระบบ
     * @return Booking ID ใหม่ในรูปแบบ "LUNA" ตามด้วยตัวเลข 16 หลัก (เช่น LUNA0000000000000001)
     */
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

    /**
     * บันทึกข้อมูลการจองทั้งหมดจาก hash ลงในไฟล์ CSV (เขียนทับไฟล์เดิม)
     * @throws IOException หากเกิดข้อผิดพลาดระหว่างการเขียนไฟล์
     */
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
                     booking.getStatus().name()+ "," +
                     booking.isCheckinNotified() + "," + 
                     booking.isCheckoutNotified());
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
    public boolean loadBookingFromCSV() {
    File fi = new File("File/Booking.csv");
    try {
        if (!fi.getParentFile().exists()) {
            fi.getParentFile().mkdirs();
        }
        if (!fi.exists()) {
            fi.createNewFile();
            System.out.println("สร้างไฟล์ Booking.csv ใหม่ (ว่าง)");
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
        
            if (parts.length >= 10) { 
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

                    // เงื่อนไขนี้จะทำงานได้อย่างถูกต้องแล้ว
                    if (parts.length == 12) {
                        booking.setCheckinNotified(Boolean.parseBoolean(parts[10]));
                        booking.setCheckoutNotified(Boolean.parseBoolean(parts[11]));
                    }
                    
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

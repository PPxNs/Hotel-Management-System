package com.mycompany.projectdesign.Project.DecoratorPattern;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.*;


//มันยังมีความไม่ยืดหยุ่นในเรื่องของจำนวนคน
/**
 *  Concrete Decorators: บริการรับส่ง - บาท แก้ปัญหาด้วยการคิดแบบ เหมาต่อห้อง ห้องละ 300 บาท
 */


public class PickupServiceDecorator extends DepositDecorator{

    public PickupServiceDecorator(DepositRoom wrappedDepositRoom) {
        super(wrappedDepositRoom);
    }
   
    public String getDescription(){
        return super.getDescription() + "\n\n + Pickup Service = 300.00" ;
    }
    @Override
    public double getCost() {
         return super.getPrice() + 300.00;

    }
    
}

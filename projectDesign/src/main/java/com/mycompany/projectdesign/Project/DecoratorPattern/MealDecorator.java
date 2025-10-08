package com.mycompany.projectdesign.Project.DecoratorPattern;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.*;


//มันยังมีความไม่ยืดหยุ่นในเรื่องของจำนวนคน และจำนวนวันที่จะรับบริการเราค่อยห่อซ้ำถ้า 2 วัน
//ต้องมีการจำกัดการจำนวนวันที่กินได้อิงตามวันพัก
//ต้องมีลักษณะการบิการที่ยืดหยุ่นกว่านี้

/**
 *  Concrete Decorators: บริการมื้ออาหารบุฟเฟต์ 500 บาท / 1 ห้อง / วัน
 */

public class MealDecorator extends DepositDecorator {
    
    private int days;

    public MealDecorator(DepositRoom wrappedDepositRoom, int days ) {
        super(wrappedDepositRoom);
        this.days = days ;
    }

    public String getDescription(){
        return super.getDescription() + "\n + Meal (" + days + " days) = " + 500.0*days +"0" ; //พอดีอยากได้ทศนิยม 2 ตำแหน่ง

    }

    @Override
    public double getCost() {
       return super.getPrice() + 500.00 * days ;  
    }
    
    
}

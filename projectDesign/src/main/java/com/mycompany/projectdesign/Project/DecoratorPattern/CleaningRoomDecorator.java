package com.mycompany.projectdesign.Project.DecoratorPattern;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.*;

/**
 *  Concrete Decorators: บริการเสริมทำความสะอาดห้องระหว่างวันเป็นแบบเหมา 1000 บาท
 */

public class CleaningRoomDecorator extends DepositDecorator {


    public CleaningRoomDecorator(DepositRoom wrappedDepositRoom) {
        super(wrappedDepositRoom);
    }
    
    public String getDescription(){
        return super.getDescription() + "\n\n + Cleaning Room = 1,000.00";
    }

    @Override
    public double getCost() {
        return new BigDecimal(super.getPrice() + 1000.0).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    
}

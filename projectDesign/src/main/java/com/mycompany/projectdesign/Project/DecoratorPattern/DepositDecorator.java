package com.mycompany.projectdesign.Project.DecoratorPattern;
import com.mycompany.projectdesign.Project.FactoryMethodPattern.*;

/**
 * Abstract Decortor สำหรับห่อหุ้ม DepositRoom
 */
public abstract class DepositDecorator implements DepositRoom{
 
    protected DepositRoom wrappedDepositRoom;

    public DepositDecorator(DepositRoom wrappedDepositRoom){
        this.wrappedDepositRoom = wrappedDepositRoom ; 
    }

    public double getPrice(){
        return wrappedDepositRoom.getCost();
    }

    public String getDescription(){
        return wrappedDepositRoom.getDescription();
    }
}
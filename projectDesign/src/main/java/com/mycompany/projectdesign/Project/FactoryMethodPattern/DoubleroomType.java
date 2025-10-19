package com.mycompany.projectdesign.Project.FactoryMethodPattern;

/*
 * มัดจำของ Doubleroom
 */

public class DoubleroomType implements DepositRoom{


    @Override
    public String getDescription() {
       return "\nDeposit for the Double room = 1,500.00" ;
    }

    @Override
    public double getCost() {
        return 1500.00;
    }

   
    
}

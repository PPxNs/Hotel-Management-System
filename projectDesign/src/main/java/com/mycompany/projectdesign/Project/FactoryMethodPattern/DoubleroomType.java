package com.mycompany.projectdesign.Project.FactoryMethodPattern;

/*
 * มัดจำของ Doubleroom
 */

public class DoubleroomType implements DepositRoom{


    @Override
    public String getDescription() {
       return "Deposit for the Double room --> 1500.00" ;
    }

    @Override
    public double getCost() {
        return 1500.00;
    }

   
    
}

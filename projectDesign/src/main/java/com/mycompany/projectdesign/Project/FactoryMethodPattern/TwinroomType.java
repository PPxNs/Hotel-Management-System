package com.mycompany.projectdesign.Project.FactoryMethodPattern;

/*
 * มัดจำของ Twinroom
 */

public class TwinroomType implements DepositRoom{

    @Override
    public String getDescription() {
       return "\nDeposit for the Twin room = 1,000.00" ;
    }

    @Override
    public double getCost() {
        return 1000.00;
    }

    
    
}

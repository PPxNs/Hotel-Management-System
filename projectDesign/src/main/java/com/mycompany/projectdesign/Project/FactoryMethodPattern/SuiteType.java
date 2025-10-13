package com.mycompany.projectdesign.Project.FactoryMethodPattern;

/*
 * มัดจำของ Suite
 */

public class SuiteType implements DepositRoom{

    @Override
    public String getDescription() {
       return "\nDeposit for the suite = 3,000.00" ;
    }

    @Override
    public double getCost() {
        return 3000.00 ;
    }

    
    
}

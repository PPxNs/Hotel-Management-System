package com.mycompany.projectdesign.Project.StrategyPattern;

import com.mycompany.projectdesign.Project.Model.*;

//ให้มันสร้างโรงงานเลือกเอง
public class DiscountSelector {
    public static DiscountStrategy getStrategy(Room room, Customer customer) {
        
        if (customer.getDateStay() >= 2) {
            return new StayMoreDiscount();
        } else if (customer.getDaysBeforeCheckin() >= 30) {
            return new EarlyBirdDiscount();
        }
        return new DefaultDiscount();
    }
}

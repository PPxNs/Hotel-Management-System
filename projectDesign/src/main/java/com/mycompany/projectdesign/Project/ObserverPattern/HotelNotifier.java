package com.mycompany.projectdesign.Project.ObserverPattern;

import java.util.ArrayList;
import java.util.List;

public class HotelNotifier {
    private List<HotelObserver> observers = new ArrayList<>();

    public void register(HotelObserver observer){ observers.add(observer);}
    public void unregister(HotelObserver observer){ observers.remove(observer);}

    public void notifyObserver(HotelEvent event){
        for (HotelObserver observer : observers){
            observer.update(event);
        }
    }
    
}

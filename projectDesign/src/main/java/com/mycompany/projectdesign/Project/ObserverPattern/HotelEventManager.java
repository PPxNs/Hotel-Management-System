package com.mycompany.projectdesign.Project.ObserverPattern;

import java.util.ArrayList;
import java.util.List;

public class HotelEventManager {
    private final List<HotelObserver> observers = new ArrayList<>();
    private static final HotelEventManager instance = new HotelEventManager();
    
    private HotelEventManager(){}

    public static HotelEventManager getInstance(){
        return instance;
    }

    public void addObserver(HotelObserver observer){
        observers.add(observer);
    }

    public void removeObserver(HotelObserver observer){
        observers.remove(observer);
    }

    public void notifyObserver(HotelEvent event){
        for (HotelObserver observer : observers){
            observer.update(event);
        }
    }
}

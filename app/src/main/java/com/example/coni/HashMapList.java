package com.example.coni;

import java.util.HashMap;

public class HashMapList {

    private static HashMap<String, Double> ListOfCoordinates;


    static {
        ListOfCoordinates = new HashMap<>();
    }



    public static HashMap<String, Double> getListOf() {
        return ListOfCoordinates;
    }

    public static void setListOf(HashMap<String, Double> listOfCoordinates) {
        ListOfCoordinates = listOfCoordinates;
    }
}

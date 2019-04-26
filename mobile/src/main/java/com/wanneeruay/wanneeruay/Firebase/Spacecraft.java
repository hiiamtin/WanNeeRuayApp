package com.wanneeruay.wanneeruay.Firebase;

import java.util.ArrayList;

public class Spacecraft {
    String key;
    ArrayList<ArrayList<String>> value;

    public Spacecraft() {
        value = new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<ArrayList<String>> getValue() {
        return value;
    }

    public void setValue(ArrayList<ArrayList<String>> value) {
        this.value = value;
    }
    public void addValue(ArrayList<String> value){
        this.value.add(value);
    }

    @Override
    public String toString() {
        String s=key;
        if (value!=null){
            for (int i = 0; i < value.size(); i++) {
                s+=",";
                ArrayList<String> temp = value.get(i);
                for (int j = 1; j < temp.size(); j++) {
                    s+=",";
                    s+= temp.get(i);
                }
            }
        }
        return s;
    }
}

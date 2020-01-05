package com.example.attendance;

public class name {

    private String Name, Count, rollNo;

    public name(String count, String name, String rollNo) {
        this.Count = count;
        this.Name = name;
        this.rollNo = rollNo;
    }


    public String getCount() {
        return Count;
    }

    public String getName() {
        return Name;
    }

    public String getRollNo() {
        return rollNo;
    }
}

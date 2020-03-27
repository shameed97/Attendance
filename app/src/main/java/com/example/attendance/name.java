package com.example.attendance;

public class name {

    private String Name, Count, rollNo,Mobile;

    public name(String count, String name, String rollNo, String mobile) {
        this.Count = count;
        this.Name = name;
        this.rollNo = rollNo;
        Mobile = mobile;
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

    public String getMobile() {
        return Mobile;
    }
}

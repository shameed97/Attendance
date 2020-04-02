package com.example.attendance;

public class name {

    private String Name, Count, rollNo, Mobile, Hour;

    public name(String count, String name, String rollNo, String mobile, String hour) {
        this.Count = count;
        this.Name = name;
        this.rollNo = rollNo;
        this.Mobile = mobile;
        this.Hour = hour;
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

    public String getHour() {
        return Hour;
    }
}

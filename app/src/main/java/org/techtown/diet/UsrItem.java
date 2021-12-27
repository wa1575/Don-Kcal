package org.techtown.diet;

import java.util.UUID;

public class UsrItem {
    // num 추가!
    int num;
    String usrid;
    String date;
    int intake;
    double weight;
    int tot;

    public UsrItem(int _id, String date, int intake, double weight, int tot) {
        this.num = _id;
        this.usrid = UUID.randomUUID().toString();
        this.date = date;
        this.intake = intake;
        this.weight = weight;
        this.tot = tot;
    }

    public int getNum(){return num;}

    public void setNum(int num) {
        this.num = num;
    }

    public String getUsrid(){ return usrid; }

    public void setUsrid(String usrid) { this.usrid = usrid; }

    public double getWeight() { return weight; }

    public void setWeight(double weight) { this.weight = weight; }

    public int getTot() { return tot; }

    public void setTot(int resId) { this.tot = tot; }

    public int getIntake() { return intake; }

    public void setIntake(int intake) { this.intake = intake; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }
}

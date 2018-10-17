package com.outofbox.model;

public class HourlyUsage {
    //This should be in range of 0 to 23
    private int hourNumber;
    private int durationOfUsage;
    private int countOfEntry;

    public HourlyUsage(int hourNumber, int durationOfUsage, int countOfEntry) {
        this.hourNumber = hourNumber;
        this.durationOfUsage = durationOfUsage / 1000 / 60;
        this.countOfEntry = countOfEntry;
    }

    public HourlyUsage(int hourNumber) {
        this.hourNumber = hourNumber;
        this.durationOfUsage = 0;
        this.countOfEntry = 0;
    }

    public int getHourNumber() {
        return hourNumber;
    }

    public int getDurationOfUsage() {
        return durationOfUsage;
    }

    public int getCountOfEntry() {
        return countOfEntry;
    }
}

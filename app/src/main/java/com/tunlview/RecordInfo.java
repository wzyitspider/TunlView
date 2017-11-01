package com.tunlview;

public class RecordInfo {

    private long startTime;
    private long endTime;

    private int startValue;
    private int endValue;

    public int getStartValue() {
        return startValue;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
    }

    public int getEndValue() {
        return endValue;
    }

    public void setEndValue(int endValue) {
        this.endValue = endValue;
    }

    public RecordInfo() {
        super();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return " startTime:" + startTime + " endTime:" + endTime + " startValue:" + startValue + " endValue:" + endValue;
    }


}

package com.tunlview;

public class OneDayRecordInfo {

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

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return " startTime:" + startTime + " endTime:" + endTime +  " startValue:"+startValue+" endValue:"+endValue;
    }


    public OneDayRecordInfo() {
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

//    /**
//     * @return currentDate
//     */
//    public String getCurrentDate() {
//        return CurrentDate;
//    }
//
//    /**
//     * @param currentDate 要设置的 currentDate
//     */
//    public void setCurrentDate(String currentDate) {
//        CurrentDate = currentDate;
//    }


}

package com.example.paintit;

import java.util.Date;

public class Timer {
    Date start;
    Date end;
    public Timer (Date start, Date end){
        this.start = start;
        this.end = end;
    }
    public Timer (Date start){
        this.start = start;
        this.end = null;
    }
    public Timer (){
        this.start = null;
        this.end = null;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
    public int diffrence (){
        int diff = (int) (end.getTime() - start.getTime());
        return diff;
    }
    public long[] format (long diff){
        long[] time = new long[3];
        time[0] = diff/1000;
        time[1] = (time[0]/60)%60;
        time[2] = time[0]/3600;
        return time;
    }
}

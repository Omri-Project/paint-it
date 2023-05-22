package com.example.paintit;

import java.util.Date;

public class Timer {
    Date start;
    Date end;
    public Timer (Date start, Date end){
        this.start = start;
        this.end = end;
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
    public long diffrence (){
        long diff = end.getTime() - start.getTime();
        return diff;
    }
    public long[] format (long diff){
        long[] time = new long[4];
        time[0] = diff/1000%60;
        time[1] = diff/60000%60;
        time[2] = diff/3600000%24;
        time[3] = diff/86400000;
        return time;
    }
}

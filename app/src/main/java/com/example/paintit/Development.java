package com.example.paintit;

public class Development {
    private int paintingId;
    private long userId;
    private String coloredData;
    private int time;
    private int numClicked;

    public Development(int paintingId, long userId, String coloredData, int time, int numClicked) {
        this.paintingId = paintingId;
        this.userId = userId;
        this.coloredData = coloredData;
        this.time = time;
        this.numClicked = numClicked;
    }

    public int getPaintingId() {
        return paintingId;
    }

    public long getUserId() {
        return userId;
    }

    public String getColoredData() {
        return coloredData;
    }

    public void setPaintingId(int paintingId) {
        this.paintingId = paintingId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setColoredData(String coloredData) {
        this.coloredData = coloredData;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getNumClicked() {
        return numClicked;
    }

    public void setNumClicked(int numClicked) {
        this.numClicked = numClicked;
    }

    public int getTime() {
        return time;
    }
}


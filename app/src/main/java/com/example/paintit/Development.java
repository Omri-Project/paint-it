package com.example.paintit;

public class Development {
    private int paintingId;
    private int userId;
    private String coloredData;
    private String time;

    public Development(int paintingId, int userId, String coloredData, String time) {
        this.paintingId = paintingId;
        this.userId = userId;
        this.coloredData = coloredData;
        this.time = time;
    }

    public int getPaintingId() {
        return paintingId;
    }

    public int getUserId() {
        return userId;
    }

    public String getColoredData() {
        return coloredData;
    }

    public String getTime() {
        return time;
    }
}


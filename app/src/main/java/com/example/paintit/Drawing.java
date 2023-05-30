package com.example.paintit;

public class Drawing {
    private int id;
    private String name;
    private String pixels;
    private String colors;

    public Drawing(int id, String name, String pixels, String colors) {
        this.id = id;
        this.name = name;
        this.pixels = pixels;
        this.colors = colors;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPixels() {
        return pixels;
    }

    public String getColors() {
        return colors;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPixels(String pixels) {
        this.pixels = pixels;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }
}

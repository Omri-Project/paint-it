package com.example.paintit;

public class Drawing {
    private String name;
    private String difficulty;
    private boolean isDone;
    private int imgID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public int getImgID() {
        return imgID;
    }

    public void setImgID(int imgID) {
        this.imgID = imgID;
    }

    public Drawing (String name, int dif, int imgID){
        this.name = name;
        this.isDone = false;
        this.imgID = imgID;
        if (dif == 1){
            this.difficulty = "Easy";
        } else if (dif == 2){
            this.difficulty = "Medium";
        } else {
            this.difficulty = "Hard";
        }
    }
}

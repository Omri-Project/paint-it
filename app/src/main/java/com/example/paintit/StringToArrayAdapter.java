package com.example.paintit;

public class StringToArrayAdapter {
    public static String arrayToString(int[][] pixels) {
        StringBuilder array = new StringBuilder();
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                array.append(pixels[i][j]).append(",");
            }
            array.append(";");
        }
        return array.toString();
    }

    public static int[][] stringToArray(String array) {
        String[] rows = array.split(";");
        int numRows = rows.length;
        int numColumns = rows[0].split(",").length;

        int[][] result = new int[numRows][numColumns];
        for (int i = 0; i < numRows; i++) {
            String[] values = rows[i].split(",");
            for (int j = 0; j < numColumns; j++) {
                result[i][j] = Integer.parseInt(values[j]);
            }
        }

        return result;
    }
}



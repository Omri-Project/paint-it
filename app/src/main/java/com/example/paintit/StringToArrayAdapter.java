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
        int numColumns = 0;

        if (numRows > 0) {
            numColumns = rows[0].split(",").length;
        }

        int[][] result = new int[numRows][numColumns];
        for (int i = 0; i < numRows; i++) {
            String[] values = rows[i].split(",");
            for (int j = 0; j < numColumns; j++) {
                try {
                    result[i][j] = Integer.parseInt(values[j]);
                } catch (NumberFormatException e) {
                    // Handle any potential parsing errors here
                    // For example, you could set a default value or throw an exception
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    public static String[] stringToColorArray (String array) {
        int length = array.length()-array.replace(",", "").length();
        String[] colors = new String[length];
        int index = 0;
        for (int i = 0; i < colors.length; i++) {
            colors[i] = array.substring(index, array.indexOf(',', index));
            index +=colors[i].length()+1;
        } return colors;
    }

}



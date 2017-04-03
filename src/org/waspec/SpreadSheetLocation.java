package org.waspec;

public class SpreadSheetLocation {
    private int row;
    private int col;

    public SpreadSheetLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public SpreadSheetLocation(int row, char col) {
        col = Character.toLowerCase(col);
        this.row = row;
        this.col = col - 'a' + 1;
    }

    public SpreadSheetLocation(String cellName) {
        cellName = cellName.toLowerCase().trim();
        this.row = Integer.parseInt(cellName.substring(1, cellName.length()));
        this.col = cellName.charAt(0) - 'a' + 1;
    }

    public String locationString() {
        char colChar = (char) (col - 1 + 'A');//change the integer to char type
        return Character.toString(colChar) + Integer.toString(row);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

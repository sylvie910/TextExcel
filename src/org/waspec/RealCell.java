package org.waspec;

public class RealCell implements Cell {
    private double value;

    public RealCell(String input) {
        this.value = Double.parseDouble(input);
    }


    @Override
    public String abbreviatedCellText() {
        return SpreadSheet.printFormat(Double.toString(value));
    }

    @Override
    public String fullCellText() {
        if ((int) value != value) {
            return Double.toString(value);
        } else {
            return Integer.toString((int) value);
        }
    }

    @Override
    public String cellType() {
        return "RealCell";
    }
}

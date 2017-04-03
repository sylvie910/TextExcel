package org.waspec;

public class RealCell implements Cell {
    private double value;
    private String formula;

    public RealCell(String input) {
        this.value = Double.parseDouble(input);
    }

    // because we set the cell to be RealCell after the calculation and we want to keep
    //the original formula. So I add a new constructor here
    public RealCell(String input, String formula) {
        this.value = Double.parseDouble(input);
        this.formula= formula;
    }

    @Override
    public String abbreviatedCellText() {
        return SpreadSheet.printFormat(Double.toString(value));
    }

    @Override
    public String fullCellText() {
        //if the value the RealCell was calculated by formula, the fullCellText will be the formula
        if (formula!= null) {
            return formula;
        } else {
            if ((int) value != value) {
                return Double.toString(value);
            } else {
                return Integer.toString((int) value);
            }
        }
    }

    @Override
    public String cellType() {
        return "RealCell";
    }
}

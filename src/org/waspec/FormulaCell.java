package org.waspec;


public class FormulaCell implements Cell {
    private String formula;
    private SpreadSheet parentSheet;
    private SpreadSheetLocation location;

    public FormulaCell(String input, String location, SpreadSheet spreadSheet) {
        this.formula = input.substring(1, input.length() - 1).trim();
        this.parentSheet = spreadSheet;
        this.location = new SpreadSheetLocation(location);
    }

    private double getValue(String value) {
        double result = 0;
        if (parentSheet.isLocation(value)) {
            SpreadSheetLocation formulaLocation = new SpreadSheetLocation(value);
            result = Double.parseDouble(parentSheet.getCell(formulaLocation).fullCellText());
        } else {
            result = Double.parseDouble(value);
        }
        return result;
    }

    private String calculate() {
        double result = 0;
        int index = 0;
        double[] numbers = null;
        String[] elements = null;
        if (formula.startsWith("sum")) {
            elements = formula.split(" ", 2);
            numbers = getRangeValues(elements[1]);
            result = sum(numbers);
        } else if (formula.startsWith("avg")) {
            elements = formula.split(" ", 2);
            numbers = getRangeValues(elements[1]);
            result = avg(numbers);
        } else {
            elements = formula.split(" ");
            result = getValue(elements[0]);
            index++;
            while (index <= elements.length - 2) {
                if (elements[index].equals("+")) {
                    result += getValue(elements[index + 1]);
                } else if (elements[index].equals("-")) {
                    result -= getValue(elements[index + 1]);
                } else if (elements[index].equals("*")) {
                    result *= getValue(elements[index + 1]);
                } else if (elements[index].equals("/")) {
                    result /= getValue(elements[index + 1]);
                }
                index = index + 2;
            }
        }
        RealCell realCell = new RealCell(String.valueOf(result), "(" + formula + ")");
        parentSheet.sheet[location.getRow()][location.getCol()] = realCell;

        return String.valueOf(result);
    }

    private double[] getRangeValues(String range) {
        SpreadSheetLocation[] locations = parentSheet.getRange(range);
        if (locations == null) {
            return new double[0];
        }
        double[] result = new double[locations.length];
        for (int i = 0; i < locations.length; i++) {
            Cell cell = parentSheet.getCell(locations[i]);
            if (cell.cellType().equals("EmptyCell")) {
                result[i] = 0;
            } else {
                result[i] = Double.parseDouble(cell.fullCellText());
            }
        }
        return result;

    }

    private double sum(double[] numbers) {
        double sum = 0;
        for (int i = 0; i < numbers.length; i++) {
            sum += numbers[i];
        }
        return sum;
    }

    private double avg(double[] numbers) {
        if (numbers.length == 0) {
            return 0;
        }
        return sum(numbers) / numbers.length;
    }

    @Override
    public String abbreviatedCellText() {
        //return the value after the calculation
        //before we do this, we need to validate the formula
        return SpreadSheet.printFormat(calculate());
    }

    @Override
    public String fullCellText() {
        //return the content of the command(part 3)
        return "( " + formula + " )";
    }

    @Override
    public String cellType() {
        return "FormulaType";
    }
}

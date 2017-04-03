package org.waspec;

import java.util.ArrayList;

class SpreadSheet implements SerializableToString {
    private static final int ROWS = 11;
    private static final int COLS = 8;
    Cell[][] sheet = new Cell[ROWS][COLS];

    public SpreadSheet() {
        sheetInit();
    }

    private void sheetInit() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                sheet[i][j] = new EmptyCell();
            }
        }
        fillTableHeader();
    }

    private void fillTableHeader() {
        String[] rowHeader = {"A", "B", "C", "D", "E", "F", "G"};
        for (int i = 1; i < COLS; i++) {
            sheet[0][i] = new TextCell("\"" + rowHeader[i - 1] + "\"");
        }
        for (int i = 1; i < ROWS; i++) {
            sheet[i][0] = new TextCell("\"" + Integer.toString(i) + "\"");
        }
    }

    public void print() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(sheet[i][j].abbreviatedCellText());
                System.out.print("|");
            }
            System.out.println();
            for (int j = 0; j < COLS; j++) {
                for (int k = 0; k < 12; k++) {
                    System.out.print("-");
                }
                System.out.print("+");
            }
            System.out.println();
        }
    }

    public static String printFormat(String input) {
        String result = "";
        if (input.length() > 12) {
            input = input.substring(0, 11);
            input += ">";
        }
        for (int i = 0; i < (12 - input.length()) / 2; i++) {
            result += " ";
        }
        result += input;
        for (int i = 0; i < (12 - input.length() + 1) / 2; i++) {
            result += " ";
        }
        return result;
    }

    public String processCommand(String command) {
        command = command.trim();
        if (command.equals("")) {
            return "Invalid input";
        }
        if (command.startsWith("save") || command.startsWith("load")) {
            String[] parts = command.split(" ", 2);
            if (parts[0].equals("save")) {
                boolean saveStatus = FileHelper.save("D:\\" + parts[1], this);
                if (!saveStatus) {
                    System.out.println("Error when saving files");
                }
            } else if (parts[0].equals("load")) {
                boolean loadStatus = FileHelper.load("D:\\" + parts[1], this);
                if (!loadStatus) {
                    System.out.println("Cannot load this file, file doesn't exist");
                }
            }
            return "";
        } else if (command.startsWith("sorta") || command.startsWith("sortd")) {
            String[] parts = command.split(" ", 2);
            if (parts[0].equals("sorta")) {
                //TODO: have the sorting method
                sortCommand(parts[1], true);
            } else if (parts[0].equals("sortd")) {
                sortCommand(parts[1], false);
                //TODO: have the sorting method
            }
        } else {
            String[] parts = command.split(" ", 3);
            if (parts.length == 1) {
                if (parts[0].equals("print")) {
                    print();
                    return "";
                } else if (parts[0].equals("clear")) {
                    clear(null);
                    return "";
                } else {
                    SpreadSheetLocation location = new SpreadSheetLocation(command);
                    if (location.getRow() < 0 || location.getRow() > 10 || location.getCol() < 0 || location.getCol() > 7) {
                        return "The location is out of range";
                    } else {
                        return command + " = " + this.getCell(location).fullCellText();
                    }
                }
            } else if (parts.length == 2 && parts[0].equals("clear")) {
                clear(new SpreadSheetLocation(parts[1]));
                return "";
            } else if (parts.length == 3 && parts[1].equals("=")) {
                setCell(new SpreadSheetLocation(parts[0]), parseCell(parts[2], parts[0]));
                return "";
            } else {
                return null;
            }
        }
        return "";
    }

    Cell getCell(SpreadSheetLocation location) {
        return sheet[location.getRow()][location.getCol()];
    }

    private String sortCommand(String range, boolean isAscending) {
        //range => list of cells
        SpreadSheetLocation[] locations = getRange(range);
        if (locations == null) {
            return "Invalid Range.";
        }
        //do the sorting => selection sort
        for (int i = 0; i < locations.length; i++) {
            //locations => numbers
            double limit = Double.parseDouble(getCell(locations[i]).fullCellText());
            SpreadSheetLocation limitLoc = locations[i];
            for (int j = i + 1; j < locations.length; j++) {
                double value =Double.parseDouble(getCell(locations[j]).fullCellText());
                if ((isAscending && value < limit) || (!isAscending && value > limit)) {
                    limit = value;
                    limitLoc = locations[j];
                    swap(locations[i], limitLoc);
                }
            }
        }
        return "";
    }

    private void swap (SpreadSheetLocation loc1, SpreadSheetLocation loc2) {
        //int temp = i;
        //i = j;
        //j = temp;

        Cell temp = getCell(loc1);
        setCell(loc1, getCell(loc2));
        setCell(loc2, temp);
    }

    SpreadSheetLocation[] getRange(String range) {
        String[] parts = range.split("-");
        if (parts.length != 2) {
            return null;
        }

        SpreadSheetLocation start = new SpreadSheetLocation(parts[0]);
        SpreadSheetLocation end = new SpreadSheetLocation(parts[1]);

        int startRow = Math.min(start.getRow(), end.getRow());
        int endRow = Math.max(end.getRow(), start.getRow());
        int startCol = Math.min(start.getCol(), end.getCol());
        int endCol = Math.max(start.getCol(), end.getCol());

        int length = (endRow - startRow + 1) * (endCol - startCol + 1);
        SpreadSheetLocation[] locations = new SpreadSheetLocation[length];

        for (int idx = 0, row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++, idx++) {
                locations[idx] = new SpreadSheetLocation(row, col);
            }
        }

        return locations;
    }

    //TODO 4 : write sortCells method
    //TODO 5 : write the swap method
    private Cell parseCell(String value, String location) {
        if (value.charAt(0) == '\"' && value.endsWith("\"")) {
            return new TextCell(value);
        } else if (value.startsWith("(") && value.endsWith(")")) {
            if (isValidFormula(value)) {
                return new FormulaCell(value, location, this);
            } else {
                System.out.println("Invalid Formula");
            }
        } else if (isDigit(value)) {
            return new RealCell(value);
        }
        return new EmptyCell();
    }


    public boolean isValidFormula(String value) {
        value = value.trim();
        value = value.substring(1, value.length() - 1).trim();
        String[] elements = value.split(" ");
        String start = elements[0];
        if (start.equals("sum") || start.equals("avg")) {
            if (elements.length != 4) {
                return false;
            }
            if (!elements[2].equals("-")) {
                return false;
            }
            if (!isLocation(elements[1]) || !isLocation(elements[3])) {
                return false;
            }
        } else {
            if (elements.length % 2 == 0) {
                return false;
            }
            if (!isLocationOrDigit(elements[0]) && !isLocationOrDigit(elements[elements.length - 1])) {
                return false;
            }
            for (int i = 1; i < elements.length - 1; i++) {
                if (!isLocationOrDigit(elements[i]) && !isOperation(elements[i])) {
                    return false;
                } else if (isLocationOrDigit(elements[i]) && isLocationOrDigit(elements[i - 1])) {
                    return false;
                } else if (isOperation(elements[i]) && isOperation(elements[i - 1])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isLocationOrDigit(String value) {
        return isDigit(value) || isLocation(value);
    }

    boolean isLocation(String value) {
        value = value.trim();
        if (value.length() < 2 || value.length() > 3) {
            return false;
        }
        if (value.charAt(0) - 'A' < 0 || value.charAt(0) - 'G' > 0) {
            return false;
        }

        String rowHeader = value.substring(1);
        if (!isDigit(rowHeader)) {
            return false;
        }
        int row = Integer.parseInt(rowHeader);
        if (row < 1 || row > 10) {
            return false;
        }
        return true;
    }

    private boolean isOperation(String element) {
        if (element.equals("+") || element.equals("-") || element.equals("*") || element.equals("/")) {
            return true;
        }
        return false;
    }

    private boolean isDigit(String value) {
        int dotCount = 0;
        if (value.charAt(0) == '-') {
            value = value.substring(1);
        }
        if (value.length() == 0) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i)) && value.charAt(i) != '.') {
                return false;
            } else if (value.charAt(i) == '.') {
                dotCount++;
            }
        }
        if (dotCount > 1) {
            return false;
        }
        return true;
    }

    private void setCell(SpreadSheetLocation location, Cell cell) {
        sheet[location.getRow()][location.getCol()] = cell;
    }

    private void clear(SpreadSheetLocation location) {
        if (location == null) {
            sheetInit();
        } else {
            setCell(location, new EmptyCell());
        }
    }


    @Override
    public ArrayList<String> serialize() {
        ArrayList<String> result = new ArrayList<>();
        for (int row = 1; row < ROWS; row++) {//begin from 1
            for (int col = 1; col < COLS; col++) {//begin from 1
                Cell cell = sheet[row][col];
                SpreadSheetLocation location = new SpreadSheetLocation(row, col);
                if (!cell.cellType().equals("EmptyCell")) {
                    result.add(location.locationString() + "," + cell.cellType() + "," + cell.fullCellText());
                }
            }
        }
        return result;
    }

    @Override
    public void deserialize(ArrayList<String> arrayLoaded) {
        for (String s : arrayLoaded) {
            String parts[] = s.split(",");
            if (parts.length != 3) {
                System.out.println("File error");
                continue;
            }
            SpreadSheetLocation location = new SpreadSheetLocation(parts[0]);
            setCell(location, parseCell(parts[2], parts[0]));
        }
    }


}


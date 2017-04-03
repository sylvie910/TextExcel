package org.waspec;

public class EmptyCell implements Cell{


    @Override
    public String abbreviatedCellText() {
        return "            ";
    }

    @Override
    public String fullCellText() {
        return "<empty>";
    }

    @Override
    public String cellType() {
        return "EmptyCell";
    }

}

package org.waspec;

public class TextCell implements Cell {
    private String text;

    public TextCell(String text) {
        this.text = text.substring(1,text.length()-1);
    }

    @Override
    public String abbreviatedCellText() {
        return SpreadSheet.printFormat(text);
    }

    @Override
    public String fullCellText() {
        return "\"" + text + "\"";
    }

    @Override
    public String cellType() {
        return "TextCell";
    }

}

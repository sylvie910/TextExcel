package org.waspec;

public interface Cell {
    // text for spreadsheet cell display, must be exactly length 12
    String abbreviatedCellText();

    // text for individual cell inspection, not truncated or padded
    String fullCellText();

    String cellType();

}

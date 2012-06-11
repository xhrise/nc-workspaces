package com.ufsoft.report;

import com.ufsoft.table.CellsAuthorization;

public class ReportAuthReadOnly implements CellsAuthorization {

    public boolean isReadable(int row, int col) {
        return true;
    }

    public boolean isWritable(int row, int col) {
        return false;
    }

    public void authorize(int row, int col, int type) {
    }

}

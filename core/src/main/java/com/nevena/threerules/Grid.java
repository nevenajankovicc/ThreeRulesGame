package com.nevena.threerules;

import java.util.Random;

public class Grid {

    private final int rows;
    private final int columns;
    private final Cell[][] grid;
    private final int numOfCrystals;

    private final int startRow;
    private final int startColumn;
    private final int endRow;
    private final int endColumn;

    public Grid(int rows, int columns, int numOfCrystals) {
        this.rows = rows;
        this.columns = columns;
        this.numOfCrystals = numOfCrystals;
        this.grid = new Cell[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = new Cell(false);
            }
        }

        this.startRow = 0;
        this.startColumn = 0;
        this.endRow = rows - 1;
        this.endColumn = columns - 1;

        putCrystals(numOfCrystals);
    }

    //Placing crystals in random locations in the matrix
    private void putCrystals(int numOfCrystals) {
        Random random = new Random();
        int placed = 0;

        while (placed < numOfCrystals) {
            int row = random.nextInt(rows);
            int column = random.nextInt(columns);

            if ((row == startRow && column == startColumn) || (row == endRow && column == endColumn)) {
                continue;
            }

            if (!grid[row][column].hasCrystal()) {
                grid[row][column] = new Cell(true);
                placed++;
            }
        }
    }

    public boolean isInsideGrid(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public Cell getCell(int row, int column) {
        if (isInsideGrid(row, column)) {
            return grid[row][column];
        } else {
            throw new IndexOutOfBoundsException("Coordinates out of grid bounds");
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getNumOfCrystals() {
        return numOfCrystals;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndColumn() {
        return endColumn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if (r == startRow && c == startColumn) {
                    sb.append("      S "); // Start
                }else if (r == endRow && c == endColumn) {
                    sb.append("      E "); // End
                } else if (grid[r][c].hasCrystal()) {
                    sb.append("      * "); // Crystal
                } else {
                    sb.append("      . "); // Empty
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

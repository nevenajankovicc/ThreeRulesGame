package com.nevena.threerules;

import java.util.HashSet;
import java.util.Set;

public class Player{
    private int row;
    private int column;
    private boolean brokeRule;
    private Set<String> visitedCells;
    private Direction lastDirection;
    private int stepsInSameDirection;
    private int collectedCrystals;
    private String gameOverMessage;

    public Player(int startRow, int startColumn) {
        this.row = startRow;
        this.column = startColumn;
        this.brokeRule = false;
        this.visitedCells = new HashSet<>();
        this.lastDirection = null;
        this.stepsInSameDirection = 0;
        this.collectedCrystals = 0;

        markAsVisited(startRow, startColumn);
    }

    public void markAsVisited(int row, int column) {
        visitedCells.add(row + ", " + column);
    }

    public boolean hasVisited(int row, int column) {
        return visitedCells.contains(row + ", " + column);
    }

    public void collectCrystal() {
        collectedCrystals++;
    }

    public boolean move(Direction direction, Grid grid) {
        int newRow = row;
        int newColumn = column;

        switch (direction) {
            case UP: newRow++; break;
            case DOWN: newRow--; break;
            case LEFT: newColumn--; break;
            case RIGHT: newColumn++; break;
        }

        if (!grid.isInsideGrid(newRow, newColumn)) {
            System.out.println("Cannot move outside the board.");
            return false;
        }

        if (hasVisited(newRow, newColumn)) {
            gameOverMessage = "GAME OVER \n" +
                "Cannot visit the same cell twice.";
            brokeRule = true;
            return false;
        }

        if (direction == lastDirection) {
            stepsInSameDirection++;
            if (stepsInSameDirection > 2) {
                gameOverMessage = "GAME OVER \n" +
                    "Cannot move in the same direction\n" +
                                                    "more than twice.";
                brokeRule = true;
                return false;
            }
        } else {
            stepsInSameDirection = 1;
            lastDirection = direction;
        }


        row = newRow;
        column = newColumn;
        markAsVisited(row, column);

        if (newRow == grid.getEndRow() && newColumn == grid.getEndColumn()) {
            if (collectedCrystals != grid.getNumOfCrystals()) {
                gameOverMessage = "GAME OVER \n" +
                    "You haven't collect all crystals";
                brokeRule = true;
                return false;
            }
        }

        // Checking crystals and collecting crystals if any exist
        Cell currentCell = grid.getCell(row, column);
        if (currentCell.hasCrystal()) {
            collectCrystal();
            currentCell.removeCrystal();
            System.out.println("Collected a crystal! Total: " + collectedCrystals);
        }

        System.out.println("Moved to (" + row + ", " + column + ")");

        return true;
    }

    public boolean hasWon(Grid grid) {
        return row == grid.getEndRow() && column == grid.getEndColumn() && collectedCrystals == grid.getNumOfCrystals();
    }

    public boolean hasLost() {
        return brokeRule;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Set<String> getVisitedCells() {
        return visitedCells;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }

    public int getStepsInSameDirection() {
        return stepsInSameDirection;
    }

    public int getCollectedCrystals() {
        return collectedCrystals;
    }

    public String getGameOverMessage() {
        return gameOverMessage;
    }
}

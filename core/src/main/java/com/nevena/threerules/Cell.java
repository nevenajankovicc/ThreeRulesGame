package com.nevena.threerules;

public class Cell {
    private boolean hasCrystal;
    private boolean visited;

    public Cell(boolean hasCrystal) {
        this.hasCrystal = hasCrystal;
        this.visited = false;
    }

    public boolean hasCrystal() {
        return hasCrystal;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited() {
        visited = true;
    }

    public void removeCrystal() {
        this.hasCrystal = false;
    }
}

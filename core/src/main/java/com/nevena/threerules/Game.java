package com.nevena.threerules;

import java.util.Scanner;

public class Game {
    private Grid grid;
    private Player player;
    private final int totalCrystals;

    public Game(int rows, int columns, int numberOfCrystals) {
        grid = new Grid(rows, columns, numberOfCrystals);
        player = new Player(rows - 1, 0);  // Start position in the bottom left corner
        totalCrystals = numberOfCrystals;

        // Setting the starting cell as visited
        grid.getCell(player.getRow(), player.getColumn()).setVisited();
    }

    public boolean movePlayer(Direction direction) {
        // Moving the player
        player.move(direction, grid);

        // Proveri da li je pobedio
        if (player.hasWon(grid)) {
            System.out.println("Great job! You reached the goal and collected all the crystals!");
        } else if (player.hasLost()) {
            System.out.println("You lost");
            return false;
        }
        return true;
    }

    public Grid getGrid() {
        return grid;
    }

    public Player getPlayer() {
        return player;
    }

    public int getTotalCrystals() {
        return totalCrystals;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (!player.hasWon(grid) && !player.hasLost()) {
            System.out.println(grid); // ako imaš toString() za crtanje

            System.out.print("Enter direction: ");
            String input = scanner.nextLine().trim().toUpperCase();

            Direction direction = null;
            switch (input) {
                case "W": direction = Direction.UP; break;
                case "S": direction = Direction.DOWN; break;
                case "A": direction = Direction.LEFT; break;
                case "D": direction = Direction.RIGHT; break;
                default:
                    System.out.println("Unknown command");
                    continue;
            }

            movePlayer(direction);

        }

        if (player.hasWon(grid)) {
            System.out.println("Victory! All crystals: " + player.getCollectedCrystals());
        } else {
            System.out.println("You lost! The rules were not followed");
        }

        scanner.close();
    }
}

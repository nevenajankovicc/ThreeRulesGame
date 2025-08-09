package com.nevena.threerules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import java.util.Random;

public class GameScreen implements Screen {
    private Grid grid;
    private Player player;
    Status status = Status.PLAYING;

    // Used to draw basic shapes directly on the screen
    private ShapeRenderer shapeRenderer;

    private SpriteBatch batch;
    private BitmapFont font;

    private int screenWidth;
    private int screenHeight;
    private int offsetX;
    private int offsetY;

    private int moveCount = 0;
    private static int yourMinMoveCount = 0;
    private int hudHeight = 100;

    private String gameOverMessage = "";
    private String winMessage = "";
    private static boolean showRules = true;

    private int CELL_SIZE;


    public GameScreen() {
        grid = new Grid(10, 10, 5);
        player = new Player(0, 0);

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        CELL_SIZE = Math.min(
            screenWidth / grid.getColumns(),
            (screenHeight - hudHeight) / grid.getRows()
        );

        offsetX = (screenWidth - (CELL_SIZE * grid.getColumns())) / 2;
        offsetY =  ( (screenHeight - hudHeight) - (CELL_SIZE * grid.getRows()) ) / 2;

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (showRules) {
            batch.begin();
            font.setColor(Color.BLACK);
            font.getData().setScale(1.5f);

            font.draw(batch, "Three Rules Game", 50, screenHeight - 50);
            font.draw(batch, "1.You cannot visit the same cell twice.", 50, screenHeight - 100);
            font.draw(batch, "2.You cannot move in the same direction more than twice.", 50, screenHeight - 140);
            font.draw(batch, "3.You must collect all crystals before reaching the end.", 50, screenHeight - 180);

            font.draw(batch, "Press SPACE to start the game.", 50, screenHeight - 240);
            batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                showRules = false;
            }

            return;
        }


        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        grid.getCell(player.getRow(), player.getColumn()).setVisited();

        handleInput();

        // HUD text
        if (status == Status.PLAYING) {
            batch.begin();
            font.setColor(Color.BLACK);
            font.getData().setScale(1.5f);
            font.draw(batch, "Number Of Steps: " + moveCount, 20, screenHeight - 20);
            font.draw(batch, "Your Best Score: " + yourMinMoveCount, 20, screenHeight - 50);
            batch.end();

            batch.begin();
            font.setColor(Color.ORANGE);
            font.getData().setScale(1.0f);
            font.draw(batch, "(Press space to reset)", 20, screenHeight - 80);
            batch.end();
        }

        if (status == Status.LOSE) {
            batch.begin();
            font.setColor(Color.RED);
            font.getData().setScale(2);
            font.draw(batch, gameOverMessage, screenWidth / 2f - 300, screenHeight / 2f);
            font.draw(batch, "Press SPACE to reset", screenWidth / 2f - 300, screenHeight / 2f - 200);
            batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                resetGame();
                return;
            }

            return;
        } else if (status == Status.WON) {
            batch.begin();
            font.setColor(Color.GREEN);
            font.getData().setScale(2);
            font.draw(batch, winMessage, screenWidth / 2f - 300, screenHeight / 2f);
            font.draw(batch, "Press SPACE to reset", screenWidth / 2f - 300, screenHeight / 2f - 200);
            batch.end();

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                resetGame();
                return;
            }

            return;

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            resetGame();
            return;
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Drawing the matrix
        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {
                Cell cell = grid.getCell(row, column);

                //Cells - green if visited, light gray if not visited
                if (cell.isVisited()) {
                    shapeRenderer.setColor(Color.GREEN);
                } else {
                    shapeRenderer.setColor(Color.LIGHT_GRAY);
                }

                shapeRenderer.rect(offsetX + column * CELL_SIZE, offsetY + row * CELL_SIZE, CELL_SIZE, CELL_SIZE);


                //Crystals - blue
                if (cell.hasCrystal()) {
                    shapeRenderer.setColor(Color.BLUE);
                    shapeRenderer.circle(offsetX + column * CELL_SIZE + CELL_SIZE / 2f,
                        offsetY + row * CELL_SIZE + CELL_SIZE / 2f,
                        CELL_SIZE / 9f);

                }
            }
        }

        //Player - red circle
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(offsetX + player.getColumn() * CELL_SIZE + CELL_SIZE / 2f,
            offsetY + player.getRow() * CELL_SIZE + CELL_SIZE / 2f,
            CELL_SIZE / 4f);

        shapeRenderer.end();


        // Drawing lines between cells in the matrix
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);

        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getColumns(); col++) {
                float x = offsetX + col * CELL_SIZE;
                float y = offsetY + row * CELL_SIZE;
                shapeRenderer.rect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }

        shapeRenderer.end();


        batch.begin();

        // Displaying START and END
        if (player.getColumn() != 0 || player.getRow() != 0) {
            float xS = offsetX + 0 * CELL_SIZE + CELL_SIZE / 4f;
            float yS = offsetY + 0 * CELL_SIZE + CELL_SIZE * 0.75f;

            font.draw(batch, "START", xS, yS);
        }

        if (player.getRow() != (grid.getRows() - 1) || player.getColumn() != (grid.getColumns()) - 1) {
            float xE = offsetX + (grid.getColumns() - 1) * CELL_SIZE + CELL_SIZE / 4f;
            float yE = offsetY + (grid.getRows() - 1) * CELL_SIZE + CELL_SIZE * 0.75f;

            font.draw(batch, "END", xE, yE);
        }

        batch.end();


    }

    private void handleInput() {
        if (status == Status.LOSE || status == Status.WON) {
            // The game is over; do not allow further movement
            return;
        }

        boolean moved = false;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            moved = player.move(Direction.UP, grid);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            moved = player.move(Direction.DOWN, grid);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            moved = player.move(Direction.LEFT, grid);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            moved = player.move(Direction.RIGHT, grid);
        }

        if (moved) {
            // Mark the cell where the player is located as visited
            grid.getCell(player.getRow(), player.getColumn()).setVisited();
            moveCount++;

        }

        if (player.hasWon(grid)) {
            status = Status.WON;
            if (yourMinMoveCount != 0) {
                yourMinMoveCount = Math.min(yourMinMoveCount, moveCount);
            } else {
                yourMinMoveCount = moveCount;
            }
            winMessage = "CONGRATULATIONS! YOU WON!";
        } else if (player.hasLost()) {
            status = Status.LOSE;
            gameOverMessage = player.getGameOverMessage();
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        /* Releases resources used by ShapeRenderer, SpriteBatch,
           and font to prevent memory leaks and free up system memory.
        */
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }

    public void resetGame() {
        // Resetting the game
        ((MainGame) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
    }


}

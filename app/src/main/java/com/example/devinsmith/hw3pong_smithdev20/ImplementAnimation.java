package com.example.devinsmith.hw3pong_smithdev20;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

/**
 * animates the ball's movement
 *
 * @author Devin Smith
 * @version March 2018
 */

public class ImplementAnimation implements Animator {
    private int x, y, xCount, yCount, paddleWidth, paddleR, paddleL, paddleCenter, score, ballRemain, wallIndex;
    private ArrayList<Integer> wallList = new ArrayList<Integer>();
    /**
     * External Citation:
     * Problem: I couldn't make array list with int
     * Citation: https://stackoverflow.com/questions/18021218/create-a-list-of-primitive-int
     * Solution: use Integer
     */
    private boolean goBackwardsX, goBackwardsY;
    private boolean paused;
    private Random rand;


    @Override
    public int interval() {
        return 30;
    }

    /**
     * colors the background
     *
     * @return background color
     */
    @Override
    public int backgroundColor() {
        return Color.rgb(0, 0, 0);
    }

    @Override
    public boolean doPause() {
        return paused;
    }

    @Override
    public boolean doQuit() {
        return false;
    }

    /**
     * draws the ball and the walls and paddle
     *
     * @param canvas
     */
    @Override
    public void tick(Canvas canvas) {
        Paint scoreColor = new Paint();
        scoreColor.setColor(Color.rgb(0, 255, 137));

        if (goBackwardsX) {
            xCount--;
        } else {
            xCount++;
        }

        if (goBackwardsY) {
            yCount--;
        } else {
            yCount++;
        }

        //draw walls
        Paint wallColor = new Paint();
        wallColor.setColor(Color.WHITE);
        drawWalls(canvas, wallColor);

        //draws paddle
        paddleCoords();
        canvas.drawRect(paddleR, 1000f, paddleL, 1300f, wallColor);//paddle

        Paint end = new Paint();
        end.setColor(Color.rgb(0, 26, 255));
        end.setTextSize(150);

        if (ballRemain == 0) {
            paused = true;
            y = 1300;
            canvas.drawText("YOU LOSE", 700, 450, end);
        } else {
            //draws ball
            Paint ballColor = new Paint();
            ballColor.setColor(Color.RED);

            if (y == 1300) {
                canvas.drawCircle(x, 1400f, 100f, ballColor);
                paused = true;
            }
            else {
                x = getX(xCount);
                y = getY(yCount);
            }
            canvas.drawCircle(x, y, 100f, ballColor);
        }

        scoreColor.setTextSize(75f);
        canvas.drawText("Score: " + score, 20, 80, scoreColor);
        canvas.drawText("Remaining Balls: " + ballRemain, 1400, 80, scoreColor);

        if (score == 10) {
            canvas.drawText("YOU WIN", 700, 450, end);
        }

    }

    /**
     * draws the walls
     * @param canvas
     * @param wallColor
     */
    public void drawWalls(Canvas canvas, Paint wallColor){
        canvas.drawRect(0f, 0f, 2000f, 100f, wallColor);//top wall
        Paint black = new Paint();
        black.setColor(Color.BLACK);

        for (int i = 0; i < wallList.size(); i++) {
            canvas.drawRect(wallList.get(i)-100, 0, wallList.get(i) + 100, 200, black);
        }
        canvas.drawRect(0f, 0f, 100f, 1500f, wallColor);//left wall
        canvas.drawRect(1950f, 0f, 2050f, 1500f, wallColor);//right wall
    }

    /**
     * getXCoordinate moves the ball in the x direction, if the ball hits a wall then
     * it will change directions
     *
     * @return x
     */
    public int getX(int xCount) {
        x = (xCount * 20) % 1850;

        if (x == 200) {
            goBackwardsX = false;
            return x;
        }

        if (x == 1800) {
            goBackwardsX = true;
            return x;
        }

        return x;
    }

    /**
     * getYCoordinate moves the ball in the y direction, if the ball hits a wall or the paddle then
     * it will change directions
     *
     * @param yCount
     * @return y
     */
    public int getY(int yCount) {
        y = (yCount * 20) % 1850;

        if (y <= 200) {
            if (checkWall(x)) {
                ballOut();
                return y;
            }
            else if( !checkWall(x)) {
                goBackwardsY = false;
                wallList.add(x);
                wallIndex++;
                return y;
            }
        }

        //if ball hits paddle
        if (y == 900) {
            if (x >= paddleL - 25 && x <= paddleR + 25) {
                goBackwardsY = true;
                score++;
            }
        }
        //if the ball leaves the platform
        if (y == 1200) {
            ballOut();
        }
        return y;
    }

    /**
     * checks if that piece of wall is still there
     * @param x
     * @return
     */
    public boolean checkWall(int x) {
        for (int i = 0; i < wallList.size(); i++) {
            if (x >= wallList.get(i) -100  && x <= wallList.get(i) + 100) {
                return true;
            }
        }
        return false;
    }

    /**
     * sets paddle width based on button selection
     *
     * @param width
     */
    public void setPaddleWidth(int width) {
        this.paddleWidth = width;
    }

    /**
     * gives coordinates by using paddleWidth,
     * if no button was selected the small size one will be set
     */
    public void paddleCoords() {
        int midPad = paddleWidth / 2;

        if (paddleWidth == 0) {
            paddleWidth = 600;
            midPad = 300;
        }

        paddleR = paddleCenter + midPad;
        paddleL = paddleCenter - midPad;
    }

    public void ballOut() {
        ballRemain--;
        y = 1500;
        paused = true;
    }

    /**
     * calls random x and y and both directions
     */
    public void reset() {
        randomX();
        randomXDir();
        randomY();
        randomYDir();
        paused = false;
    }

    public void getRemaining(int ballRemaining) {
        ballRemain = ballRemaining;
    }

    /**
     * randomX will set the x coordinates for the new ball
     */
    public void randomX() {
        rand = new Random();
        x = rand.nextInt(1600) + 300;
        xCount = x / 20;
    }

    /**
     * randomXDir will randomly set the horizontal direction for the new ball
     */
    public void randomXDir() {
        rand = new Random();
        int randCoord = rand.nextInt(2);
        if (randCoord == 0) {
            goBackwardsX = true;
        } else if (randCoord == 1) {
            goBackwardsX = false;
        }
    }

    /**
     * randomY will set the y coordinates for the new ball
     */
    public void randomY() {
        rand = new Random();
        y = rand.nextInt(760) + 300;
        yCount = y / 20;

    }

    /**
     * randomYDir will randomly set the vertical direction for the new ball
     */
    public void randomYDir() {
        rand = new Random();
        int randCoord = rand.nextInt(2);
        if (randCoord == 0) {
            goBackwardsY = true;
        } else if (randCoord == 1) {
            goBackwardsY = false;
        }
    }

    /**
     * External citation:
     * Date: 3-16-18
     * Problem: Couldn't figure out how to get position of tap
     * Resource:https://stackoverflow.com/questions/3476779/how-to-get-the-touch-position-in-android
     * Solution:the code below
     */
    @Override
    public void onTouch(MotionEvent event) {

        paddleCenter = (int) event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
        }
    }
}

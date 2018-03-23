package com.example.devinsmith.hw3pong_smithdev20;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.Random;

/**
 * animates the ball's movement
 *
 * @author Devin Smith
 * @version March 2018
 */

public class ImplementAnimation implements Animator {
    private int x, y, xCount, yCount, paddleWidth, paddleR, paddleL;
    private double speed;
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


        //draws the walls
        Paint wallColor = new Paint();
        wallColor.setColor(Color.WHITE);
        canvas.drawRect(0f, 0f, 100f, 1500f, wallColor);//left wall
        canvas.drawRect(1950f, 0f, 2050f, 1500f, wallColor);//right wall
        canvas.drawRect(0f, 0f, 2000f, 100f, wallColor);//top wall

        //draws paddle
        paddleCoords();
        canvas.drawRect(paddleR, 1000f, paddleL, 1300f, wallColor);//paddle

        //draws ball
        Paint ballColor = new Paint();
        ballColor.setColor(Color.RED);
        if (y == 1300) {
            canvas.drawCircle(x, 1400f, 100f, ballColor);
            paused = true;

        } else {
            x = getX(xCount);

            y = getY(yCount);
        }


        canvas.drawCircle(x, y, 100f, ballColor);


        if (y == 900) {
            if (x >= paddleL-25 && x <= paddleR+25) {
                goBackwardsY = true;
            } else if (x < paddleL || x > paddleR) {
                goBackwardsY = false;
                if (y == 1100) {
                    paused = true;
                    y = 1300;
                }
            }
        }

    }

    public void setVelocity() {
        Random rand = new Random();
        //speed = rand.nextFloat(1.0f);
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

        if (y == 200) {
            goBackwardsY = false;
            return y;
        }
        return y;
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
     * gives coordinates by using paddleWidth
     */
    public void paddleCoords() {
        if (paddleWidth == 0) {
            paddleWidth = 600;
        }
        paddleR = 2050 - paddleWidth;
        paddleL = paddleWidth;
    }

    /**
     * calls random x and y when
     */
    public void reset() {
        randomX();
        randomXDir();
        randomY();
        randomYDir();
        setVelocity();
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
     * Solution:Although I won't use this until later I found but commented out sample code
     */
    @Override
    public void onTouch(MotionEvent event) {

        //int x = (int)event.getX();
        //  int y = (int)event.getY();
        reset();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:

                paused = false;

        }
    }
}

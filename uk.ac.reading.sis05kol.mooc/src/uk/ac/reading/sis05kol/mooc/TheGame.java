package uk.ac.reading.sis05kol.mooc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class TheGame extends GameThread {

    private Bitmap mBall;

    private float mBallX = -100;
    private float mBallY = -100;

    private float mBallSpeedX = 0;
    private float mBallSpeedY = 0;

    private Bitmap mPaddle;

    private float playerY = 0;
    private float opponentY = 0;

    public TheGame(GameView gameView) {
        super(gameView);

        mBall = BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.white_ball);

        mPaddle = BitmapFactory.decodeResource
                (gameView.getContext().getResources(),
                        R.drawable.paddle);
    }

    //This is run before a new game (also after an old game)
    @Override
    public void setupBeginning() {
        mBallSpeedX = mCanvasWidth * 2 / 3;
        mBallSpeedY = mCanvasHeight / 5;

        mBallX = mCanvasWidth / 2;
        mBallY = mCanvasHeight / 2;

        playerY = mCanvasHeight / 2;
        opponentY = mCanvasHeight / 2;
    }

    @Override
    protected void doDraw(Canvas canvas) {
        if (canvas == null) return;
        super.doDraw(canvas);
        canvas.drawBitmap(mBall, mBallX - mBall.getWidth() / 2, mBallY - mBall.getHeight() / 2, null);
        canvas.drawBitmap(mPaddle, mCanvasWidth - mPaddle.getWidth() * 2, playerY - mPaddle.getHeight() / 2, null);
        canvas.drawBitmap(mPaddle, mPaddle.getWidth() * 2, opponentY - mPaddle.getHeight() / 2, null);
    }

//    @Override
//    protected void actionOnTouch(float x, float y) {
//        playerY = y - mPaddle.getHeight() / 2;
//    }

    @Override
    protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
        if (playerY >= 0 && playerY <= mCanvasHeight) {
            playerY = playerY + xDirection - 40;
        }
    }

    //This is run just before the game "scenario" is printed on the screen
    @Override
    protected void updateGame(float secondsElapsed) {
        mBallX = mBallX + secondsElapsed * mBallSpeedX;
        mBallY = mBallY + secondsElapsed * mBallSpeedY;

        playerLimits();
        opponentLimits();
        bounceSides();
        checkPlayerCollision();
        checkOpponentCollision();
        checkWinner();
    }

    private void playerLimits() {
        if (playerY < mPaddle.getHeight() / 2) {
            playerY = mPaddle.getHeight() / 2;
        } else if (playerY > mCanvasHeight - mPaddle.getHeight() / 2){
            playerY = mCanvasHeight - mPaddle.getHeight() / 2;
        }
    }


    private void opponentLimits() {
        if (playerY < mPaddle.getHeight() / 2) {
            playerY = mPaddle.getHeight() / 2;
        } else if (playerY > mCanvasHeight - mPaddle.getHeight() / 2){
            playerY = mCanvasHeight - mPaddle.getHeight() / 2;
        }
    }
    private void bounceSides() {
        if (mBallX <= mBall.getWidth() / 2 & mBallSpeedX < 0) {  //left
            updateScore(1);
            mBallSpeedX = -mBallSpeedX;
        }
        if (mBallX >= mCanvasWidth - mBall.getWidth() / 2 & mBallSpeedX > 0) { //right
            updateScore(-1);
            mBallSpeedX = -mBallSpeedX;
        }
        if (mBallY <= mBall.getWidth() / 2 && mBallSpeedY < 0 || mBallY >= mCanvasHeight - mBall.getWidth() / 2 && mBallSpeedY > 0) { //top and bottom
            mBallSpeedY = -mBallSpeedY;
        }
    }

    private void checkPlayerCollision() {
        if (mBallSpeedX > 0                                          // moving towards player
                && mBallX > mCanvasWidth - mPaddle.getWidth() * 2    // x
                && mBallY >= playerY - mPaddle.getHeight() / 2      // y bottom
                && mBallY <= playerY + mPaddle.getHeight() / 2) {   // y top
//            mBallSpeedX = -mBallSpeedX;
            collision(mCanvasWidth + 30, playerY);
        }
    }

    private void checkOpponentCollision() {
        if (mBallSpeedX < 0                                          // moving towards opponent
                && mBallX < mPaddle.getWidth() * 2                   // x
                && mBallY >= opponentY - mPaddle.getHeight() / 2      // y bottom
                && mBallY <= opponentY + mPaddle.getHeight() / 2) {   // y top
            collision(- 30, opponentY);
        }
    }

    private void collision(int x, float y) {
                float velocityOfBall = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
                mBallSpeedX = mBallX - x;
                mBallSpeedY = mBallY - y;
                float newVelocity = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
                mBallSpeedX = mBallSpeedX * velocityOfBall / newVelocity;
                mBallSpeedY = mBallSpeedY * velocityOfBall / newVelocity;
    }

    private void checkWinner() {
        if (score == 3) {
            setState(GameThread.STATE_WIN);
        }

        if (score == -3) {
            setState(GameThread.STATE_LOSE);
        }
    }
}

//    Pythagoras and Trig collision method
//    private void deflectFromPosition(int ball, float x, float y) {
//        float newXSpeed = mBallX - x;
//        float newYSpeed = mBallY - y;
//        float velocityOfBall = (float) Math.sqrt(newXSpeed * newXSpeed + newYSpeed * newYSpeed);
//
//        double origAngle = Math.atan2(newXSpeed, newYSpeed);
//        float newVelocity = 1000 - velocityOfBall;
//        mBallSpeedX = (float) (Math.sin(origAngle) * newVelocity);
//        mBallSpeedY = (float) (Math.cos(origAngle) * newVelocity);
//    }

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// 
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.

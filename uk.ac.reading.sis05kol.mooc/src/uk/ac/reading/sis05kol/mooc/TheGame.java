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

    private float mPaddleY = 0;

    private float mMinDistanceBetweenBallAndObject = 0;

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
        mBallSpeedX = mCanvasWidth / 2;
        mBallSpeedY = mCanvasHeight / 5;

        mBallX = mCanvasWidth / 2;
        mBallY = mCanvasHeight / 2;

        mPaddleY = mCanvasHeight / 2;

//        mMinDistanceBetweenBallAndObject = (mPaddle.getWidth() / 2 * mPaddle.getWidth() / 2) + (mBall.getWidth() / 2 * mBall.getWidth() / 2);
    }

    @Override
    protected void doDraw(Canvas canvas) {
        if (canvas == null) return;
        super.doDraw(canvas);
        canvas.drawBitmap(mBall, mBallX - mBall.getWidth() / 2, mBallY - mBall.getHeight() / 2, null);
        canvas.drawBitmap(mPaddle, mCanvasWidth - mPaddle.getWidth() * 2, mPaddleY - mPaddle.getHeight() / 2, null);
    }

    @Override
    protected void actionOnTouch(float x, float y) {
        mPaddleY = y - mPaddle.getHeight() / 2;
    }

    @Override
    protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
        if (mPaddleY >= 0 && mPaddleY <= mCanvasHeight) {
            mPaddleY = mPaddleY + xDirection - 40;

            if (mPaddleY < mPaddle.getHeight() / 2) mPaddleY = mPaddle.getHeight() / 2;
            if (mPaddleY > mCanvasHeight - mPaddle.getHeight() / 2) mPaddleY = mCanvasHeight - mPaddle.getHeight() / 2;
        }
    }


    //This is run just before the game "scenario" is printed on the screen
    @Override
    protected void updateGame(float secondsElapsed) {

        mBallX = mBallX + secondsElapsed * mBallSpeedX;
        mBallY = mBallY + secondsElapsed * mBallSpeedY;

//        checkPaddleCollision();
        basicCollision();
        bounceSides();
        checkWinner();
    }

    private void basicCollision() {
        if(mBallX > mCanvasWidth - mPaddle.getWidth() * 2
                && mBallY >= mPaddleY - mPaddle.getHeight() / 2
                && mBallY <= mPaddleY + mPaddle.getHeight() / 2){
            mBallSpeedX = -mBallSpeedX;

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

    private void checkPaddleCollision() {
        if (mBallSpeedX > 0) {
            if (paddleCollision()) {
                float velocityOfBall = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
                mBallSpeedX = mBallX - mCanvasWidth;
                mBallSpeedY = mBallY - mPaddleY;
                float newVelocity = (float) Math.sqrt(mBallSpeedX * mBallSpeedX + mBallSpeedY * mBallSpeedY);
                mBallSpeedX = mBallSpeedX * velocityOfBall / newVelocity;
                mBallSpeedY = mBallSpeedY * velocityOfBall / newVelocity;
            }
        }
    }

    private boolean paddleCollision() {
        float distanceBetweenBallAndPaddle = (mCanvasWidth - mBallX) * (mCanvasWidth - mBallX) + (mPaddleY - mBallY) * (mPaddleY - mBallY);
        return mMinDistanceBetweenBallAndObject >= distanceBetweenBallAndPaddle;
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

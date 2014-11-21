package uk.ac.reading.sis05kol.mooc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class TheGame extends GameThread{

	private Bitmap[] mBall = new Bitmap[3];
	
	private float[] mBallX = {-20,-20,-20};
	private float[] mBallY = {-20,-20,-20};
	
	private float[] mBallSpeedX = {0,0,0};
	private float[] mBallSpeedY = {0,0,0};

	public TheGame(GameView gameView) {
		super(gameView);
		
		mBall[0] = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.red_balloon);
		mBall[1] = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.purple_balloon);
		mBall[2] = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.green_balloon);
	}
	
	@Override
	public void setupBeginning() {
		
		setInitialLocation(0, 0.5f, 0.5f);
		setInitialLocation(1, 0.3f, 0.3f);
		setInitialLocation(2, 0.6f, 0.6f);
		
		setInitialSpeed(0, 150, 150);
		setInitialSpeed(1, -150, -150);
		setInitialSpeed(2, 150, 150);	
		
	}

	private void setInitialLocation(int ball, float canvasX, float canvasY) {
		mBallX[ball] = mCanvasWidth * canvasX;
		mBallY[ball] = mCanvasHeight * canvasY;
	}

	private void setInitialSpeed(int ball, int speedX, int speedY) {
		mBallSpeedX[ball] = speedX; 
		mBallSpeedY[ball] = speedY;
	}
	
	@Override
	protected void doDraw(Canvas canvas) {
		if(canvas == null) return;
		
		super.doDraw(canvas);
		
		canvas.drawBitmap(mBall[0], mBallX[0] - mBall[0].getWidth() / 2, mBallY[0] - mBall[0].getHeight() / 2, null);
		canvas.drawBitmap(mBall[1], mBallX[1] - mBall[1].getWidth() / 2, mBallY[1] - mBall[1].getHeight() / 2, null);
		canvas.drawBitmap(mBall[2], mBallX[2] - mBall[2].getWidth() / 2, mBallY[2] - mBall[2].getHeight() / 2, null);
	}
	
	@Override
	protected void actionOnTouch(float x, float y) {
		deflectFromTouch(0,x,y);
		deflectFromTouch(1,x,y);
		deflectFromTouch(2,x,y);
	}
	
	private void deflectFromTouch(int ball, float x, float y) {
		float newXSpeed = mBallX[ball] - x;
		float newYSpeed = mBallY[ball] - y;
		float velocityOfBall = (float) Math.sqrt(newXSpeed*newXSpeed + newYSpeed*newYSpeed);
		
		float minChangableVelocity = 400;
		if (velocityOfBall < minChangableVelocity){
	        double origAngle = Math.atan2(newXSpeed, newYSpeed);
	        float newVelocity = 1000 - velocityOfBall;
			mBallSpeedX[ball] = (float) (Math.sin(origAngle) * newVelocity);
	        mBallSpeedY[ball] = (float) (Math.cos(origAngle) * newVelocity);
		}
	}

//	@Override
//	protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
//		//Increase/decrease the speed of the ball
//		mBallSpeedX = mBallSpeedX - 1.5f * xDirection;
//		mBallSpeedY = mBallSpeedY - 1.5f * yDirection;
//	}
	
	@Override
	protected void updateGame(float secondsElapsed) {
		
		moveBall(0, secondsElapsed);
		moveBall(1, secondsElapsed);
		moveBall(2, secondsElapsed);
		
		gravity(0);
		gravity(1);
		gravity(2);
		
		bounceOffWalls(0);
		bounceOffWalls(1);
		bounceOffWalls(2);
	}

	private void moveBall(int ball, float secondsElapsed) {
		mBallX[ball] = mBallX[ball] + secondsElapsed * mBallSpeedX[ball];
		mBallY[ball] = mBallY[ball] + secondsElapsed * mBallSpeedY[ball];
	}

	private void gravity(int ball){
		mBallSpeedY[ball] = mBallSpeedY[ball] + 20f;
	}
	
	private void bounceOffWalls(int ball) {
		if (mBallX[ball] <= 0){
			mBallSpeedX[ball] = Math.abs(mBallSpeedX[ball]);
		}
		if (mBallX[ball] >= mCanvasWidth){
			mBallSpeedX[ball] = -Math.abs(mBallSpeedX[ball]);
		}
		if (mBallY[ball] >= mCanvasHeight + 50){
			mBallY[ball] = -50;
			mBallSpeedY[ball] = 0;
		}
	}
	
}

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

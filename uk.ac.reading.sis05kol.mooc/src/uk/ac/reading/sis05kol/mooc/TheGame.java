package uk.ac.reading.sis05kol.mooc;

//Other parts of the android libraries that we use
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class TheGame extends GameThread{

	private Bitmap mBall;

	private float mBallX = -100;
	private float mBallY = -100;

	private float mBallSpeedX = 0;
	private float mBallSpeedY = 0;

	private Bitmap mPaddle;
	
	private float mPaddleX = 0;
	
	private Bitmap mSmileyBall;

	private float mSmileyBallX = -100;
	private float mSmileyBallY = -100;
	
	private Bitmap mSadBall;

	private float[] mSadBallX = {-100,-100,-100};
	private float[] mSadBallY = new float[3];

	private float mMinDistanceBetweenRedBallAndBigBall = 0;

	public TheGame(GameView gameView) {
		super(gameView);

		//prepare images
		mBall = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.small_red_ball);

		mPaddle = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.yellow_ball);

		mSmileyBall =  BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.smiley_ball);

		mSadBall =  BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
						R.drawable.sad_ball);
	}

	//This is run before a new game (also after an old game)
	@Override
	public void setupBeginning() {
		mBallSpeedX = mCanvasWidth / 3; 
		mBallSpeedY = mCanvasHeight / 3;

		mBallX = mCanvasWidth / 2;
		mBallY = mCanvasHeight / 2;

		mPaddleX = mCanvasWidth / 2;

		mSmileyBallX = mCanvasWidth / 2;
		mSmileyBallY = mSmileyBall.getHeight()/2;

		mSadBallX[0] = mCanvasWidth / 3;
		mSadBallY[0] = mCanvasHeight / 3;
		
		mSadBallX[1] = mCanvasWidth - mCanvasWidth / 3;
		mSadBallY[1] = mCanvasHeight / 3;

		mSadBallX[2] = mCanvasWidth / 2;
		mSadBallY[2] = mCanvasHeight / 5;

		mMinDistanceBetweenRedBallAndBigBall = (mPaddle.getWidth() / 2 * mPaddle.getWidth() / 2) + (mBall.getWidth() / 2 * mBall.getWidth() / 2);
	}

	@Override
	protected void doDraw(Canvas canvas) {
		if(canvas == null) return;

		super.doDraw(canvas);

		canvas.drawBitmap(mBall, mBallX - mBall.getWidth() / 2, mBallY - mBall.getHeight() / 2, null);

		canvas.drawBitmap(mPaddle, mPaddleX - mPaddle.getWidth() / 2, mCanvasHeight - mPaddle.getHeight() / 2, null);
		
		canvas.drawBitmap(mSmileyBall, mSmileyBallX - mSmileyBall.getWidth() / 2, mSmileyBallY - mSmileyBall.getHeight() / 2, null);
		
		for(int i = 0; i < mSadBallX.length; i++) {
			canvas.drawBitmap(mSadBall, mSadBallX[i] - mSadBall.getWidth() / 2, mSadBallY[i] - mSadBall.getHeight() / 2, null);
		}
	}

	@Override
	protected void actionOnTouch(float x, float y) {
		mPaddleX = x - mPaddle.getWidth() / 2;
	}
	
	@Override
	protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {
		if(mPaddleX >= 0 && mPaddleX <= mCanvasWidth) {
			mPaddleX = mPaddleX - xDirection;
			
			if(mPaddleX < 0) mPaddleX = 0;
			if(mPaddleX > mCanvasWidth) mPaddleX = mCanvasWidth;			
		}
	}
	 

	//This is run just before the game "scenario" is printed on the screen
	@Override
	protected void updateGame(float secondsElapsed) {
		float distanceBetweenBallAndPaddle;
		
		//If the ball moves down on the screen perform potential paddle collision
		if(mBallSpeedY > 0) {
			//Get actual distance (without square root - remember?) between the mBall and the ball being checked
			distanceBetweenBallAndPaddle = (mPaddleX - mBallX) * (mPaddleX - mBallX) + (mCanvasHeight - mBallY) *(mCanvasHeight - mBallY);
		
			//Check if the actual distance is lower than the allowed => collision
			if(mMinDistanceBetweenRedBallAndBigBall >= distanceBetweenBallAndPaddle) {

				//Get the present velocity (this should also be the velocity going away after the collision)
				float velocityOfBall = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

				//Change the direction of the ball (See image) TODO need image of this
				mBallSpeedX = mBallX - mPaddleX;
				mBallSpeedY = mBallY - mCanvasHeight;

				//Get the velocity after the collision
				float newVelocity = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

				//using the fraction between the original velocity and present velocity to calculate the needed
				//speeds in X and Y to get the original velocity but with the new angle.
				mBallSpeedX = mBallSpeedX * velocityOfBall / newVelocity;
				mBallSpeedY = mBallSpeedY * velocityOfBall / newVelocity;

			}
		}
	
		mBallX = mBallX + secondsElapsed * mBallSpeedX;
		mBallY = mBallY + secondsElapsed * mBallSpeedY;


		//Check if the ball hits either the left side or the right side of the screen
		//But only do something if the ball is moving towards that side of the screen
		//If it does that => change the direction of the ball in the X direction
		if((mBallX <= mBall.getWidth() / 2 & mBallSpeedX < 0) || (mBallX >= mCanvasWidth - mBall.getWidth() / 2 & mBallSpeedX > 0) ) {
			mBallSpeedX = -mBallSpeedX;
		}
		
		
		distanceBetweenBallAndPaddle = (mSmileyBallX - mBallX) * (mSmileyBallX - mBallX) + (mSmileyBallY - mBallY) *(mSmileyBallY - mBallY);
		
		//Check if the actual distance is lower than the allowed => collision
		if(mMinDistanceBetweenRedBallAndBigBall >= distanceBetweenBallAndPaddle) {

			//Get the present velocity (this should also be the velocity going away after the collision)
			float velocityOfBall = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

			//Change the direction of the ball (See image) TODO need image of this
			mBallSpeedX = mBallX - mSmileyBallX;
			mBallSpeedY = mBallY - mSmileyBallY;

			//Get the velocity after the collision
			float newVelocity = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

			//using the fraction between the original velocity and present velocity to calculate the needed
			//speeds in X and Y to get the original velocity but with the new angle.
			mBallSpeedX = mBallSpeedX * velocityOfBall / newVelocity;
			mBallSpeedY = mBallSpeedY * velocityOfBall / newVelocity;
			
			//Increase score
			updateScore(1);
			
		}
		
		//Loop through all SadBalls
		for(int i = 0; i < mSadBallX.length; i++) {
			//Perform collisions (if necessary) between SadBall in position i and the red ball
			
			//Get actual distance (without square root - remember?) between the mBall and the ball being checked
			distanceBetweenBallAndPaddle = (mSadBallX[i] - mBallX) * (mSadBallX[i] - mBallX) + (mSadBallY[i] - mBallY) *(mSadBallY[i] - mBallY);
		
			//Check if the actual distance is lower than the allowed => collision
			if(mMinDistanceBetweenRedBallAndBigBall >= distanceBetweenBallAndPaddle) {

				//Get the present velocity (this should also be the velocity going away after the collision)
				float velocityOfBall = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

				//Change the direction of the ball (See image) TODO need image of this
				mBallSpeedX = mBallX - mSadBallX[i];
				mBallSpeedY = mBallY - mSadBallY[i];

				//Get the velocity after the collision
				float newVelocity = (float) Math.sqrt(mBallSpeedX*mBallSpeedX + mBallSpeedY*mBallSpeedY);

				//using the fraction between the original velocity and present velocity to calculate the needed
				//speeds in X and Y to get the original velocity but with the new angle.
				mBallSpeedX = mBallSpeedX * velocityOfBall / newVelocity;
				mBallSpeedY = mBallSpeedY * velocityOfBall / newVelocity;
			}

		}

		//If the ball goes out of the top of the screen and moves towards the top of the screen =>
		//change the direction of the ball in the Y direction and add a point
		if(mBallY <= mBall.getWidth() / 2 && mBallSpeedY < 0) {
			mBallSpeedY = -mBallSpeedY;
		}

		//If the ball goes out of the bottom of the screen => lose the game
		if(mBallY >= mCanvasHeight) {
			setState(GameThread.STATE_LOSE);
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

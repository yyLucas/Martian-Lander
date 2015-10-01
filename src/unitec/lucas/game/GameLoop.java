package unitec.lucas.game;

import unitec.lucas.martianlander.Constant;
import unitec.lucas.martianlander.R;
import unitec.lucas.model.SpaceCraft;
import unitec.lucas.model.Terrain;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;

/**
 * Main surface view, handle all the game's events.
 * @author Yang Zhang (Lucas)
 *
 */

public class GameLoop extends SurfaceView implements Runnable, SurfaceHolder.Callback, OnTouchListener {

	/** Terrain Shape*/
	Terrain terrain;
	Path terrainPath;
	
	/** Space craft */
	SpaceCraft craft;
	
	double t = Constant.INITAL_TIME;
		
	/** Main Tread*/
	Thread main;
	
	Canvas canvas = new Canvas();
	Paint paint = new Paint();
	
	/** Check which button is pressed*/
	boolean drawLeftThruster = false;
	boolean drawRightThruster = false;
	boolean drawMainThruster = false;
	
	/** Fuel Gauge bar*/
	ProgressBar fuelBar;
	
	//ProgressBar fuelBar = (ProgressBar)findViewById(R.id.fuelProgressBar);
	
	SoundPool soundPool;
	
	boolean gameOver = false;
	

	/**
	 * Initialize
	 * @param context
	 */
	public GameLoop(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		initialize();	//Initialize the game
		initialSound(context);	//Initial sound
	}
	
	/**
	 * Initialize
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public GameLoop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		// TODO Auto-generated constructor stub
		initialize();	//Initialize the game
		initialSound(context);	//Initial sound
	}

	/**
	 * Initialize
	 * @param context
	 * @param attrs
	 */
	public GameLoop(Context context, AttributeSet attrs) {
		super(context, attrs);

		// TODO Auto-generated constructor stub
		initialize();	//Initialize the game
		initialSound(context);	//Initial sound
	}
	
	/**
	 * Initial the sound
	 * @param context
	 */
	@SuppressWarnings("deprecation")
	public void initialSound(Context context){
		//soundPool= new SoundPool(10,AudioManager.STREAM_SYSTEM,5);
		soundPool.load(context, R.raw.explosion, 1);
	}

	/**
	 * Handle on touch listener, start the game
	 * @return 
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//re-initialize the program
		initialize();
		return true;
	}

	/**
	 * Called when surface first created, start the thread
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		main = new Thread(this);
		if (main != null) {
			main.start();
		}
	}

	/**
	 * Called when there is any structural changed in the surface
	 * @param holder
	 * @param format
	 * @param width
	 * @param height
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Called when surface destroyed, close the thread
	 * Reference to John Casey's code
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		while (retry)
		{
			try
			{
				main.join();	//Close the thread
				retry = false;
			}
			catch (InterruptedException e)
			{
				// try again shutting down the thread
			}
		}
		
	}

	/**
	 * Execute the thread, update and draw the background, space craft, thrusters and so on.
	 * It refreshes and updates every 20ms.
	 */
	@Override
	public void run() {
		while(true) {
			while (!gameOver) {
				Canvas canvas = null;
				SurfaceHolder holder = getHolder();
				synchronized (holder) {
					canvas = holder.lockCanvas();	//lock canvas
					
					canvas.drawColor(Color.BLACK);	//draw black background
					terrain.draw(canvas, paint);	//draw terrain
					
					//(int) ((0.5 * (Constant.GRAVITY * t * t)
					craft.setVerticalSpeed((float) Constant.GRAVITY);	//set vertical speed of the craft
					
					/** Reference to John Casey's collision detection code*/
					
					//Detect bottom left and bottom right point collisions with the terrain
					boolean bottomLeft = contains(terrain.getSurfaceX(), terrain.getSurfaceY(), craft.getXButtomLeft(), craft.getYButtomLeft());
					boolean bottomRight = contains(terrain.getSurfaceX(), terrain.getSurfaceY(), craft.getXButtomRight(), craft.getYButtomRight());
					boolean bottomMid = contains(terrain.getSurfaceX(), terrain.getSurfaceY(), craft.getXButtomMid(), craft.getYButtomMid());
					
					//If any point hit the terrain, means collision.
					//Check if it's successful landing or crash
					if (bottomLeft || bottomRight || bottomMid)
					{
						t = Constant.INITAL_TIME; // reset the time variable						
						gameOver = true;			//End the game
						
						if(bottomLeft && bottomRight && bottomMid){	//successful landing 
							Log.i("state", "not crash");
							craft.drawBody(canvas, paint);							
						}else{		//crash
							Log.i("state", "crash");
							
							terrain.drawCrater(craft.getXButtomMid(), craft.getYButtomMid(), canvas, paint);	//draw crater
							
							craft.drawCrashBody(canvas, paint);	//draw body of crashed craft
														
							soundPool.play(1, 1, 1, 0, 0, 1);	//play crash audio
						}
					}else {		//update the position of craft
						
						//set Y coordinate using h = 0.5 * v * t
						craft.setYCoordinate(craft.getYCoordinate() + (int) (0.5 * (craft.getVerticalSpeed() * t)));
						//set X coordinate using h = v * t
						craft.setXCoordinate(craft.getXCoordinate() + (int) ((craft.getHorizontalSpeed() * t)));
						craft.drawBody(canvas, paint);
					}
					
					craft.updateCoordinate();	// update the coordinate since X and Y changed
					
					//check which thruster should be drawn
					if(drawRightThruster){
						craft.drawRightThruster(canvas, paint);
						drawRightThruster = false;
					}
					if(drawMainThruster){
						craft.drawMainThruster(canvas, paint);
						drawMainThruster = false;
					}
					if(drawLeftThruster){
						craft.drawLeftThruster(canvas, paint);
						drawLeftThruster = false;
					}
										
					fuelBar.setProgress(craft.getFuelLevel());	//update the fuel gauge					
				}
				
				try {
					Thread.sleep(Constant.REFRESH_RATE);	//Refresh every 10ms.
				} catch (Exception e) {
				
				}finally {
					if (canvas != null){
						holder.unlockCanvasAndPost(canvas);	//unlock canvas
					}
				}	
			}
					
		}
		
	}
	
	/**
	 * Initialize the game, add action listener, 
	 */
	
	@SuppressLint("ClickableViewAccessibility")
	private void initialize() {
		createTerrain();	//Initialize and create new terrain
		createSpacecraft();	//Initialize and create new space craft
		setOnTouchListener(this);	
		getHolder().addCallback(this);	
		gameOver = false;
		t = Constant.INITAL_TIME; // reset the time variable
		
	}
	
	/**
	 * Create space craft
	 * @see SpaceCraft
	 */
	public void createSpacecraft(){
		craft = new SpaceCraft(getResources());
		//craft.draw(canvas);
	}
	
	/**
	 * Create Terrain
	 * @see Terrain
	 */
	public void createTerrain(){
		terrain = new Terrain(getResources());
	}

	/**
	 * reset everything
	 */
	public void reset() {
		initialize();		
	}

	/**
	 * Called when left button pressed.
	 * It checks if it runs out of fuel, if not, change the craft's speed, then draw thruster
	 */
	public void moveLeft() {
		if(!craft.isOutofFuel()){
			craft.setHorizontalSpeed(Constant.SPEED_INCREASE);
			craft.fire(Constant.SIDE_FIRE_USAGE);
			drawLeftThruster = true;
		}
		//Log.i("left", "left");
	}

	/**
	 * Called when right button pressed.
	 * It checks if it runs out of fuel, if not, change the craft's speed, then draw thruster
	 */
	public void moveRight() {
		if(!craft.isOutofFuel()){
			craft.setHorizontalSpeed(Constant.SPEED_DECREASE);
			craft.fire(Constant.SIDE_FIRE_USAGE);
			drawRightThruster = true;
		}
	}

	/**
	 * Called when up button pressed.
	 * It checks if it runs out of fuel, if not, change the craft's speed, then draw thruster
	 */
	public void moveUp() {
		if(!craft.isOutofFuel()){
			craft.setVerticalSpeed(Constant.SPEED_DECREASE);
			craft.fire(Constant.MAIN_FIRE_USAGE);
			drawMainThruster = true;
		}
	}

	/**
	 * Initialize the fuel gauge, set it to the max storage.
	 * @param fuelBar	Fuel gauge progress bar
	 */
	public void controlProgress(ProgressBar fuelBar) {
		this.fuelBar = fuelBar;
		this.fuelBar.setProgress(Constant.FUEL);		
	}	
	
	/**
	 * Reference to John Casey's collision detection code
	 * Collision detection
	 * @param xcor	Surface array of X coordinate
	 * @param ycor	Surface array of Y coordinate
	 * @param x0	Point's X Coordinate
	 * @param y0	Point's Y Coordinate
	 * @return	If the surface contains the point
	 */
	public boolean contains(int[] xcor, int[] ycor, double x0, double y0) {
		int crossings = 0;

		for (int i = 0; i < xcor.length - 1; i++) {
			int x1 = xcor[i];
			int x2 = xcor[i + 1];

			int y1 = ycor[i];
			int y2 = ycor[i + 1];

			int dy = y2 - y1;
			int dx = x2 - x1;

			double slope = 0;
			if (dx != 0) {
				slope = (double) dy / dx;
			}

			boolean cond1 = (x1 <= x0) && (x0 < x2); // is it in the range?
			boolean cond2 = (x2 <= x0) && (x0 < x1); // is it in the reverse
														// range?
			boolean above = (y0 < slope * (x0 - x1) + y1); // point slope y - y1

			if ((cond1 || cond2) && above) {
				crossings++;
			}
		}
		//If it is even, it is outside the terrain; If it is odd, it is inside the terrain
		return (crossings % 2 != 0); // even or odd
	}
}

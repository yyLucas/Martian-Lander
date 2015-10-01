package unitec.lucas.model;

import unitec.lucas.martianlander.Constant;
import unitec.lucas.martianlander.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Space craft class, handle everything that belongs to the craft, control the craft's behavior.
 * @author Yang Zhang (Lucas)
 *
 */
public class SpaceCraft {
	
	private float verticalSpeed;
	private float horizontalSpeed;
	
	private float xCoordinate;
	private float yCoordinate;
	
	private float buttomLeftX;
	private float buttomLeftY;
	
	private float buttomRightX;
	private float buttomRightY;
	
	private float buttomMidX;
	private float buttomMidY;
	
	private int fuel;
	private boolean fuelEmpty;
	
	private Resources resources;
	private Bitmap craftTexture;
	private Bitmap thrusterTexture;
	private Bitmap mainThrusterTexture;
	private Bitmap crashCraft;
	
	/**
	 * Initialize space craft, set speed and spawn point to it.
	 * @param resources
	 */
	public SpaceCraft(Resources resources){
		//initialize textures
		this.resources = resources;
		craftTexture = getTexture(R.drawable.craftmain, Constant.CRAFT_SIZE);
		thrusterTexture = getTexture(R.drawable.thruster, Constant.SIDE_THRUSTER_SIZE);
		mainThrusterTexture = getTexture(R.drawable.main_flame, Constant.MAIN_THRUSTER_SIZE);
		crashCraft = getTexture(R.drawable.lander_crashed, Constant.CRAFT_SIZE);
		
		//initialize speed and coordinates
		verticalSpeed = 0;
		horizontalSpeed = 0;
		
		xCoordinate = Constant.CANVAS_WIDTH/2;
		yCoordinate = 0;
		updateCoordinate();	//calculate bottom coordinates
		
		fuel = Constant.FUEL;
		fuelEmpty = false;
		
	}
	
	/**
	 * Update coordinate used in the craft, used to detect collision and draw thrusters.
	 */
	public void updateCoordinate(){
		buttomLeftX = xCoordinate - getTextureWidth(craftTexture)/2;
		buttomLeftY = yCoordinate + getTextureHeight(craftTexture);
		
		buttomRightX = xCoordinate + getTextureWidth(craftTexture)/2;
		buttomRightY = yCoordinate + getTextureHeight(craftTexture);
		
		buttomMidX = xCoordinate;
		buttomMidY = yCoordinate + getTextureHeight(craftTexture);
	}
	
	/**
	 * Get current fuel level
	 * @return fuel How many fuel left
	 */
	public int getFuelLevel(){
		return fuel;
	}
	
	/**
	 * Called when firing thruster, change the fuel level
	 * @param usage	How many fuel used in one action
	 */
	public void fire(int usage){
		fuel -= usage;
	}
	
	/**
	 * Check if the fuel is used up
	 * @return true is used up; false is not
	 */
	public boolean isOutofFuel(){
		if(fuel <= 0){
			fuelEmpty = true;
		}
		else{
			fuelEmpty = false;
		}
		return fuelEmpty;
	}
	
	/**
	 * Get X coordinate of bottom mid point of craft
	 * @return buttomMidX
	 */
	public float getXButtomMid(){
		return buttomMidX;
	}
	
	/**
	 * Get Y coordinate of bottom mid point of craft
	 * @return buttomMidX
	 */
	public float getYButtomMid(){
		return buttomMidY;
	}
	
	/**
	 * Get X coordinate of bottom left point of craft
	 * @return buttomLeftX
	 */
	public float getXButtomLeft(){
		return buttomLeftX;
	}
	
	/**
	 * Get X coordinate of bottom right point of craft
	 * @return buttomRightX
	 */
	public float getXButtomRight(){
		return buttomRightX;
	}
	
	/**
	 * Get Y coordinate of bottom left point of craft
	 * @return buttomLeftY
	 */
	public float getYButtomLeft(){
		return buttomLeftY;
	}
	
	
	/**
	 * Get Y coordinate of button right point of craft
	 * @return buttomRightY
	 */
	public float getYButtomRight(){
		return buttomRightY;
	}
	
	/**
	 * Get X coordinate of the craft
	 * @return xCoordinate X coordinate
	 */
	public float getXCoordinate(){
		return xCoordinate;
	}
	
	/**
	 * Get Y coordinate of the craft
	 * @return yCoordinate Y coordinate
	 */
	public float getYCoordinate(){
		return yCoordinate;
	}
	
	/**
	 * Set X coordinate of the craft
	 * @param xCoordinate X coordinate
	 */
	public void setXCoordinate(float x){
		xCoordinate = x;
	}
	
	/**
	 * Set Y coordinate of the craft
	 * @param yCoordinate Y coordinate
	 */
	public void setYCoordinate(float y){
		yCoordinate = y;
	}
		
	/**
	 * Get texture width 
	 * @param texture 
	 */
	private int getTextureWidth(Bitmap texture) {
		return texture.getWidth();
	}
	
	/**
	 * Get texture height 
	 * @param texture 
	 */
	private int getTextureHeight(Bitmap texture) {
		return texture.getHeight();
	}
	
	/**
	 * Decode picture to bitmap, and scale it to desirable size
	 * @param pic	
	 * @param size
	 * @return texture Bitmap image
	 */
	private Bitmap getTexture(int pic, int size) {
		Bitmap texture = BitmapFactory.decodeResource(resources, pic);	//decode picture to bitmap
		texture = Bitmap.createScaledBitmap(texture, size, size, true);	//scale bitmap
		return texture;
	}
	
	/**
	 * Get vertical speed of the craft
	 * @return verticalSpeed
	 */
	public float getVerticalSpeed(){
		return verticalSpeed;		
	}
	
	/**
	 * Set vertical speed of the craft
	 * @param speedDecrease Changes in vertical speed
	 */
	public void setVerticalSpeed(double speedDecrease) {
		verticalSpeed += speedDecrease;
		// Control the speed vertical speed within -1, or it will raise too fast
		//Just used to keep the game hard and interesting
		if(verticalSpeed < -1){
			verticalSpeed = -1;
		}
	}
	
	/**
	 * Get horizontal speed of the craft
	 * @return horizontalSpeed
	 */
	public float getHorizontalSpeed(){
		return horizontalSpeed;		
	}
	
	/**
	 * Set horizontal speed of the craft
	 * @param speedDecrease Changes in horizontal speed
	 */
	public void setHorizontalSpeed(double speedDecrease) {
		horizontalSpeed += speedDecrease;
	}
	
	/**
	 * Draw left thruster
	 * @param canvas
	 * @param paint
	 */
	public void drawLeftThruster(Canvas canvas, Paint paint){
		canvas.drawBitmap(thrusterTexture, buttomLeftX, buttomLeftY, paint);	//Using unique texture and spawn point
	}
	
	/**
	 * Draw right thruster
	 * @param canvas
	 * @param paint
	 */
	public void drawRightThruster(Canvas canvas, Paint paint){
		canvas.drawBitmap(thrusterTexture, buttomRightX - getTextureWidth(thrusterTexture), buttomRightY, paint);
	}
	
	/**
	 * Draw main thruster
	 * @param canvas
	 * @param paint
	 */
	public void drawMainThruster(Canvas canvas, Paint paint){
		canvas.drawBitmap(mainThrusterTexture, buttomMidX-getTextureWidth(mainThrusterTexture)/2, buttomMidY, paint);
	}
	
	/**
	 * Draw body of the space craft
	 * @param canvas
	 * @param paint
	 */
	public void drawBody(Canvas canvas, Paint paint){
		wrapScreen(canvas);
		//set initial point to top middle of the screen
		canvas.drawBitmap(craftTexture, xCoordinate - getTextureWidth(craftTexture)/2, yCoordinate, paint);
	}
	
	/**
	 * Allow space craft to wrap screen
	 * @param canvas
	 */
	public void wrapScreen(Canvas canvas){
		if(xCoordinate >= canvas.getWidth()){	//if it goes to the right boundary, let it through from the left side
			xCoordinate = 0;
		}else if(xCoordinate <= 0){
			xCoordinate = canvas.getWidth();	//if it goes to the left boundary, let it through from the right side
		}
	}
	/**
	 * Draw crash body at point crashed
	 * @param canvas
	 * @param paint
	 */
	public void drawCrashBody(Canvas canvas, Paint paint) {
		canvas.drawBitmap(crashCraft, xCoordinate - getTextureWidth(crashCraft)/2, yCoordinate, paint);		
	}

}

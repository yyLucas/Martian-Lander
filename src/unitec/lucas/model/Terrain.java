package unitec.lucas.model;

import java.util.ArrayList;
import java.util.Collections;

import unitec.lucas.martianlander.Constant;
import unitec.lucas.martianlander.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader.TileMode;
import android.util.Log;

/**
 * Randomly create terrain
 * @author Yang Zhang (Lucas)
 *
 */
public class Terrain {

	private Path path;
	private Path crater;
	int xCoordinate[];
	int yCoordinate[];
	
	ArrayList<Integer> surfaceX = new ArrayList<Integer>();
	ArrayList<Integer> surfaceY = new ArrayList<Integer>();
	
	Resources resource;
	
	//int xcor[] = new int[Constant.TERRAIN_SIZE]; using when randomly generate the terrain
	//int ycor[] = new int[Constant.TERRAIN_SIZE];
	
		
	/**
	 * Initialize the terrain
	 * @param resources
	 * @see randomTerrain()
	 * @see create()
	 */
	
	public Terrain(Resources resources) {
		path = new Path();
		this.resource = resources;
		randomTerrain();	//Randomized the surface
		create();
			
	}
	
	/**
	 * Get array of X coordinate
	 * @return xCoordinate
	 */
	public int[] getSurfaceX(){
		return xCoordinate;
	}
	
	/**
	 * Get array of X coordinate
	 * @return xCoordinate
	 */
	public int[] getSurfaceY(){
		return yCoordinate;
	}

	/**
	 * Create the terrain path according to the coordinate.
	 */
	public void create(){		
		for (int i = 0; i < xCoordinate.length; i++) {
			path.lineTo(xCoordinate[i], yCoordinate[i]);	//connect the path with coordinate
		}		
	}
	
	/**
	 * Create the x coordinate of the terrain
	 */
	
	private void createSurfaceX() {		
		for(int i=1; i<Constant.TERRAIN_SURFACE_SIZE; i++){
			surfaceX.add((int)(1+Math.random()*720));
			//Log.i("xCor", ""+surfaceX.get(i));
		}
		Collections.sort(surfaceX, Collections.reverseOrder());	//sort the x coordinate
		//int safeLandingIndex = (int) (Math.random()*Constant.TERRAIN_SURFACE_SIZE);
		
	}
	
	/**
	 * Generate safe landing spot for the craft
	 * @return whether there is a sage landing spot
	 */
	
	private boolean generateLandingTerrain() {
		for(int i=1; i<surfaceX.size()-1; i++){
			//Log.i("Distance", ""+Math.abs(surfaceX.get(i+1)-surfaceX.get(i)));
			//Set flat space if the distance is between 75 to 150
			if(Math.abs(surfaceX.get(i+1)-surfaceX.get(i)) < Constant.TERRAIN_LARGE_SPACE &&
					Math.abs(surfaceX.get(i+1)-surfaceX.get(i)) > Constant.TERRAIN_SMALL_SPACE){
				Log.i("Land", "success");
				surfaceY.set(i+1, surfaceY.get(i));	//set safe landing spot
				return true;
			}
		}
		return false;
		
	}

	/**
	 * Create the y coordinate of the terrain
	 */
	private void createSurfaceY() {
		for(int i=1; i<Constant.TERRAIN_SURFACE_SIZE; i++){
			surfaceY.add((int)(800+Math.random()*(1000-800)));
			//Log.i("yCor", ""+surfaceY.get(i));
		}
	}

	/** 
	 * Randomly generate the terrain, check if there is a safe landing spot. If it's not, re-create one.
	 */
	private void randomTerrain() {
		createSurfaceX();	//create X coordinate of the surface
		createSurfaceY();	//create y coordinate of the surface
		//if there is no flat landing space, re-generate the surface
		if(!generateLandingTerrain()){
			randomTerrain();
		}
		/** points for the terrain path*/
		xCoordinate = new int[surfaceX.size()+5];
		yCoordinate = new int[surfaceX.size()+5];
		
		/** Boundary of the terrain path*/
		xCoordinate[0] = 0;
	    yCoordinate[0] = Constant.CANVAS_HEIGHT;
	    
	    xCoordinate[1] = Constant.CANVAS_WIDTH;
	    yCoordinate[1] = Constant.CANVAS_HEIGHT;
	    
	    xCoordinate[2] = Constant.CANVAS_WIDTH;
	    yCoordinate[2] = 950;
	    
	    //add the randomized terrain surface to array.
	    for (int i=0; i < surfaceX.size(); i++)
	    {
	        xCoordinate[i+3] = surfaceX.get(i).intValue();
	        yCoordinate[i+3] = surfaceY.get(i).intValue();
	    }
	    
	    xCoordinate[xCoordinate.length-2] = 0;
	    yCoordinate[xCoordinate.length-2] = 960;
	    
	    xCoordinate[xCoordinate.length-1] = 0;
	    yCoordinate[xCoordinate.length-1] = Constant.CANVAS_HEIGHT;
	    
	    
	    
//		xCoordinate = new int[] {0, Constant.CANVAS_WIDTH, Constant.CANVAS_WIDTH, 700, 660, 550, 480,
//				460, 430, 300, 300, 230, 0, 0};
//		yCoordinate = new int[] {Constant.CANVAS_HEIGHT, Constant.CANVAS_HEIGHT, 950, 820, 800, 800, 900,
//				990, 970, 970, 920, 850, 960, Constant.CANVAS_HEIGHT};				
	}
	/**
	 * Draw Terrain
	 * @param canvas
	 * @param paint
	 */
	public void draw(Canvas canvas, Paint paint){
		//draw terrain
		Bitmap bitmap = BitmapFactory.decodeResource(resource, R.drawable.mars);
		//create a bitmap shader, so the paint can set to the texture
		BitmapShader mBitmapShader = new BitmapShader(bitmap, TileMode.REPEAT, TileMode.REPEAT);	
		
		paint.setShader(mBitmapShader);	//set bitmap shader to the paint
		canvas.drawPath(path, paint);	//draw terrain
	}
	
	/**
	 * Draw crater
	 * @param x X coordinate of central point of crater
	 * @param y Y coordinate of central point of crater
	 * @param canvas
	 * @param paint
	 */
	public void drawCrater(float x, float y, Canvas canvas, Paint paint){
		paint.setShader(null);
		paint.setColor(Color.BLACK);
		canvas.drawCircle(x, y, Constant.CRATER_SIZE, paint);		
	}
	
}

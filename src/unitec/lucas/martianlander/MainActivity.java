package unitec.lucas.martianlander;

import unitec.lucas.martianlander.R;
import unitec.lucas.game.GameLoop;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Called when activity first created. Shows the initial layout 
 * and add action listener to the restart button.
 * @author Yang Zhang (Lucas)
 *
 */

public class MainActivity extends Activity {
	private GameLoop gameLoop;
	ProgressBar fuelBar;
	
	/**
	 * Called when first created. Initialize the activity.
	 * Reference to John Casey's project.
	 * 
	 * @param  savedInstanceState 
	 * @see GameLoop 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); //hide the title 
		setContentView(R.layout.activity_main); // set the content view or our widget lookups will fail
        
        gameLoop = (GameLoop)findViewById(R.id.gameLoop);	//main game fram
        fuelBar = (ProgressBar)findViewById(R.id.fuelProgressBar);	//progress bar
        gameLoop.controlProgress(fuelBar);	//initial the progress bar
        
        
        final Button btnRestart = (Button)findViewById(R.id.btnRestart);
        //add action listener to the restart button
        btnRestart.setOnClickListener(new OnClickListener()
        {	
			@Override
			public void onClick(View v)
			{
				gameLoop.reset();	//restart the game
				gameLoop.invalidate();	//re-draw
			}
		}); 
        
	}
	
	/**
	 * Handle button listener, control the speed and direction of space craft.
	 * @param v Surface View
	 */
	
	public void performAction(View v) {
		switch (v.getId()) {
		case R.id.btnLeft:
			gameLoop.moveLeft();	//control left button pressed			
			break;
		case R.id.btnRight:
			gameLoop.moveRight();	//control right button pressed		
			break;
		case R.id.btnUp:
			gameLoop.moveUp();		//control up button pressed
			break;				
		}
	}

}

package org.omnirom.games.eggs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

	private EggGame eggGame;
	private GameGraphics gameGraphics;
	private SoundHandler soundHandler;
	private TextView bestScoreView;
	private SharedPreferences pref;
	private Dialog overDialog;
	private boolean gameOverBool;
	private int bestScore;
	public boolean soundsOn;
	protected ImageView reset_btn;
	private List<Drawable> mBackgrounds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBackgrounds = new ArrayList<Drawable>();
		mBackgrounds.add(getResources().getDrawable(R.drawable.bg_gradient));
		mBackgrounds.add(getResources().getDrawable(R.drawable.bg_gradient_1));
		mBackgrounds.add(getResources().getDrawable(R.drawable.bg_gradient_2));
		mBackgrounds.add(getResources().getDrawable(R.drawable.bg_gradient_3));

		getGamePreferences();

		gameGraphics = new GameGraphics(this);
		soundHandler = new SoundHandler(this, soundsOn);
		gameGraphics.initializeGameGraphics();
		//soundHandler.initializeSoundFx();
		
		gameOverBool = false;
		reset_btn = (ImageView) findViewById(R.id.btn_reset);
		bestScoreView = (TextView) findViewById(R.id.best_view);
		updateBest();

		eggGame = new EggGame(this, gameGraphics, soundHandler);
		eggGame.startGame();
		Log.d("OnCreate", "started game");
		
		//reset button
		reset_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				eggGame.stopGame();
				eggGame.resetGame();
				gameOverBool = false;
			}
		});

	}// end OnCreate

	private void getGamePreferences() {
	
		//get existing high score
		pref = this.getSharedPreferences("ca.gsalisi.eggs", Context.MODE_PRIVATE);
		bestScore = pref.getInt("best", 0);
		soundsOn = pref.getBoolean("soundfx", true);
				
	}

	/*cancel timers when the window is closed-- when 
	**back button or home button is pressed to prevent
	**eggs falling even when out of game*/
	@Override
	protected void onStop() {
		super.onStop();
		if(!gameOverBool){
			try{
				eggGame.stopGame();
				Log.d("On Stop", "Called cancel timers");
			} catch(Exception e) {
				Log.d("On Stop", "exception caught");
			}
		}else{
			overDialog.dismiss();
		}
		finish();
		
		
	}//end onStop()
	

	
	//show game over dialog
	public void gameOver() {
		
		gameOverBool = true;
		
		if(eggGame.scoreCount > bestScore){//save high score if it is beaten
			bestScore = eggGame.scoreCount;
			pref = this.getSharedPreferences("ca.gsalisi.eggs", Context.MODE_PRIVATE);
			Editor editor = pref.edit();
			editor.putInt("best", bestScore);
			editor.commit();
			updateBest();
		}
		
		eggGame.stopGame();
		
		//creates dialog
		overDialog = new Dialog(MainActivity.this, R.style.CustomDialog);
		overDialog.setContentView(R.layout.game_over);

		TextView tv = new TextView(this);
		tv.setText("Game Over!");

		overDialog.setTitle("Game Over");
		
		TextView scoreOver = (TextView) overDialog.findViewById(R.id.score_view2);
		TextView bestOver = (TextView) overDialog.findViewById(R.id.best_view2);
		
		scoreOver.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
		bestOver.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);

		scoreOver.setText(gameGraphics.getScoreView().getText().toString());
		bestOver.setText(bestScoreView.getText().toString());

		ImageView restartbtn = (ImageView) overDialog.findViewById(R.id.btn_gameover);
		restartbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				overDialog.dismiss();
				eggGame.resetGame();
				gameOverBool = false;
			}
		});
		overDialog.show();
		
		overDialog.setOnCancelListener(new Dialog.OnCancelListener(){

			@Override
			public void onCancel(DialogInterface arg0) {
				gameOverBool = true;
			}
		});
		
	}//end of gameOver()
	
	//updates best score view
	protected void updateBest() {
		bestScoreView.setText("Best: " + bestScore);
	}

	public void updateBackground(int level) {
		findViewById(R.id.main).setBackground(mBackgrounds.get(level%mBackgrounds.size()));
	}
}
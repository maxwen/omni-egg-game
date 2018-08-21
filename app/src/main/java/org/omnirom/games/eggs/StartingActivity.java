package org.omnirom.games.eggs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class StartingActivity extends Activity {
	 
	private boolean soundOn;
	private SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starting);
		
		//loads animation to chicken
		Animation swayingAnim = AnimationUtils.loadAnimation(this, R.anim.sway_main);
		ImageView playButton = (ImageView) findViewById(R.id.btn_play);
		playButton.startAnimation(swayingAnim);
		
		//get sounds preferences
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		soundOn = false; //pref.getBoolean("soundfx", false);
		
		//play button click listener
		playButton.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(StartingActivity.this,
						MainActivity.class);
				if(Build.VERSION.SDK_INT > 14){
					Bundle translateBundle = ActivityOptions.makeCustomAnimation(
							StartingActivity.this, R.anim.slide_in_left, R.anim.slide_out_left).toBundle();
					startActivity(intent, translateBundle);
				}else{
					startActivity(intent);

				}

			}
		});
		
		//sounds toggle view
		final ImageView btn_settings = (ImageView) findViewById(R.id.btn_soundfx);
		if(soundOn){
			btn_settings.setImageResource(R.drawable.ic_volume_on);
		}else{
			btn_settings.setImageResource(R.drawable.ic_volume_off);
		}
		//sounds toggle listener
		btn_settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//also edits application preference
				if(soundOn){
					soundOn = false;
					Editor editor = pref.edit();
					editor.putBoolean("soundfx", false);
					editor.commit();
					btn_settings.setImageResource(R.drawable.ic_volume_off);
					
				}else{
					soundOn = true;
					Editor editor = pref.edit();
					editor.putBoolean("soundfx", true);
					editor.commit();
					btn_settings.setImageResource(R.drawable.ic_volume_on);
				}
			}
		});
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	

}

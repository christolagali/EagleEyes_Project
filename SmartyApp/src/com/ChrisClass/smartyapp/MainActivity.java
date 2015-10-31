package com.ChrisClass.smartyapp;


import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.speech.tts.TextToSpeech;


public class MainActivity extends ActionBarActivity implements TextToSpeech.OnInitListener{

	private TextToSpeech tts;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_main);
		
		 tts = new TextToSpeech(getBaseContext(), this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onClickme(View view)
	{
		
		tts.speak("Welcome to Eagle Eyes", TextToSpeech.QUEUE_FLUSH, null);
		startActivity(new Intent("com.ChrisClass.smartyapp.SecondActivity"));
	}

	
	
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
			tts.setLanguage(Locale.UK);
			
	}
	



	protected void onDestroy()
	{
		super.onDestroy();
		if(tts != null)
		{
			tts.stop();
			tts.shutdown();
			
		}
		
	}
	
	@SuppressWarnings("static-access")
	protected void onResume()
	{
		super.onResume();
		tts.speak("Application has paused", tts.QUEUE_FLUSH, null);
		
	}
	
	
	protected void onStart()
	{
		super.onStart();
		tts.speak("Welcome to Eagle Eyes", TextToSpeech.QUEUE_FLUSH, null);
	}
	
}

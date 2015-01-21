package com.milton.gotcha;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class FailedConfirmation extends Activity {
	android.widget.Spinner spinner;

	DataFetcher myFetcher;
	String myId;
	List<String> names; 
	List<String> realNames;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_failed_confirmation);
		Intent intent = getIntent();
		myId = intent.getExtras().getString("id");
		Integer tries = intent.getExtras().getInt("tries"); 
		
		spinner = (android.widget.Spinner)findViewById(R.id.spinner1);
		myFetcher = new DataFetcher(myId);
		myFetcher.tries = tries;
		myFetcher.setTries();
		if(tries >= 3)
		{
				String msg = "You're out of tries! Please contact us if this is a mistake.";
	    		Intent Failintent = new Intent(this, OutPage.class);
	    		Failintent.putExtra("msg", msg);
	    		startActivity(Failintent);
		}
		TextView triesLeft =(TextView)findViewById(R.id.tLeft);
	    triesLeft.setText("You have " + Integer.toString(3-tries) + " more tries, don't mess up!");

	    names = myFetcher.getList();
	    realNames = new ArrayList<String>();
        for(String f:names)
        {
        	realNames.add(myFetcher.getName(f));
        }
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, realNames);
	    spinner.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_failed_confirmation, menu);
		return true;
	}
	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onPause()
	{
	    super.onPause();
	    finish();
	}
	public void sendMessage(View view) {
    	String newTarget = spinner.getSelectedItem().toString();
    	newTarget = names.get(realNames.indexOf(newTarget));
    	myFetcher.newTarget = newTarget;
    	
    	if(myFetcher.tagOut())
    	{
    		String msg = "Thank you for confirming your new target, good luck!";
    		Intent intent = new Intent(this, OutPage.class);
    		intent.putExtra("msg", msg);
    		startActivity(intent);
    		finish();
    	}
    	else
    	{
    		String id = myId;
    		Integer newtries = myFetcher.tries+1;
    		Intent intent = new Intent(this, FailedConfirmation.class);
    		intent.putExtra("tries", newtries);
    		intent.putExtra("id", id);
    		startActivity(intent);
    		finish();
    	}
    }
}

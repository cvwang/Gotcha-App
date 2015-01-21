package com.milton.gotcha;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class OutPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_out_page);
		Intent intent = getIntent();
		String msg = intent.getExtras().getString("msg");
		 TextView message =(TextView)findViewById(R.id.message); 
	        message.setText(msg);
		
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_out_page, menu);
		return true;
	}

}
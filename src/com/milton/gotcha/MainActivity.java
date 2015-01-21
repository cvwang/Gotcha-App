package com.milton.gotcha;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {
	android.widget.Spinner spinner;
	DataFetcher myFetcher;
	String myId, timeStamp;
	List<String> realNames;
	List<String> names;
	List<Integer> topTen;
	int numberOut, percentOut, total, numberTagged, second, minute, hour, day;
	long unixTime;
	int[] dateInfo = new int[4];
	//String[] topTen = new String[10];
	StringBuilder builder = new StringBuilder();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (android.widget.Spinner)findViewById(R.id.spinnerFirst);
        
        myId = "charles_wang13@milton.edu";
        //myId = "nathaniel_bresnick14@milton.edu";
        //myId = "neo@milton.edu";
        //myId = "morpheus@milton.edu";
        myFetcher = new DataFetcher(myId);
        String target = myFetcher.getTarget();
        
        if(target.equals("OUT"))
        {
        	dateInfo = myFetcher.getTimeSurvived();
        	String msg = "You're out of tries! You lasted for "  + dateInfo[3] + " days " + dateInfo[2] + " hrs " + dateInfo[1] + " min " + dateInfo[0] + " sec. " + "Please contact us if you have any concerns.";
        	//String msg = "You've been tagged out! Please contact us if this is a mistake.";
    		Intent Failintent = new Intent(this, OutPage.class);
    		Failintent.putExtra("msg", msg);
    		startActivity(Failintent);  
    		finish();
        }
        if(myFetcher.getTries() >= 3)
        {
        	dateInfo = myFetcher.getTimeSurvived();
        	String msg = "You're out of tries! You lasted for "  + dateInfo[3] + " days " + dateInfo[2] + " hrs " + dateInfo[1] + " min " + dateInfo[0] + " sec. " + "Please contact us if this is a mistake.";
        	Intent Failintent = new Intent(this, OutPage.class);
        	Failintent.putExtra("msg", msg);
        	startActivity(Failintent);   
        	finish();
        }
        TextView yourName =(TextView)findViewById(R.id.yourName);
        yourName.setText("Hey " + myFetcher.getName(myId).split(" ")[0] + "!");
        TextView targetName =(TextView)findViewById(R.id.targetName); 
        targetName.setText(myFetcher.getName(target));
        names = myFetcher.getRandom();
        realNames = new ArrayList<String>();
        
        numberOut = myFetcher.getOut();
        TextView textView6 =(TextView)findViewById(R.id.textView6);
        textView6.setText("Number of people out: " + numberOut);
        
        total = myFetcher.getTotal();
        TextView textView7 = (TextView)findViewById(R.id.textView7);
        textView7.setText("Total number of people: " + total);
        
        percentOut = (int)(100*numberOut/total);
        TextView textView8 = (TextView)findViewById(R.id.textView8);
        textView8.setText("Percentage of people out: " + percentOut + "%");
        
        numberTagged = myFetcher.numberTagged();
        TextView textView9 = (TextView)findViewById(R.id.textView9);
        textView9.setText("Number of people you've tagged: " + numberTagged);
        
        dateInfo = myFetcher.getTime();
        TextView textView10 = (TextView)findViewById(R.id.textView10);
        textView10.setText("Time Survived: " + dateInfo[3] + " days " + dateInfo[2] + " hrs " + dateInfo[1] + " min " + dateInfo[0] + " sec ");
        
        topTen = myFetcher.topTen();
        Iterator it = topTen.iterator();
        TextView textView11 = (TextView)findViewById(R.id.textView11);
        int counter = 1;
        while(it.hasNext())
        {
        	int value = Integer.parseInt(it.next().toString());
        	System.out.println(value);
        	builder.append(counter + ": " + value + "; ");
        	counter++;
        }
        textView11.setText(builder.toString());

        for(String f:names)
        {
        	realNames.add(myFetcher.getName(f));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, realNames);
				 
        spinner.setAdapter( adapter );
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
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
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
    	}
    	else
    	{
    		String id = myId;
    		Integer tries = 1;
    		Intent intent = new Intent(this, FailedConfirmation.class);
    		intent.putExtra("tries", tries);
    		intent.putExtra("id", id);
    		startActivity(intent);
    	}
    	
    }
    
}

package com.project.waverr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class Home extends Activity implements OnClickListener{

	ImageButton ib1,ib2;
	TabHost th;
	TextView tv;
	TextView x;
	
	String cityName;
	LocationManager locationManager;
	Criteria criteria;
	LocationGiver provider;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home);
		
		provider = new LocationGiver(getBaseContext());
		
		ib1=(ImageButton)findViewById(R.id.cuisine1);
		ib2=(ImageButton)findViewById(R.id.cuisine2);
		th=(TabHost)findViewById(R.id.tabhost);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		criteria = new Criteria();
		
		th.setup();
		TabSpec specs = th.newTabSpec("Search");
		specs.setContent(R.id.tab1);
		specs.setIndicator("Search");
		th.addTab(specs);
		x = (TextView) th.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
	    x.setTextSize(15);
	    x.setTextColor(Color.parseColor("#424242"));
		
	    specs = th.newTabSpec("Nearby");
		specs.setContent(R.id.tab2);
		specs.setIndicator("Nearby");
		th.addTab(specs);
		x = (TextView) th.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
	    x.setTextSize(15);
	    x.setTextColor(Color.parseColor("#424242"));
		
	    specs = th.newTabSpec("New");
		specs.setContent(R.id.tab3);
		specs.setIndicator("New");
		th.addTab(specs);
		x = (TextView) th.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
	    x.setTextSize(15);
	    x.setTextColor(Color.parseColor("#424242"));
		
	    specs = th.newTabSpec("BestDeals");
		specs.setContent(R.id.tab4);
		specs.setIndicator("Best Deals");
		th.addTab(specs);
		x = (TextView) th.getTabWidget().getChildAt(3).findViewById(android.R.id.title);
	    x.setTextSize(15);
	    x.setTextColor(Color.parseColor("#424242"));
		ib1.setOnClickListener(this);
		ib2.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent c1,c2;
		switch(arg0.getId())
		{
		case R.id.cuisine1:c1 = new Intent("com.project.waverr.CHINESECUISINE");
		startActivity(c1);
			break;
		case R.id.cuisine2:c2= new Intent("com.project.waverr.INDIANCUISINE");
		startActivity(c2);
			break;
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String city = provider.getLocation(locationManager, criteria);
		tv = (TextView) findViewById(R.id.cityname);
		tv.setText(city);
	}
}
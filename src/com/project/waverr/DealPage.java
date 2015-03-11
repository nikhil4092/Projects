package com.project.waverr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.project.waverr.SimpleGestureFilter.SimpleGestureListener;

public class DealPage extends GlobalActionBar implements OnTabChangeListener, OnMapReadyCallback, OnClickListener, SimpleGestureListener{

	TabHost th;
	TextView x,restname,dealtext;
	double latitude;
	double longitude;
	Button getDirections;
	ImageButton favouriteStatus,share;;
	String restaurantPhoneNumber;
	String restaurantName;
	GlobalClass global;
	Button timerText;
	String time;
	JSONObtainer obtainer;
	private SimpleGestureFilter detector;
	Button activate;
	String dtext;
	boolean login;
	Deal deal;
	
	DateTime start;
	DateTime end;
	CountDownTimer timer;
	/*Integer[] imageIDs = {
			 R.drawable.chinese1,R.drawable.ic_launcher,R.drawable.splash,R.drawable.chinese1};*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.deal_page);
		global = (GlobalClass) getApplication();

		Intent intent = getIntent();
		String dealString = intent.getStringExtra("deal");
		Gson gson = new Gson();
		if(dealString!=null)
		{
			//Toast.makeText(this, dealString, Toast.LENGTH_LONG).show();
			deal = gson.fromJson(dealString, Deal.class);
			//deal = global.getDeal();
			start = deal.getStartDateTime();
			end = deal.getEndDateTime();
			setLayout(deal);
		}
		else
			Toast.makeText(this, "Deal string is null", Toast.LENGTH_SHORT).show();

		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		ImagePagerAdapter adapter = new ImagePagerAdapter();
		viewPager.setAdapter(adapter);

		th=(TabHost)findViewById(R.id.tabhost1);

		findViewById(R.id.button_call).setOnClickListener(this);
		favouriteStatus = (ImageButton) findViewById(R.id.favourite_status);
		favouriteStatus.setOnClickListener(this);
		restname=(TextView)findViewById(R.id.placeName);
		dealtext=(TextView)findViewById(R.id.theDeal);
		restaurantPhoneNumber = "+918105563395";
		share=(ImageButton)findViewById(R.id.share);
		share.setOnClickListener(this);
		th.setup();
		TabSpec specs = th.newTabSpec("Deal");
		specs.setContent(R.id.dealtab1);
		specs.setIndicator("Deal");
		th.addTab(specs);
		x = (TextView) th.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
		x.setTextSize(15);
		x.setTextColor(Color.parseColor("#424242"));

		specs = th.newTabSpec("About");
		specs.setContent(R.id.dealtab2);
		specs.setIndicator("About");
		th.addTab(specs);
		x = (TextView) th.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
		x.setTextSize(15);
		x.setTextColor(Color.parseColor("#424242"));

		specs = th.newTabSpec("Images");
		specs.setContent(R.id.dealtab3);
		specs.setIndicator("Images");
		th.addTab(specs);
		x = (TextView) th.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
		x.setTextSize(15);
		x.setTextColor(Color.parseColor("#424242"));

		specs = th.newTabSpec("Location");
		specs.setContent(R.id.dealtab4);
		specs.setIndicator("Location");
		th.addTab(specs);
		x = (TextView) th.getTabWidget().getChildAt(3).findViewById(android.R.id.title);
		x.setTextSize(15);
		x.setTextColor(Color.parseColor("#424242"));

		for(int i=0;i<th.getTabWidget().getChildCount();i++){
			th.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_unselected_waverraccent);
			th.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_unselected_pressed_waverraccent);
		}
		th.getTabWidget().setCurrentTab(0);
		th.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_selected_waverraccent);
		th.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_selected_pressed_waverraccent);

		th.setOnTabChangedListener(this);

		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.the_map);
		mapFragment.getMapAsync(this);
		latitude = 13.0092;
		longitude = 74.7937;

		getDirections = (Button) findViewById(R.id.get_directions);
		getDirections.setOnClickListener(this);

		timerText = (Button) findViewById(R.id.deal_countdown_button);
		activate = (Button) findViewById(R.id.activatedeal);
		activate.setOnClickListener(this);
		//final String RestaurantName = deal.getRestaurantName();
		final int percentageDeal = deal.getPercentageDiscount();
		final int minamount = deal.getMinimumAmount();
		final int amountdiscount = deal.getAmountDiscount();
		final String canvastext	= deal.getCanvasText();
		final String freebie = deal.getFreebie();
		Bundle b=getIntent().getExtras();
		dtext="";
		if(canvastext.compareTo("")!=0&&canvastext!=null)
		{
			dtext=canvastext;
		}
		if(freebie.compareTo("")!=0&&freebie!=null)
		{
			dtext="Get "+freebie+" free on purchase of "+minamount;
		}
		if(amountdiscount!=0)
		{
			dtext="Get Rs."+amountdiscount+" off on a Minimum purchase of Rs."+minamount;
		}
		if(percentageDeal!=0)
		{
			dtext="Get "+percentageDeal+"% off on a Minimum purchase of Rs."+minamount;
		}
		login=b.getBoolean("login");
		if(login==false)
		{
			activate.setEnabled(false);
			activate.setBackgroundColor(Color.parseColor("#f1f1f1"));
		}

		restaurantName = "Hao Ming";
		if(global.isFavourited(restaurantName)) {
			//Toast.makeText(this, "It's a favourite!!", Toast.LENGTH_SHORT).show();
			favouriteStatus.setImageResource(R.drawable.favorite_full);
		}
		else {
			//Toast.makeText(this, "Nope!!", Toast.LENGTH_SHORT).show();
			favouriteStatus.setImageResource(R.drawable.favorite_empty);
		}

		detector = new SimpleGestureFilter(this, this);
		startTimer();
	}

	private class ImagePagerAdapter extends PagerAdapter {
		private int[] mImages = new int[] {
				R.drawable.chinese,
				R.drawable.ic_launcher,
				R.drawable.soup3,
				R.drawable.chinese1
		};

		@Override
		public int getCount() {
			return mImages.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = DealPage.this;
			ImageView imageView = new ImageView(context);
			int padding = context.getResources().getDimensionPixelSize(
					R.dimen.padding_medium);
			imageView.setPadding(padding, padding, padding, padding);
			imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			imageView.setImageResource(mImages[position]);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		for(int i=0;i<th.getTabWidget().getChildCount();i++){
			th.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_unselected_waverraccent);
			th.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_unselected_pressed_waverraccent);
		}
		th.getTabWidget().getChildAt(th.getCurrentTab()).setBackgroundResource(R.drawable.tab_indicator_ab_waverraccent);
		th.getTabWidget().getChildAt(th.getCurrentTab()).setBackgroundResource(R.drawable.tab_selected_waverraccent);
		th.getTabWidget().getChildAt(th.getCurrentTab()).setBackgroundResource(R.drawable.tab_selected_pressed_waverraccent);

	}

	@Override
	public void onMapReady(GoogleMap map) {
		// TODO Auto-generated method stub

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude) , 15.0f) );
		map.addMarker(new MarkerOptions()
		.position(new LatLng(latitude, longitude))
		.title("Location"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.get_directions:
			Uri gmmIntentUri = Uri.parse("google.navigation:q="+Double.toString(latitude)+","+Double.toString(longitude));
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
			mapIntent.setPackage("com.google.android.apps.maps");
			startActivity(mapIntent);
			break;

		case R.id.button_call:
			Intent dialIntent = new Intent(Intent.ACTION_DIAL);
			dialIntent.setData(Uri.parse("tel:" + restaurantPhoneNumber));
			startActivity(dialIntent);
			break;

		case R.id.favourite_status:
			global.setFavourite(restaurantName, !global.isFavourited(restaurantName));
			if(global.isFavourited(restaurantName))
				favouriteStatus.setImageResource(R.drawable.favorite_full);
			else
				favouriteStatus.setImageResource(R.drawable.favorite_empty);
			break;

		case R.id.activatedeal:
			// TODO Auto-generated method stub
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			intent.putExtra("PROMPT_MESSAGE", "");
			startActivityForResult(intent, 0);
			break;
		case R.id.share:
			final String RestaurantName = deal.getRestaurantName();
			Intent i=new Intent(android.content.Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Waverr");
			i.putExtra(android.content.Intent.EXTRA_TEXT, dtext+" at "+RestaurantName+".\nFind such amazing deals nearby, at Waverr - India's First Live Deal Engine. Visit us at www.waverr.in");
			startActivity(Intent.createChooser(i,"Share via"));
			break;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me){
		// Call onTouchEvent of SimpleGestureFilter class
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		int currentTab = th.getCurrentTab();
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			th.setCurrentTab(currentTab-1);
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			th.setCurrentTab(currentTab+1);
			break;
		}
		//Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDoubleTap() {
		//Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {

				String contents = intent.getStringExtra("SCAN_RESULT");
				if(contents.compareTo("Tritoria")==0)
				{
					Intent i=new Intent("com.project.waverr.SUCCESS");
					i.putExtra("Working",true);
					startActivity(i);
				}
				else
				{
					Intent i=new Intent("com.project.waverr.SUCCESS");
					i.putExtra("Working",false);
					startActivity(i);
				}
				// String format = intent.getStringExtra("SCAN_RESULT_FORMAT");	   	     	
				// Handle successful scan
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				Toast.makeText(getApplicationContext(),"not working", Toast.LENGTH_SHORT).show();

			}
		}
	}

	private void setLayout(Deal deal) {
		
		TextView theThing = (TextView) findViewById(R.id.theDeal);
		String dealThing = "Flat "+deal.getPercentageDiscount()+"% off on all food items!";
		theThing.setText(dealThing);

		TextView details = (TextView) findViewById(R.id.conditions);
		details.setText("Minimum amount is Rs "+deal.getMinimumAmount()+"\n"+deal.getDetails());

		TextView duration = (TextView) findViewById(R.id.timeLimit);
		duration.setText("The deal is valid from "+start.getDateTime()+" to "+end.getDateTime());
	}
	
	private void startTimer() {
		final long startMillis = start.getTimeInMillis();
		final long endMillis = end.getTimeInMillis();
		
		new JSONObtainer() {
			DateTime current = new DateTime();
			
			@Override
			protected void onProgressUpdate(Void... voids) {
				timerText.setText("Calculating time left...");
			}
			
			@Override
			protected void onPostExecute(JSONArray array) {
				try {
					JSONObject object = array.getJSONObject(0);
					current.setDate(object.getString("date"));
					current.setTime(object.getString("time"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long currentMillis = current.getTimeInMillis();
				
				final long timeUntilStart, timeUntilEnd;
				
				if(startMillis > currentMillis)
					timeUntilStart = startMillis - currentMillis;
				else
					timeUntilStart = 0;
				if(endMillis > currentMillis)
					timeUntilEnd = endMillis - currentMillis;
				else
					timeUntilEnd = 0;
				
				new CountDownTimer(timeUntilStart, 1000) {
					final DateTime actual = new DateTime();
					
					@Override
					public void onTick(long millisUntilFinished) {
						// TODO Auto-generated method stub
						actual.setDateTimeByMillis(millisUntilFinished);
						String text = "Deal starts in\n"
								+ actual.days + " d, "
								+ actual.hours + " h, "
								+ actual.minutes + " m, "
								+ actual.seconds + "s";
						timerText.setText(text);
					}
					
					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						new CountDownTimer(timeUntilEnd, 1000) {
							
							@Override
							public void onTick(long millisUntilFinished) {
								// TODO Auto-generated method stub
								actual.setDateTimeByMillis(millisUntilFinished);
								String text = "Deal ends in\n"
										+ actual.days + " d, "
										+ actual.hours + " h, "
										+ actual.minutes + " m, "
										+ actual.seconds + "s";
								timerText.setText(text);
							}
							
							@Override
							public void onFinish() {
								// TODO Auto-generated method stub
								String text = "Deal has expired!";
								timerText.setText(text);
							}
						}.start();
					}
				}.start();
			}
		}.execute(new String[] {"http://waverr.in/getcurrenttime.php"});
	}
}

package com.project.waverr;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.squareup.picasso.Picasso;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
@SuppressWarnings("deprecation")
public class NavigationDrawerFragment extends Fragment {

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callback instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	// private LinearLayout mDrawerLinearLayout;
	private View mFragmentContainerView;

	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;
	private boolean mUserLearnedDrawer;
	private GoogleApiClient mGoogleApiClient;
	GlobalClass global;
	ArrayAdapter<String> adapter;
	String[] things;

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		global = (GlobalClass) getActivity().getApplication();
		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			//mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;
		}

		// Select either the default item (0) or the last selected item.
		selectItem(0);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mDrawerListView = (ListView) inflater.inflate(
				R.layout.fragment_navigation_drawer, container, false);
		/*
		 * TextView
		 * tv=(TextView)mDrawerListView.findViewById(android.R.id.text1);
		 * tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menu_icon, 0,
		 * 0, 0);
		 */
		mDrawerListView
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectItem(position);
			}
		});

		GlobalClass global = (GlobalClass) getActivity().getApplication();

		// String url = "http://waverr.in/getusernames.php";
		things = new String[] {
				global.getPersonName(),
				global.getPersonEmail(),
				getString(R.string.title_section4),
				getString(R.string.title_section5),
				getString(R.string.title_section6), 
				getString(R.string.title_section7),
				global.getlastitem() };

		mGoogleApiClient=global.getClient();

		adapter = new ArrayAdapter<String>(
				getActionBar().getThemedContext(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, things);

		ImageView imageHeaderView = new ImageView(getActivity());
		/*
		 * imageHeaderView.setImageBitmap(decodeSampledBitmapFromResource(
		 * getResources(), R.drawable.chinese1, 200, 200));
		 * //imageHeaderView.setImageBitmap();
		 */
		String imageURL = null;
		if (global.getPersonPhoto() != null)
			imageURL = global.getPersonPhoto().getUrl();
		Picasso.with(getActivity()).load(imageURL + "0")
		.error(R.drawable.com_facebook_profile_default_icon)
		.placeholder(R.drawable.com_facebook_profile_default_icon)
		.resize(500, 500)
		.centerCrop().into(imageHeaderView);
		mDrawerListView.addHeaderView(imageHeaderView);
		mDrawerListView.setHeaderDividersEnabled(false);

		mDrawerListView.setAdapter(adapter);
		// mDrawerListView.addHeaderView(getActivity().getBaseContext().findViewById(R.drawable.cuisine2),
		// null, false);
		// mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

		/*
		 * new JSONObtainer() { protected void onPostExecute(JSONArray array) {
		 * Toast.makeText(getActivity(), "Got stuff",
		 * Toast.LENGTH_SHORT).show(); try { things[0] =
		 * array.getJSONObject(0).getString("Name"); things[1] =
		 * array.getJSONObject(0).getString("Email");
		 * adapter.notifyDataSetChanged(); } catch (JSONException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } }.execute(url);
		 */

		return mDrawerListView;
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null
				&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
				R.string.navigation_drawer_open, /*
				 * "open drawer" description for
				 * accessibility
				 */
				R.string.navigation_drawer_close /*
				 * "close drawer" description for
				 * accessibility
				 */
				) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!isAdded()) {
					return;
				}

				getActivity().supportInvalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
				global.setDrawerOpen(false);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!isAdded()) {
					return;
				}
				global.setDrawerOpen(true);
				things[0] = global.getPersonName();
				things[1] = global.getPersonEmail();
				things[6] = global.getlastitem();
				adapter.notifyDataSetChanged();

				if (!mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					mUserLearnedDrawer = true;
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
					.apply();
				}

				getActivity().supportInvalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void selectItem(int position) {
		//mCurrentSelectedPosition = position;

		if (mDrawerListView != null) {
			mDrawerListView.setItemChecked(position, false);
			mDrawerLayout.closeDrawer(mFragmentContainerView);
			if (mCallbacks != null) {
				mCallbacks.onNavigationDrawerItemSelected(position);
			}

			if (position == 3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("About Waverr");
				builder.setMessage(R.string.about_waverr);
				builder.setPositiveButton(R.string.button_ok, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			else if(position==4){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Contact Us");
				builder.setMessage(R.string.contact_us);
				builder.setPositiveButton("Send email", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_SENDTO,
								Uri.fromParts("mailto", "contact@waverr.in", null));
						intent.putExtra(Intent.EXTRA_SUBJECT, "Support - Android");
						startActivity(Intent.createChooser(intent, "Send email..."));
					}
				});
				builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}

			if(position == 5 ) {
				final ArrayList<String> restaurants = new ArrayList<>();

				final ProgressDialog dialog = new ProgressDialog(getActivity());
				dialog.setIndeterminate(true);
				dialog.setCancelable(true);
				dialog.setMessage("Please wait...");
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.show();

				new JSONObtainer() {
					String display = "";
					@Override
					protected void onPostExecute(JSONArray array) {
						if(array==null) 
							display = "Please check your network connection and try again";
						else {
							int i=0;
							for(i=0; i<array.length(); i++) {
								JSONObject object = null;
								try {
									object = array.getJSONObject(i);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								try {
									String temp  = object.getString("Restaurant Name");
									restaurants.add(temp);
									display = display + "\n" + temp;
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						dialog.dismiss();
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle("Our Partners");
						builder.setPositiveButton(R.string.button_ok, new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						builder.setMessage(display);
						builder.create().show();
					}

				}.execute(new String[] {
						"http://waverr.in/restaurantpartners.php"
				});
			}

			else if(position == 6) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Like our app? Please help us improve by rating us on the Play Store. Thank you!");
				builder.setPositiveButton("Rate us", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						try {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.project.waverr")));
						}
						catch (ActivityNotFoundException e) {
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.project.waverr"));
							startActivity(intent);
						}
					}
				});
				builder.setNegativeButton("No, thanks", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				builder.setTitle("Rate Waverr");
				builder.create().show();
			}
			else if (position == 7) {
				if (global.getloginstatus().equals("none")) {
					//getActivity().finish();
					Intent intent = new Intent("com.project.waverr.LOGINPAGE");
					startActivity(intent);
				}
				else if (global.getloginstatus().equals("google")) {
					logoutGoogle();
					getActivity().finish();
					Intent intent = new Intent("com.project.waverr.LOGINPAGE");
					startActivity(intent);
				}
			}
		}
	}

	public void logoutGoogle() {
		mGoogleApiClient = global.getClient();
		if(mGoogleApiClient.isConnected()){
			Toast.makeText(getActivity(), "Logging out...", Toast.LENGTH_SHORT).show();
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
			//mGoogleApiClient.disconnect();
			global.clearUser();
			getActivity().finish();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		if (mDrawerLayout != null && isDrawerOpen()) {
			inflater.inflate(R.menu.global, menu);
			showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle("Menu");
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}
}

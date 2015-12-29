package de.grundid.drinker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;
import de.grundid.drinker.location.LocationAdapter;
import de.grundid.drinker.menu.DrinksMenuActivity;
import de.grundid.drinker.menu.Menu;
import de.grundid.drinker.storage.DaoManager;
import de.grundid.drinker.storage.Location;
import de.grundid.drinker.utils.AnalyticsUtils;

import java.util.*;

public class MainActivity extends AppCompatActivity implements ItemClickListener<String> {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final Set<Integer> SUPPORTED_TYPES = new HashSet<>(Arrays.asList(
			Place.TYPE_BAKERY, Place.TYPE_BAR, Place.TYPE_CAFE, Place.TYPE_BOWLING_ALLEY, Place.TYPE_CASINO,
			Place.TYPE_GAS_STATION, Place.TYPE_HOSPITAL, Place.TYPE_MEAL_TAKEAWAY, Place.TYPE_MOVIE_THEATER,
			Place.TYPE_NIGHT_CLUB, Place.TYPE_RESTAURANT, Place.TYPE_SHOPPING_MALL, Place.TYPE_TRAIN_STATION,
			Place.TYPE_UNIVERSITY));
	private static final int PLACE_PICKER_REQUEST = 1;
	private RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Drinker - die offene Getränkekarte");
		final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		initFab();
	}

	@Override protected void onStart() {
		super.onStart();
		checkPlayServices();
		List<?> locations = DaoManager.with(this).selectAllLocations();
		recyclerView.setAdapter(new LocationAdapter((List<Object>)locations, this));
		AnalyticsUtils.with(this).sendScreen("/start");
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST) {
			if (resultCode == RESULT_OK) {
				Place place = PlacePicker.getPlace(data, this);
				if (!Collections.disjoint(SUPPORTED_TYPES, place.getPlaceTypes())) {
					savePlace(place);
					Ion.with(this).load(Config.BASE_URL + "/menu/" + place.getId())
							.as(new TypeToken<Menu>() {
							})
							.withResponse()
							.setCallback(new PlaceResponseHandler(this, place, this));
				}
				else {
					String toastMsg = String.format("Dieser Ort wird nicht unterstützt.", place.getName());
					Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	private void savePlace(Place place) {
		DaoManager daoManager = DaoManager.with(this);
		Location location = daoManager.selectLocation(place.getId());
		if (location == null) {
			location = new Location();
			location.setPlaceId(place.getId());
			location.setName(place.getName().toString());
			location.setAddress(place.getAddress().toString());
			location.setLatitude(place.getLatLng().latitude);
			location.setLongitude(place.getLatLng().longitude);
			location.setVisits(0);
			daoManager.insertLocation(location);
		}
	}

	private void initFab() {
		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) {
				PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
				try {
					startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
				}
				catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			}
			else {
				Log.i("MBW", "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

	@Override public void onItemClick(String placeId) {
		Intent intent = new Intent(this, DrinksMenuActivity.class);
		intent.putExtra("PLACE_ID", placeId);
		startActivity(intent);
	}
}

package de.grundid.drinker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import de.grundid.drinker.addplace.AddPlaceActivity;
import de.grundid.drinker.addplace.NewPlace;
import de.grundid.drinker.location.LocationAdapter;
import de.grundid.drinker.menu.DrinksMenuActivity;
import de.grundid.drinker.menu.Menu;
import de.grundid.drinker.storage.DaoManager;
import de.grundid.drinker.storage.Location;
import de.grundid.drinker.utils.AnalyticsUtils;
import de.grundid.drinker.utils.ListElement;
import de.grundid.drinker.utils.PlaceWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
		implements ItemClickListener<String>, DeleteRequestListener<Location> {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	private static final int PLACE_PICKER_REQUEST = 1;
	private static final int NEW_LOCATION_REQUEST = 2;
	private RecyclerView recyclerView;
	private ProgressDialog progressDialog;
	private LocationAdapter locationAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Drinker - offene Getränkekarte");
		final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		initFab();
	}

	@Override protected void onStart() {
		super.onStart();
		checkPlayServices();
		loadData();
		AnalyticsUtils.with(this).sendScreen("/start");
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	private void loadData() {
		List<Location> locations = DaoManager.with(this).selectAllLocations();
		List<ListElement> locationElements = new ArrayList<>(locations.size());
		for (Location location : locations) {
			locationElements.add(new ListElement(LocationAdapter.TYPE_LOCATION, location));
		}
		if (locationAdapter == null) {
			locationAdapter = new LocationAdapter(locationElements, this, this);
			recyclerView.setAdapter(locationAdapter);
		}
		else {
			locationAdapter.updateLocations(locationElements);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST) {
			if (resultCode == RESULT_OK) {
				final Place place = PlacePicker.getPlace(data, this);
				if (!Collections.disjoint(Config.SUPPORTED_TYPES, place.getPlaceTypes())) {
					handleSelectedPlace(new PlaceWrapper(place));
				}
				else {
					if (!Collections.disjoint(Config.NEW_SUPPORTED_TYPES, place.getPlaceTypes())) {
						createNewPlace(place);
					}
					else {
						Log.i("DRINKER", "Place type: " + place.getPlaceTypes());
						String toastMsg = "Dieser Ort wird nicht unterstützt.";
						Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
					}
				}
			}
		}
		else if (requestCode == NEW_LOCATION_REQUEST) {
			if (resultCode == RESULT_OK) {
				PlaceWrapper place = data.getParcelableExtra("PLACE");
				handleSelectedPlace(place);
			}
		}
	}

	private void createNewPlace(final Place place) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Neue Location?").setMessage(
				"An dieser Stelle ist keine Location vermerkt. "
						+ "Möchtest du eine neue Location anlegen?")
				.setPositiveButton(
						"Ja", new DialogInterface.OnClickListener() {

							@Override public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(MainActivity.this, AddPlaceActivity.class);
								intent.putExtra(AddPlaceActivity.EXTRA_NEW_PLACE,
										new NewPlace(place.getAddress(), place.getLatLng().latitude,
												place.getLatLng().longitude));
								intent.setFlags(
										Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
								startActivityForResult(intent, NEW_LOCATION_REQUEST);
							}
						}).setNegativeButton("Nein", null).create().show();
	}

	private void handleSelectedPlace(PlaceWrapper place) {
		DaoManager.with(this).savePlace(place);
		Ion.with(this).load(Config.BASE_URL + "/menu/" + place.getId())
				.as(new TypeToken<Menu>() {

				})
				.withResponse()
				.setCallback(new PlaceResponseHandler(this, place, this));
	}

	private void initFab() {
		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) {
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setCancelable(false);
				progressDialog.setIndeterminate(true);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progressDialog.show();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_info: {
				startActivity(new Intent(this, InfoActivity.class));
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override public void requestDelete(final Location location) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Location löschen").setMessage("Möchtest du diese Location löschen?")
				.setPositiveButton(
						"Ja", new DialogInterface.OnClickListener() {

							@Override public void onClick(DialogInterface dialog, int which) {
								DaoManager.with(MainActivity.this).deleteLocation(location.getPlaceId());
								loadData();
							}
						}).setNegativeButton("Nein", null).create().show();
	}
}

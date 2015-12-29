package de.grundid.drinker.addplace;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import de.grundid.drinker.Config;
import de.grundid.drinker.R;
import de.grundid.drinker.Utils;
import de.grundid.drinker.utils.LabelWithId;
import de.grundid.drinker.utils.PlaceWrapper;

import java.util.*;

public class AddPlaceActivity extends AppCompatActivity {

	public static final String EXTRA_NEW_PLACE = "NEW_PLACE";
	private EditText placeName;
	private EditText placeStreet;
	private EditText placeZip;
	private EditText placeCity;
	private EditText placePhone;
	private EditText placeUrl;
	private NewPlace newPlace;
	private Set<Integer> selectedPlaceTypes = new HashSet<>();
	private GoogleApiClient googleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_place);
		setTitle("Neue Location");
		newPlace = getIntent().getParcelableExtra(EXTRA_NEW_PLACE);
		placeName = (EditText)findViewById(R.id.placeName);
		placeStreet = (EditText)findViewById(R.id.placeStreet);
		placeZip = (EditText)findViewById(R.id.placeZip);
		placeCity = (EditText)findViewById(R.id.placeCity);
		placePhone = (EditText)findViewById(R.id.placePhone);
		placeUrl = (EditText)findViewById(R.id.placeUrl);
		findViewById(R.id.addressBox).setVisibility(newPlace.hasAddress() ? View.GONE : View.VISIBLE);
		findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) {
				checkPlace();
			}
		});
		googleApiClient = new GoogleApiClient
				.Builder(this)
				.addApi(Places.GEO_DATA_API)
				.addApi(Places.PLACE_DETECTION_API)
				.build();
	}

	@Override
	protected void onStart() {
		super.onStart();
		googleApiClient.connect();
	}

	@Override
	protected void onStop() {
		googleApiClient.disconnect();
		super.onStop();
	}

	private boolean isValid() {
		if (!Utils.hasText(placeName.getText())) {
			return false;
		}
		if (!newPlace.hasAddress()) {
			return Utils.hasText(placeStreet.getText()) && Utils.hasText(placeZip.getText()) && Utils
					.hasText(placeCity.getText());
		}
		return true;
	}

	public Dialog createDialog() {
		final Set<Integer> selectedIds = new HashSet<>();
		final LabelWithId[] labelsArray = createPlaceTypeLabels();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Set the dialog title
		builder.setTitle("Wähle die Kategorie aus")
				.setMultiChoiceItems(labelsArray, null,
						new DialogInterface.OnMultiChoiceClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which,
									boolean isChecked) {
								if (isChecked) {
									selectedIds.add(which);
								}
								else if (selectedIds.contains(which)) {
									selectedIds.remove(Integer.valueOf(which));
								}
							}
						})
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {
						selectedPlaceTypes.clear();
						for (Integer selectedId : selectedIds) {
							selectedPlaceTypes.add(labelsArray[selectedId].getId());
						}
						if (selectedPlaceTypes.isEmpty()) {
							AlertDialog.Builder builder = new AlertDialog.Builder(AddPlaceActivity.this);
							builder.setTitle("Daten unvollständig").setMessage("Bitte wähle min. eine Kategorie aus.")
									.setPositiveButton("OK", null);
							builder.create().show();
						}
						else {
							savePlace();
						}
					}
				})
				.setNegativeButton("Abbrechen", null);
		return builder.create();
	}

	private void savePlace() {
		final AddPlaceRequest placeRequest =
				new AddPlaceRequest(
						placeName.getText().toString(),
						new LatLng(newPlace.getLatitude(), newPlace.getLongitude()),
						getAddress(),
						new ArrayList<>(selectedPlaceTypes),
						getPhone(),
						getUrl()
				);
		Log.i("DRINKER", "Place: " + placeRequest);
		Places.GeoDataApi.addPlace(googleApiClient, placeRequest)
				.setResultCallback(new ResultCallback<PlaceBuffer>() {

					@Override
					public void onResult(PlaceBuffer places) {
						Log.i("DRINKER", "Place add result: " + places.getStatus().toString());
						if (places.getStatus().isSuccess() && places.getCount() > 0) {
							Place place = places.get(0);
							handleNewPlace(place);
						}
						else {
							Log.i("DRINKER", "No new Place");
						}
						places.release();
					}
				});
	}

	private void handleNewPlace(Place place) {
		Intent intent = new Intent();
		intent.putExtra("PLACE", new PlaceWrapper(place));
		setResult(RESULT_OK, intent);
		finish();
		;
	}

	private LabelWithId[] createPlaceTypeLabels() {
		Set<LabelWithId> labels = new TreeSet<>();
		for (Map.Entry<Integer, Integer> entry : Config.TYPE_LABELS.entrySet()) {
			labels.add(new LabelWithId(getResources().getString(entry.getValue()), entry.getKey()));
		}
		return labels.toArray(new LabelWithId[labels.size()]);
	}

	private void checkPlace() {
		if (isValid()) {
			createDialog().show();
		}
		else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Daten unvollständig").setMessage("Bitte fülle alle Felder aus.")
					.setPositiveButton("OK", null);
			builder.create().show();
		}
	}

	public String getAddress() {
		if (newPlace.hasAddress()) {
			return newPlace.getAddress();
		}
		else
			return placeStreet.getText() + ", " + placeZip.getText() + " " + placeCity.getText();
	}

	public String getPhone() {
		if (Utils.hasText(placePhone.getText()))
			return placePhone.getText().toString();
		else
			return null;
	}

	public Uri getUrl() {
		if (Utils.hasText(placeUrl.getText())) {
			String url = placeUrl.getText().toString();
			if (!url.startsWith("http")) {
				url = "http://" + url;
			}
			try {
				return Uri.parse(url);
			}
			catch (Exception e) {
				return null;
			}
		}
		else
			return null;
	}
}

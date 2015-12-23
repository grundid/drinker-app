package de.grundid.drinker;

import android.app.Activity;
import android.content.Intent;
import com.google.android.gms.location.places.Place;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import de.grundid.drinker.menu.DrinksMenuActivity;
import de.grundid.drinker.menu.Menu;

public class PlaceResponseHandler implements FutureCallback<Response<Menu>> {

	private Activity activity;
	private Place place;
	private ItemClickListener<String> placeIdClickListener;

	public PlaceResponseHandler(Activity activity, Place place, ItemClickListener<String> placeIdClickListener) {
		this.activity = activity;
		this.place = place;
		this.placeIdClickListener = placeIdClickListener;
	}

	@Override public void onCompleted(Exception e, Response<Menu> result) {
		if (result.getHeaders().code() == 404) {
			createNewPlace(place);
		}
		else {
			placeIdClickListener.onItemClick(place.getId());
		}
	}

	private void createNewPlace(final Place place) {
		LocationModel locationModel = new LocationModel();
		locationModel.setPlaceId(place.getId());
		locationModel.setName(place.getName().toString());
		locationModel.setAddress(place.getAddress().toString());
		locationModel.setLatitude(place.getLatLng().latitude);
		locationModel.setLongitude(place.getLatLng().longitude);
		Ion.with(activity).load("POST", Config.BASE_URL + "/location").setJsonPojoBody(locationModel).asString()
				.withResponse().setCallback(
				new FutureCallback<Response<String>>() {

					@Override public void onCompleted(Exception e, Response<String> result) {
						if (e == null) {
							placeIdClickListener.onItemClick(place.getId());
						}
					}
				});
	}


}

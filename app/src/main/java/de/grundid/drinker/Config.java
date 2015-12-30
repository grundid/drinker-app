package de.grundid.drinker;

import com.google.android.gms.location.places.Place;

import java.util.*;

public class Config {

	public static final String BASE_URL = "https://api.grundid.de/drinksmenu";
	public static final Set<Integer> SUPPORTED_TYPES = new HashSet<>();
	public static final Set<Integer> NEW_SUPPORTED_TYPES = new HashSet<>(Arrays.asList(
			Place.TYPE_STREET_ADDRESS, Place.TYPE_SYNTHETIC_GEOCODE));
	public static final Map<Integer, Integer> TYPE_LABELS = new HashMap<>();

	static {
		TYPE_LABELS.put(Place.TYPE_BAR, R.string.TYPE_BAR);
		TYPE_LABELS.put(Place.TYPE_CAFE, R.string.TYPE_CAFE);
		TYPE_LABELS.put(Place.TYPE_MOVIE_THEATER, R.string.TYPE_MOVIE_THEATER);
		TYPE_LABELS.put(Place.TYPE_NIGHT_CLUB, R.string.TYPE_NIGHT_CLUB);
		TYPE_LABELS.put(Place.TYPE_RESTAURANT, R.string.TYPE_RESTAURANT);
		SUPPORTED_TYPES.addAll(TYPE_LABELS.keySet());
		SUPPORTED_TYPES.add(Place.TYPE_BAKERY);
		SUPPORTED_TYPES.add(Place.TYPE_BOWLING_ALLEY);
		SUPPORTED_TYPES.add(Place.TYPE_CASINO);
		SUPPORTED_TYPES.add(Place.TYPE_GAS_STATION);
		SUPPORTED_TYPES.add(Place.TYPE_HOME_GOODS_STORE);
		SUPPORTED_TYPES.add(Place.TYPE_MEAL_TAKEAWAY);
		SUPPORTED_TYPES.add(Place.TYPE_SHOPPING_MALL);
		SUPPORTED_TYPES.add(Place.TYPE_TRAIN_STATION);
		SUPPORTED_TYPES.add(Place.TYPE_UNIVERSITY);
		SUPPORTED_TYPES.add(Place.TYPE_ESTABLISHMENT);
	}
}

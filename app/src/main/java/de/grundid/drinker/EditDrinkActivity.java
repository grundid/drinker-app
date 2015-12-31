package de.grundid.drinker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import de.grundid.android.utils.AndroidHelper;
import de.grundid.drinker.menu.DrinkModel;
import de.grundid.drinker.menu.MenuDrink;
import de.grundid.drinker.utils.AnalyticsUtils;
import de.grundid.drinker.utils.DrinkModelHelper;
import de.grundid.drinker.utils.PreferencesUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class EditDrinkActivity extends AppCompatActivity {

	public static final String EXTRA_LOCATION_ID = "EXTRA_LOCATION_ID";
	public static final String EXTRA_DRINK = "EXTRA_DRINK";
	private AutoCompleteTextView drinkName;
	private AutoCompleteTextView drinkBrand;
	private EditText drinkPrice;
	private AutoCompleteTextView drinkVolume;
	private EditText drinkDescription;
	private Spinner categorySpinner;
	private String locationId;
	private boolean saveInProcess = false;
	private boolean saveMultiple = false;
	private MenuDrink menuDrink;

	public class NumericDigitsKeyListener extends DigitsKeyListener {

		@Override
		protected char[] getAcceptedChars() {
			return new char[] {
					'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ',',
					'.' };
		}

		public int getInputType() {
			return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_drink);
		setTitle("Getränk erfassen");
		locationId = getIntent().getStringExtra(EXTRA_LOCATION_ID);
		categorySpinner = (Spinner)findViewById(R.id.category_spinner);
		categorySpinner.setAdapter(createCategoryAdapter());
		drinkName = (AutoCompleteTextView)findViewById(R.id.drinkName);
		drinkBrand = (AutoCompleteTextView)findViewById(R.id.drinkBrand);
		drinkPrice = (EditText)findViewById(R.id.drinkPrice);
		drinkPrice.setKeyListener(new NumericDigitsKeyListener());
		drinkVolume = (AutoCompleteTextView)findViewById(R.id.drinkVolume);
		drinkVolume.setKeyListener(new NumericDigitsKeyListener());
		drinkDescription = (EditText)findViewById(R.id.drinkDescription);
		AndroidHelper.setupFinishListener(this, R.id.finishButton);
		findViewById(R.id.finishButton).setVisibility(View.INVISIBLE);
		findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) {
				saveDrink();
			}
		});
		initSuggestions();
		initFieldsFromMenuDrink();
	}

	@Override protected void onStart() {
		super.onStart();
		AnalyticsUtils.with(this).sendScreen("/editDrink");
	}

	private void initFieldsFromMenuDrink() {
		menuDrink = getIntent().getParcelableExtra(EXTRA_DRINK);
		if (menuDrink != null) {
			NumberFormat priceFormat = new DecimalFormat("0.0#");
			drinkName.setText(menuDrink.getName());
			drinkBrand.setText(menuDrink.getBrand());
			drinkDescription.setText(menuDrink.getDescription());
			drinkPrice.setText(priceFormat.format((double)menuDrink.getPrice() / 100));
			if (menuDrink.getVolume() != null)
				drinkVolume.setText("" + menuDrink.getVolume());
			int counter = 0;
			for (Category cat : Category.values()) {
				if (cat.name().equals(menuDrink.getCategory())) {
					categorySpinner.setSelection(counter);
				}
				counter++;
			}
			categorySpinner.requestFocus();
		}
	}

	private StringListAdapter createCategoryAdapter() {
		List<String> categories = new ArrayList<>();
		for (Category cat : Category.values()) {
			categories.add(cat.getLabel());
		}
		return new StringListAdapter(categories, LayoutInflater.from(this), android.R.layout.simple_spinner_item,
				android.R.layout.simple_spinner_dropdown_item);
	}

	private void initSuggestions() {
		drinkVolume.setAdapter(ArrayAdapter.createFromResource(this,
				R.array.volumes, android.R.layout.simple_list_item_1));
		drinkName.setAdapter(ArrayAdapter.createFromResource(this,
				R.array.drinks, android.R.layout.simple_list_item_1));
		drinkBrand.setAdapter(ArrayAdapter.createFromResource(this,
				R.array.brands, android.R.layout.simple_list_item_1));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_save: {
				saveDrink();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private synchronized void saveDrink() {
		if (!saveInProcess) {
			saveInProcess = true;
			DrinkModel drinkModel = new DrinkModel();
			drinkModel.setLocationId(locationId);
			drinkModel.setName(drinkName.getText().toString());
			drinkModel.setBrand(drinkBrand.getText().toString());
			drinkModel.setVolume(DrinkModelHelper.parseDrinkVolume(drinkVolume.getText()));
			drinkModel.setCategory(parseDrinkCategory());
			drinkModel.setPrice(DrinkModelHelper.parseDrinkPrice(drinkPrice.getText()));
			drinkModel.setDescription(drinkDescription.getText().toString());
			if (DrinkModelHelper.isDrinkModelValid(drinkModel)) {
				String method = menuDrink != null ? "PUT" : "POST";
				String path = menuDrink != null ? "/drink/" + menuDrink.getDrinkId() : "/drink";
				Ion.with(this).load(method, Config.BASE_URL + path).
						setHeader("X-User-UUID", PreferencesUtils.getUuid(this)).
						setJsonPojoBody(drinkModel).asString()
						.withResponse().setCallback(
						new FutureCallback<Response<String>>() {

							@Override public void onCompleted(Exception e, Response<String> result) {
								saveInProcess = false;
								if (e == null && result.getHeaders().code() == 200) {
									handleSuccessfulSave();
								}
								else {
									Toast.makeText(EditDrinkActivity.this,
											"Fehler beim Speichern: " + result.getHeaders().code(), Toast.LENGTH_SHORT)
											.show();
								}
							}
						});
			}
			else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Fehlende Angaben");
				builder.setMessage("Bitte min. Name, Kategorie und Preis angeben.");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override public void onClick(DialogInterface dialog, int which) {
						saveInProcess = false;
					}
				});
				builder.create().show();
			}
		}
	}

	private void handleSuccessfulSave() {
		if (saveMultiple) {
			showSavedAndResetFrom();
		}
		else {
			if (menuDrink == null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Mehrere Getränke?").setMessage("Möchtest du weitere Getränke erfassen?")
						.setPositiveButton(
								"Ja", new DialogInterface.OnClickListener() {

									@Override public void onClick(DialogInterface dialog, int which) {
										saveMultiple = true;
										findViewById(R.id.finishButton).setVisibility(View.VISIBLE);
										showSavedAndResetFrom();
									}
								}).setNegativeButton("Nein", new DialogInterface.OnClickListener() {

					@Override public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).create().show();
			}
			else {
				finish();
			}
		}
		AnalyticsUtils.with(this).sendEvent("drink", "saveDrink", null);
	}

	private void showSavedAndResetFrom() {
		Toast.makeText(this,
				"Gespeichert", Toast.LENGTH_SHORT)
				.show();
		resetForm();
	}

	private void resetForm() {
		drinkName.setText("");
		drinkBrand.setText("");
		drinkPrice.setText(null);
		drinkVolume.setText(null);
		drinkDescription.setText(null);
		drinkName.requestFocus();
	}

	private Category parseDrinkCategory() {
		Object categoryName = categorySpinner.getSelectedItem();
		for (Category category : Category.values()) {
			if (categoryName.equals(category.getLabel())) {
				return category;
			}
		}
		return null;
	}

	@Override public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_drink_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
}

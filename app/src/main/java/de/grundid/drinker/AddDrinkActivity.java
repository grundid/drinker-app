package de.grundid.drinker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import de.grundid.android.utils.AndroidHelper;
import de.grundid.drinker.menu.DrinkModel;

import java.util.*;

public class AddDrinkActivity extends AppCompatActivity {

	private AutoCompleteTextView drinkName;
	private AutoCompleteTextView drinkBrand;
	private EditText drinkPrice;
	private AutoCompleteTextView drinkVolume;
	private EditText drinkDescription;
	private Spinner categorySpinner;
	private String locationId;
	private boolean saveInProcess = false;
	private boolean saveMultiple = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_drink);
		setTitle("Getränk erfassen");
		locationId = getIntent().getStringExtra("LOCATION_ID");
		categorySpinner = (Spinner)findViewById(R.id.category_spinner);
		categorySpinner.setAdapter(createCategoryAdapter());
		drinkName = (AutoCompleteTextView)findViewById(R.id.drinkName);
		drinkBrand = (AutoCompleteTextView)findViewById(R.id.drinkBrand);
		drinkPrice = (EditText)findViewById(R.id.drinkPrice);
		drinkVolume = (AutoCompleteTextView)findViewById(R.id.drinkVolume);
		drinkDescription = (EditText)findViewById(R.id.drinkDescription);
		AndroidHelper.setupFinishListener(this, R.id.finishButton);
		findViewById(R.id.finishButton).setVisibility(View.INVISIBLE);
		findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {

			@Override public void onClick(View v) {
				saveDrink();
			}
		});
		initSuggestions();
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
			drinkModel.setVolume(parseDrinkVolume());
			drinkModel.setCategory(parseDrinkCategory());
			drinkModel.setPrice(parseDrinkPrice());
			drinkModel.setDescription(drinkDescription.getText().toString());
			Ion.with(this).load("POST", Config.BASE_URL + "/drink").setJsonPojoBody(drinkModel).asString()
					.withResponse().setCallback(
					new FutureCallback<Response<String>>() {

						@Override public void onCompleted(Exception e, Response<String> result) {
							saveInProcess = false;
							if (e == null && result.getHeaders().code() == 200) {
								handleSuccessfulSave();
							}
							else {
								Toast.makeText(AddDrinkActivity.this,
										"Fehler beim Speichern: " + result.getHeaders().code(), Toast.LENGTH_SHORT)
										.show();
							}
						}
					});
		}
	}

	private boolean isDrinkModelValid(DrinkModel model) {

		return false;
	}

	private void handleSuccessfulSave() {
		if (saveMultiple) {
			Toast.makeText(this,
					"Gespeichert", Toast.LENGTH_SHORT)
					.show();
			resetForm();
		}
		else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Mehrere Getränke?").setMessage("Möchtest du weitere Getränke erfassen?")
					.setPositiveButton(
							"Ja", new DialogInterface.OnClickListener() {

								@Override public void onClick(DialogInterface dialog, int which) {
									saveMultiple = true;
									findViewById(R.id.finishButton).setVisibility(View.VISIBLE);
									resetForm();
								}
							}).setNegativeButton("Nein", new DialogInterface.OnClickListener() {

				@Override public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).create().show();
		}
	}

	private void resetForm() {
		drinkName.setText("");
		drinkBrand.setText("");
		drinkPrice.setText(null);
		drinkVolume.setText(null);
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

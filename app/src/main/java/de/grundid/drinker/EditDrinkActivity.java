package de.grundid.drinker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;

import de.grundid.android.utils.AndroidHelper;
import de.grundid.drinker.menu.DrinkModel;
import de.grundid.drinker.menu.DrinkViewHolder;
import de.grundid.drinker.menu.Menu;
import de.grundid.drinker.menu.MenuDrink;
import de.grundid.drinker.storage.DaoManager;
import de.grundid.drinker.utils.AnalyticsUtils;
import de.grundid.drinker.utils.CategoryHelper;
import de.grundid.drinker.utils.DatedResponse;
import de.grundid.drinker.utils.DrinkModelHelper;
import de.grundid.drinker.utils.PreferencesUtils;
import de.grundid.drinker.utils.Suggest;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private boolean resetName = false;
    private boolean resetBrand = false;
    //Standardmäßig werden Preis, Volumen und Beschreibung zurückgesetzt
    private boolean resetPrice = true;
    private boolean resetVolume = true;
    private boolean resetDescription = true;
    private MenuDrink menuDrink;
    private LinearLayout previousDrink;
    private Suggest suggest;
    private CategoryHelper categories;

    public class NumericDigitsKeyListener extends DigitsKeyListener {

        @Override
        protected char[] getAcceptedChars() {
            return new char[]{
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ',',
                    '.'};
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
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        drinkName = (AutoCompleteTextView) findViewById(R.id.drinkName);
        drinkBrand = (AutoCompleteTextView) findViewById(R.id.drinkBrand);
        drinkPrice = (EditText) findViewById(R.id.drinkPrice);
        drinkPrice.setKeyListener(new NumericDigitsKeyListener());
        drinkVolume = (AutoCompleteTextView) findViewById(R.id.drinkVolume);
        drinkVolume.setKeyListener(new NumericDigitsKeyListener());
        drinkDescription = (EditText) findViewById(R.id.drinkDescription);
        AndroidHelper.setupFinishListener(this, R.id.finishButton);
        findViewById(R.id.finishButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveDrink();
            }
        });
        findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditDrinkActivity.this);
                builder.setTitle("Getränk löschen").setMessage("Möchtest du diesen Drink löschen?")
                        .setPositiveButton(
                                "Ja", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteDrink();
                                    }
                                }).setNegativeButton("Nein", null).create().show();
            }
        });
        initSuggestions();
        initCategoryAdapter();
        resetName = PreferencesUtils.getBoolPreference(this, PreferencesUtils.KEY_RESET_NAME, false);
        resetBrand = PreferencesUtils.getBoolPreference(this, PreferencesUtils.KEY_RESET_BRAND, false);
        resetPrice = PreferencesUtils.getBoolPreference(this, PreferencesUtils.KEY_RESET_PRICE, false);
        resetVolume = PreferencesUtils.getBoolPreference(this, PreferencesUtils.KEY_RESET_VOLUME, false);
        resetDescription = PreferencesUtils.getBoolPreference(this, PreferencesUtils.KEY_RESET_DESCRIPTION, false);
        previousDrink = (LinearLayout) findViewById(R.id.previous_drink);
        previousDrink.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnalyticsUtils.with(this).sendScreen("/editDrink");
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferencesUtils.setBoolPreference(this, PreferencesUtils.KEY_RESET_NAME, resetName);
        PreferencesUtils.setBoolPreference(this, PreferencesUtils.KEY_RESET_BRAND, resetBrand);
        PreferencesUtils.setBoolPreference(this, PreferencesUtils.KEY_RESET_PRICE, resetPrice);
        PreferencesUtils.setBoolPreference(this, PreferencesUtils.KEY_RESET_VOLUME, resetVolume);
        PreferencesUtils.setBoolPreference(this, PreferencesUtils.KEY_RESET_DESCRIPTION, resetDescription);
    }

    private void initFieldsFromMenuDrink() {
        if (menuDrink != null) {
            NumberFormat priceFormat = new DecimalFormat("0.0#");
            drinkName.setText(menuDrink.getName());
            drinkBrand.setText(menuDrink.getBrand());
            drinkDescription.setText(menuDrink.getDescription());
            drinkPrice.setText(priceFormat.format((double) menuDrink.getPrice() / 100));
            if (menuDrink.getVolume() != null)
                drinkVolume.setText("" + menuDrink.getVolume());
            categorySpinner.setSelection(categories.indexOfCategory(menuDrink.getCategory()));
        }
    }

    private void initCategoryAdapter() {
        Builders.Any.B load = Ion.with(this).load(Config.BASE_URL + "/suggest");
        load.asInputStream().withResponse()
                .setCallback(new FutureCallback<Response<InputStream>>() {

                    @Override
                    public void onCompleted(Exception e, Response<InputStream> response) {
                        if (e == null) {
                            try {
                                suggest = new ObjectMapper().readValue(response.getResult(), Suggest.class);
                                categories = new CategoryHelper(suggest.getCategories());
                                //TODO
                                categorySpinner.setAdapter(new StringListAdapter(categories.getCategories(), LayoutInflater.from(EditDrinkActivity.this), android.R.layout.simple_spinner_item,
                                        android.R.layout.simple_spinner_dropdown_item));
                                initMenuDrinkFromIntent();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                Toast.makeText(EditDrinkActivity.this,
                                        "Fehler bei initialisieren der Kategorien", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EditDrinkActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
            final DrinkModel drinkModel = new DrinkModel();
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

                            @Override
                            public void onCompleted(Exception e, Response<String> result) {
                                saveInProcess = false;
                                if (e == null && result.getHeaders().code() == 200) {
                                    handleSuccessfulSave();
                                    updateLastDrink(drinkModel);
                                } else {
                                    Toast.makeText(EditDrinkActivity.this,
                                            "Fehler beim Speichern: " + result.getHeaders().code(), Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Fehlende Angaben");
                builder.setMessage("Bitte min. Name, Kategorie und Preis angeben.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveInProcess = false;
                    }
                });
                builder.create().show();
            }
        }
    }

    private void updateLastDrink(DrinkModel drinkModel) {
        DrinkViewHolder viewHolder =  new DrinkViewHolder(previousDrink);
        viewHolder.update(drinkModel, Long.MAX_VALUE);
        viewHolder.hideMoreActions();
        previousDrink.setVisibility(View.VISIBLE);
    }

    private void handleSuccessfulSave() {
        if (saveMultiple) {
            showSavedAndResetFrom();
        } else {
            if (menuDrink == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Mehrere Getränke?").setMessage("Möchtest du weitere Getränke erfassen?")
                        .setPositiveButton(
                                "Ja", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        saveMultiple = true;
                                        findViewById(R.id.finishButton).setVisibility(View.VISIBLE);
                                        showSavedAndResetFrom();
                                    }
                                }).setNegativeButton("Nein", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
            } else {
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
        if (resetName) {
            drinkName.setText("");
        }
        if (resetBrand) {
            drinkBrand.setText("");
        }
        if(resetPrice){
            drinkPrice.setText("");
        }
        if(resetVolume) {
            drinkVolume.setText("");
        }
        if(resetDescription) {
            drinkDescription.setText("");
        }
        drinkName.requestFocus();
    }

    private String parseDrinkCategory() {
        Object categoryName = categorySpinner.getSelectedItem();
        return categories.getCategoryKey(categoryName.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_drink_menu, menu);
        MenuItem resetNameItem = menu.findItem(R.id.action_resetname);
        resetNameItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i("Drinker", item.getTitle().toString());
                resetName = !resetName;
                item.setChecked(resetName);
                return true;
            }
        });
        resetNameItem.setChecked(resetName);
        MenuItem resetBrandItem = menu.findItem(R.id.action_resetbrand);
        resetBrandItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i("Drinker", item.getTitle().toString());
                resetBrand = !resetBrand;
                item.setChecked(resetBrand);
                return true;
            }
        });
        resetBrandItem.setChecked(resetBrand);
        MenuItem resetPriceItem = menu.findItem(R.id.action_resetprice);
        resetPriceItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i("Drinker", item.getTitle().toString());
                resetPrice = !resetPrice;
                item.setChecked(resetPrice);
                return true;
            }
        });
        resetPriceItem.setChecked(resetPrice);
        MenuItem resetVolumeItem = menu.findItem(R.id.action_resetvolume);
        resetVolumeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i("Drinker", item.getTitle().toString());
                resetVolume = !resetVolume;
                item.setChecked(resetVolume);
                return true;
            }
        });
        resetVolumeItem.setChecked(resetVolume);
        MenuItem resetDescriptionItem = menu.findItem(R.id.action_resetdescription);
        resetDescriptionItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i("Drinker", item.getTitle().toString());
                resetDescription = !resetDescription;
                item.setChecked(resetDescription);
                return true;
            }
        });
        resetDescriptionItem.setChecked(resetDescription);
        return super.onCreateOptionsMenu(menu);
    }

    private void deleteDrink() {
        String method = "DELETE";
        String path = "/drink/" + menuDrink.getDrinkId();
        Ion.with(this).load(method, Config.BASE_URL + path).
                setHeader("X-User-UUID", PreferencesUtils.getUuid(this)).asString()
                .setCallback(
                        new FutureCallback<String>() {

                            @Override
                            public void onCompleted(Exception e, String result) {
                                if(e == null) {
                                    Toast.makeText(EditDrinkActivity.this,
                                            "Gelöscht", Toast.LENGTH_SHORT)
                                            .show();
                                    finish();
                                } else {
                                    e.printStackTrace();
                                    Toast.makeText(EditDrinkActivity.this,
                                            "FEHLER!", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
    }

    private void initMenuDrinkFromIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            final String drinkId = data.substring(data.lastIndexOf("/") + 1);
            Builders.Any.B load = Ion.with(this).load(Config.BASE_URL + "/drink/" + drinkId);
            load.asInputStream().withResponse()
                    .setCallback(new FutureCallback<Response<InputStream>>() {

                        @Override
                        public void onCompleted(Exception e, Response<InputStream> response) {
                            if (e == null) {
                                try {
                                    menuDrink = new ObjectMapper().readValue(response.getResult(), MenuDrink.class);
                                    menuDrink.setDrinkId(drinkId);
                                    findViewById(R.id.deleteButton).setVisibility(View.VISIBLE);
                                    initFieldsFromMenuDrink();
                                } catch (IOException e1) {
                                    Toast.makeText(EditDrinkActivity.this,
                                            "Fehler bei initialisieren vom Drink", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(EditDrinkActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
    }
    else {
            menuDrink = intent.getParcelableExtra(EXTRA_DRINK);
            initFieldsFromMenuDrink();
        }
}}

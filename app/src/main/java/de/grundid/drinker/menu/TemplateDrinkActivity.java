package de.grundid.drinker.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.grundid.drinker.Category;
import de.grundid.drinker.Config;
import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.R;
import de.grundid.drinker.StringListAdapter;
import de.grundid.drinker.utils.DatedResponse;
import de.grundid.drinker.utils.DrinkModelHelper;
import de.grundid.drinker.utils.IonLoaderHelper;
import de.grundid.drinker.utils.PreferencesUtils;

import static android.os.SystemClock.sleep;

public class TemplateDrinkActivity extends AppCompatActivity {

    public static final String EXTRA_LOCATION_ID = "EXTRA_LOCATION_ID";
    private RecyclerView recyclerView;
    private MenuDrink[] templates;
    private HashMap<String, MenuDrinkContainer> drinks = new HashMap<String, MenuDrinkContainer>();
    private boolean saveInProcess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_drink);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Vorlagen");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData(false);
        findViewById(R.id.saveAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAll();
            }
        });
    }

    private void loadData(boolean forceReload) {
        new DrinkTemplateLoader(this).getForDatedResponse("/drinkTemplates",
                forceReload, new IonLoaderHelper.OnDatedResponse<MenuDrink[]>() {

                    @Override
                    public void onDatedResponse(DatedResponse<MenuDrink[]> response, Exception e) {
                        if (e == null) {
                            processMenu(response.getContent());
                        }
                    }
                });
    }

    private void processMenu(MenuDrink[] content) {
        templates = content;
        recyclerView.setAdapter(new TemplateDrinkAdapter(convertToContainers(), this));
    }

    private List<Object> convertToContainers() {;
        List<Object> drinks = new ArrayList<>();
        String lastCategory = "";
        for (MenuDrink menuDrink : templates) {
            if (!lastCategory.equals(menuDrink.getCategory())) {
                drinks.add(Category.valueOf(menuDrink.getCategory()));
            }
            drinks.add(new MenuDrinkContainer(menuDrink, false));
            lastCategory = menuDrink.getCategory();
        }
        return drinks;
    }

    public void addDrink(String key, MenuDrinkContainer drink){
        if(!(drinks.containsKey(key) && drinks.get(key).getDrink() == drink.getDrink())){
            drinks.put(key, drink);
        }
        else
        {
            Toast.makeText(TemplateDrinkActivity.this, drink.getDrink().getName().toString() + drinks.get(key).getDrink().getName().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private synchronized void saveAll(){
        if(!drinks.isEmpty()){
            for (final Map.Entry<String, MenuDrinkContainer> entry : drinks.entrySet()) {
                final MenuDrink drink = entry.getValue().getDrink();

                    if (!saveInProcess) {
                        saveInProcess = true;
                        final DrinkModel drinkModel = new DrinkModel();
                        //TODO: location ID
                        drinkModel.setLocationId(getIntent().getStringExtra(EXTRA_LOCATION_ID));
                        drinkModel.setName(drink.getName());
                        drinkModel.setBrand(drink.getBrand());
                        drinkModel.setVolume(drink.getVolume());
                        drinkModel.setCategory(drink.getCategory());
                        drinkModel.setPrice(drink.getPrice());
                        drinkModel.setDescription(drink.getDescription());
                        if (DrinkModelHelper.isDrinkModelValid(drinkModel)) {
                            String method = "POST";
                            //TODO: Drink ID
                            String path = "/drink";
                            Ion.with(this).load(method, Config.BASE_URL + path).
                                    setHeader("X-User-UUID", PreferencesUtils.getUuid(this)).
                                    setJsonPojoBody(drinkModel).asString()
                                    .withResponse().setCallback(
                                    new FutureCallback<Response<String>>() {

                                        @Override
                                        public void onCompleted(Exception e, Response<String> result) {
                                            saveInProcess = false;
                                            if (e == null && result.getHeaders().code() == 200) {
                                            } else {
                                                Toast.makeText(TemplateDrinkActivity.this,
                                                        "Fehler beim Speichern: " + result.getHeaders().code(), Toast.LENGTH_SHORT)
                                                        .show();
                                                sleep(1000);
                                            }
                                        }
                                    });
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Fehlerhafte Eingaben");
                            builder.setMessage("Bitte sinnvolle Werte eingeben");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveInProcess = false;
                                    finish();
                                }
                            });
                            builder.create().show();
                        }
                    }

            }

            Toast.makeText(TemplateDrinkActivity.this, "Speichern erfolgreich", Toast.LENGTH_SHORT);
            sleep(1000);
            finish();
        }

    }

    public void deleteDrink(String name){
        drinks.remove(name);
    }

}

package de.grundid.drinker.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.grundid.drinker.Category;
import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.R;
import de.grundid.drinker.StringListAdapter;
import de.grundid.drinker.utils.DatedResponse;
import de.grundid.drinker.utils.DrinkModelHelper;
import de.grundid.drinker.utils.IonLoaderHelper;

public class TemplateDrinkActivity extends AppCompatActivity {

    public static final String EXTRA_LOCATION_ID = "EXTRA_LOCATION_ID";
    private RecyclerView recyclerView;
    private MenuDrink[] templates;
    private Map<Integer, DrinkModel> drinks;

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

    public void addDrink(Integer position, String price, String volume){
        DrinkModel drink = new DrinkModel();
        drink.setVolume(DrinkModelHelper.parseDrinkVolume(volume));
        drink.setPrice(DrinkModelHelper.parseDrinkPrice(price));
        drink.setBrand(templates[position].getBrand());
        drink.setLocationId(getIntent().getStringExtra(EXTRA_LOCATION_ID));
        drink.setCategory(templates[position].getCategory());
        drink.setDescription(templates[position].getDescription());
        drink.setName(templates[position].getName());

        if(DrinkModelHelper.isDrinkModelValid(drink)) {
            drinks.put(position, drink);
            Toast.makeText(TemplateDrinkActivity.this, "GEHT", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(TemplateDrinkActivity.this, "FEHLER" + drink.getVolume() + drink.getPrice(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteDrink(Integer position){
        drinks.remove(position);
    }

}

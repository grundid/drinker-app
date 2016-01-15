package de.grundid.drinker.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import de.grundid.drinker.Category;
import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.R;
import de.grundid.drinker.utils.DatedResponse;
import de.grundid.drinker.utils.IonLoaderHelper;

import java.util.ArrayList;
import java.util.List;

public class TemplateDrinkActivity extends AppCompatActivity {

    public static final String EXTRA_LOCATION_ID = "EXTRA_LOCATION_ID";
    private RecyclerView recyclerView;
    private MenuDrink[] templates;
    private ArrayList<MenuDrinkContainer> drinks = new ArrayList<MenuDrinkContainer>();
    private boolean saveInProcess = false;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_drink);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        category = getIntent().getStringExtra(EditDrinkActivity.EXTRA_CATEGORY);
        setTitle("Vorlagen " + category);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData(false);
        findViewById(R.id.saveAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
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


    //TODO: HIER!!!!ELF!
    private List<Object> convertToContainers() {;
        List<Object> drinks = new ArrayList<>();
        for (MenuDrink menuDrink : templates) {
            if(menuDrink.getCategory().toLowerCase().equals(category.toLowerCase()))
                drinks.add(new MenuDrinkContainer(menuDrink, false));
        }
        return drinks;
    }

    public void addDrink(MenuDrinkContainer drink){
        drinks.add(drink);
    }

    private void save() {
    }
}

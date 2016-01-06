package de.grundid.drinker.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.List;

import de.grundid.drinker.Category;
import de.grundid.drinker.R;
import de.grundid.drinker.utils.DatedResponse;
import de.grundid.drinker.utils.IonLoaderHelper;

public class TemplateDrinkActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuDrink[] templates;

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

}

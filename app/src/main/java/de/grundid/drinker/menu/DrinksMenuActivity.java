package de.grundid.drinker.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import de.grundid.drinker.Category;
import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.ItemClickListener;
import de.grundid.drinker.R;
import de.grundid.drinker.storage.DaoManager;
import de.grundid.drinker.utils.AnalyticsUtils;
import de.grundid.drinker.utils.DatedResponse;
import de.grundid.drinker.utils.IonLoaderHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DrinksMenuActivity extends AppCompatActivity implements ItemClickListener<MenuDrink> {

	private RecyclerView recyclerView;
	private Menu menu;
	private String placeId;
	private SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drinks_menu);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		placeId = getPlaceIdFromIntent();
		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent addDrinkIntent = new Intent(DrinksMenuActivity.this, EditDrinkActivity.class);
				addDrinkIntent.putExtra(EditDrinkActivity.EXTRA_LOCATION_ID, menu.getLocationId());
				startActivity(addDrinkIntent);
			}
		});
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
		swipeRefreshLayout.setEnabled(true);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override public void onRefresh() {
				loadData(true);
			}
		});
		recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		DaoManager.with(this).incLocationVisitCount(placeId);
	}

	private String getPlaceIdFromIntent() {
		Intent intent = getIntent();
		String action = intent.getAction();
		String data = intent.getDataString();
		if (Intent.ACTION_VIEW.equals(action) && data != null) {
			return data.substring(data.lastIndexOf("/") + 1);
		}
		else {
			return intent.getStringExtra("PLACE_ID");
		}
	}

	@Override protected void onStart() {
		super.onStart();
		loadData(false);
	}

	private void loadData(boolean forceReload) {
		swipeRefreshLayout.setRefreshing(true);
		new MenuLoader(this).getForDatedResponse("/menu/" + placeId,
				forceReload, new IonLoaderHelper.OnDatedResponse<Menu>() {

					@Override public void onDatedResponse(DatedResponse<Menu> response, Exception e) {
						swipeRefreshLayout.setRefreshing(false);
						if (e == null) {
							processMenu(response.getContent(), response.getDate());
						}
					}
				});
	}

	private void processMenu(Menu menu, Date responseDate) {
		this.menu = menu;
		setTitle(menu.getName());
		AnalyticsUtils.with(this).sendScreen("/menu/" + menu.getName());
		DrinkMenuComparator comparator = new DrinkMenuComparator();
		List<MenuDrink> sortedDrinks = new ArrayList<>(menu.getDrinks());
		Collections.sort(sortedDrinks, comparator);
		List<Object> drinks = new ArrayList<>();
		String lastCategory = "";
		for (MenuDrink menuDrink : sortedDrinks) {
			if (!lastCategory.equals(menuDrink.getCategory())) {
				drinks.add(Category.valueOf(menuDrink.getCategory()));
			}
			drinks.add(menuDrink);
			lastCategory = menuDrink.getCategory();
		}
		if (!drinks.isEmpty()) {
			drinks.add(new Footer(responseDate, menu.getLastUpdated()));
		}
		recyclerView.setAdapter(new DrinkAdapter(drinks, DrinksMenuActivity.this));
	}

	@Override public void onItemClick(MenuDrink item) {
		Intent addDrinkIntent = new Intent(DrinksMenuActivity.this, EditDrinkActivity.class);
		addDrinkIntent.putExtra(EditDrinkActivity.EXTRA_LOCATION_ID, menu.getLocationId());
		addDrinkIntent.putExtra(EditDrinkActivity.EXTRA_DRINK, item);
		startActivityForResult(addDrinkIntent, 1000);
	}
}

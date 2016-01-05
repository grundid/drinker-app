package de.grundid.drinker.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import de.grundid.drinker.Category;
import de.grundid.drinker.ItemClickListener;
import de.grundid.drinker.R;
import de.grundid.drinker.storage.DaoManager;
import de.grundid.drinker.utils.AnalyticsUtils;
import de.grundid.drinker.utils.DatedResponse;
import de.grundid.drinker.utils.IonLoaderHelper;

import java.util.*;

public class SectionsActivity extends AppCompatActivity implements ItemClickListener<Category> {

	private RecyclerView recyclerView;
	private Menu menu;
	private String placeId;
	private Date responseDate;
	private SwipeRefreshLayout swipeRefreshLayout;
	private SectionAdapter drinkAdapter;
	private FloatingActionButton fab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drinks_menu);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		placeId = getPlaceIdFromIntent();
		//Hier wird jetzt editiert
		fab = (FloatingActionButton)findViewById(R.id.fab);
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
		swipeRefreshLayout.setEnabled(true);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadData(true);
			}
		});
		recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
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

	@Override
	protected void onStart() {
		super.onStart();
		loadData(false);
	}

	private void loadData(boolean forceReload) {
		swipeRefreshLayout.setRefreshing(true);
		new MenuLoader(this).getForDatedResponse("/menu/" + placeId,
				forceReload, new IonLoaderHelper.OnDatedResponse<Menu>() {

					@Override
					public void onDatedResponse(DatedResponse<Menu> response, Exception e) {
						swipeRefreshLayout.setRefreshing(false);
						if (e == null) {
							processMenu(response.getContent(), response.getDate());
						}
					}
				});
	}

	private void processMenu(Menu menu, Date responseDate) {
		this.menu = menu;
		this.responseDate = responseDate;
		setTitle(menu.getName());
		AnalyticsUtils.with(this).sendScreen("/menu/" + menu.getName());
		sortMenu(false);
		fab.setOnClickListener(new AddDrinkListener(this, menu.getLocationId()));
	}

	private void sortMenu(boolean byName) {
		DrinkMenuComparator comparator = new DrinkMenuComparator(byName);
		List<MenuDrink> sortedDrinks = new ArrayList<>(menu.getDrinks());
		Collections.sort(sortedDrinks, comparator);
		Set<Object> drinks = new HashSet<>();
		for (MenuDrink menuDrink : sortedDrinks) {
			drinks.add(Category.valueOf(menuDrink.getCategory()));
		}
		drinkAdapter = new SectionAdapter(new ArrayList<>(drinks), this);
		recyclerView.setAdapter(drinkAdapter);
	}

	@Override
	public void onItemClick(Category category) {
		Intent showDrinksIntent = new Intent(this, DrinksMenuActivity.class);
		showDrinksIntent.putExtra("LOCATION_ID", menu.getLocationId());
		showDrinksIntent.putExtra("PLACE_ID", placeId);
		showDrinksIntent.putExtra("PLACE_NAME", menu.getName() + " - " + category.getLabel());
		ArrayList<MenuDrink> drinks = new ArrayList<>();
		for (MenuDrink menuDrink : menu.getDrinks()) {
			if (category.name().equals(menuDrink.getCategory())) {
				drinks.add(menuDrink);
			}
		}
		showDrinksIntent.putParcelableArrayListExtra("DRINKS", drinks);
		startActivity(showDrinksIntent);
	}
}

package de.grundid.drinker.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import de.grundid.drinker.Category;
import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.ItemClickListener;
import de.grundid.drinker.R;
import de.grundid.drinker.StringListAdapter;
import de.grundid.drinker.storage.DaoManager;
import de.grundid.drinker.storage.Location;
import de.grundid.drinker.utils.AnalyticsUtils;
import de.grundid.drinker.utils.DatedResponse;
import de.grundid.drinker.utils.IonLoaderHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DrinksMenuActivity extends AppCompatActivity implements ItemClickListener<MenuDrink> {

	private RecyclerView recyclerView;
	private Menu menu;
	private Date responseDate;
	private String placeId;
	private SwipeRefreshLayout swipeRefreshLayout;
	private DrinkAdapter drinkAdapter;
	private long lastVisit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drinks_menu);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		placeId = getPlaceIdFromIntent();
		Location location = DaoManager.with(this).selectLocation(placeId);
		lastVisit = location.getLastVisit();
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

	@Override protected void onStart() {
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
	}

	private void sortMenu(boolean byName) {
		DrinkMenuComparator comparator = new DrinkMenuComparator(byName);
		List<MenuDrink> sortedDrinks = new ArrayList<>(menu.getDrinks());
		Collections.sort(sortedDrinks, comparator);
		Set<Object> drinks = new HashSet<>();
		for (MenuDrink menuDrink : sortedDrinks) {
			drinks.add(Category.valueOf(menuDrink.getCategory()));
		}

		if(drinkAdapter == null) {
			drinkAdapter = new DrinkAdapter(new ArrayList<>(drinks), DrinksMenuActivity.this, lastVisit);
			recyclerView.setAdapter(drinkAdapter);
		}
		else {
			drinkAdapter.setDrinks(new ArrayList<>(drinks));
		}
	}

	@Override public void onItemClick(MenuDrink item) {
		Intent addDrinkIntent = new Intent(DrinksMenuActivity.this, EditDrinkActivity.class);
		addDrinkIntent.putExtra(EditDrinkActivity.EXTRA_LOCATION_ID, menu.getLocationId());
		addDrinkIntent.putExtra(EditDrinkActivity.EXTRA_DRINK, item);
		startActivityForResult(addDrinkIntent, 1000);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_sort: {
				createDialog().show();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override public boolean onCreateOptionsMenu(android.view.Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.drinkmenu_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public Dialog createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Set the dialog title
		builder.setTitle("Getränkesortierung")
				.setItems(new String[] { "Nach Preis", "Nach Name" }, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						sortMenu(which == 1);
					}
				});
		return builder.create();
	}
}

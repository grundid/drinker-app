package de.grundid.drinker.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.ItemClickListener;
import de.grundid.drinker.R;
import de.grundid.drinker.storage.DaoManager;
import de.grundid.drinker.storage.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DrinksMenuActivity extends AppCompatActivity implements ItemClickListener<MenuDrink> {

	private RecyclerView recyclerView;
	private Date responseDate;
	private String placeId;
	private String locationId;
	private SwipeRefreshLayout swipeRefreshLayout;
	private DrinkAdapter drinkAdapter;
	private long lastVisit;
	private ArrayList<MenuDrink> drinks;
	private String placeName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drinks_menu);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		initDataFromIntent();
		Location location = DaoManager.with(this).selectLocation(placeId);
		lastVisit = location.getLastVisit();
		//Hier wird jetzt editiert
		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setOnClickListener(new AddDrinkListener(this, locationId));
		swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
		swipeRefreshLayout.setEnabled(false);
		recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		DaoManager.with(this).incLocationVisitCount(placeId);
	}

	private void initDataFromIntent() {
		Intent intent = getIntent();
		placeId = intent.getStringExtra("PLACE_ID");
		placeName = intent.getStringExtra("PLACE_NAME");
		locationId = intent.getStringExtra("LOCATION_ID");
		drinks = intent.getParcelableArrayListExtra("DRINKS");
		setTitle(placeName);
	}

	@Override protected void onStart() {
		super.onStart();
		sortMenu(false);
		//AnalyticsUtils.with(this).sendScreen("/menu/" + menu.getName());
	}

	private void sortMenu(boolean byName) {
		DrinkMenuComparator comparator = new DrinkMenuComparator(byName);
		List<MenuDrink> sortedDrinks = new ArrayList<>(drinks);
		Collections.sort(sortedDrinks, comparator);
		List<Object> localDrinks = new ArrayList<>();
		localDrinks.addAll(sortedDrinks);
		if (drinkAdapter == null) {
			drinkAdapter = new DrinkAdapter(localDrinks, DrinksMenuActivity.this, lastVisit);
			recyclerView.setAdapter(drinkAdapter);
		}
		else {
			drinkAdapter.setDrinks(localDrinks);
		}
	}

	@Override
	public void onItemClick(MenuDrink item) {
		Intent addDrinkIntent = new Intent(DrinksMenuActivity.this, EditDrinkActivity.class);
		addDrinkIntent.putExtra(EditDrinkActivity.EXTRA_LOCATION_ID, locationId);
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

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
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

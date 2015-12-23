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
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import de.grundid.drinker.AddDrinkActivity;
import de.grundid.drinker.Category;
import de.grundid.drinker.R;
import de.grundid.drinker.storage.DaoManager;
import de.grundid.drinker.utils.DatedResponse;
import de.grundid.drinker.utils.EmptyElement;
import de.grundid.drinker.utils.IonLoaderHelper;

import java.util.*;

public class DrinksMenuActivity extends AppCompatActivity {

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
		Intent intent = getIntent();
		placeId = intent.getStringExtra("PLACE_ID");
		FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent addDrinkIntent = new Intent(DrinksMenuActivity.this, AddDrinkActivity.class);
				addDrinkIntent.putExtra("LOCATION_ID", menu.getLocationId());
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

	@Override protected void onStart() {
		super.onStart();
		loadData(false);
	}

	private void loadData(boolean forceReload) {
		swipeRefreshLayout.setRefreshing(true);
		new MenuLoader(this).getForDatedResponse("/drinksmenu/menu/" + placeId,
				forceReload, new IonLoaderHelper.OnDatedResponse<Menu>() {

					@Override public void onDatedResponse(DatedResponse<Menu> response, Exception e) {
						swipeRefreshLayout.setRefreshing(false);
						if (e == null) {
							menu = response.getContent();
							setTitle(menu.getName());
							DrinkMenuComparator comparator = new DrinkMenuComparator();
							List<Object> drinks = new ArrayList<>();
							for (Map.Entry<Category, Set<MenuDrink>> entrySet : menu.getDrinks().entrySet()) {
								drinks.add(entrySet.getKey());
								SortedSet<MenuDrink> sortedSet = new TreeSet<>(comparator);
								sortedSet.addAll(entrySet.getValue());
								drinks.addAll(sortedSet);
							}
							if (!drinks.isEmpty()) {
								drinks.add(new Footer(response.getDate(), menu.getLastUpdated()));
							}
							recyclerView.setAdapter(new DrinkAdapter(drinks));
						}
					}
				});
	}
}

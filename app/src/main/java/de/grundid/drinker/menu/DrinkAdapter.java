package de.grundid.drinker.menu;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import de.grundid.drinker.ItemClickListener;
import de.grundid.drinker.R;
import de.grundid.drinker.utils.EmptyStateAdapter;
import de.grundid.drinker.utils.ListElement;

import java.util.List;

public class DrinkAdapter extends EmptyStateAdapter {

	private static final int TYPE_SECTION = 1;
	private static final int TYPE_DRINK = 2;
	private static final int TYPE_FOOTER = 3;
	private final ItemClickListener<MenuDrink> itemClickListener;
	private final long lastVisit;

	public DrinkAdapter(List<ListElement> elements, ItemClickListener<MenuDrink> itemClickListener, long lastVisit) {
		super(elements, R.string.empty_state_drinksmenu);
		this.itemClickListener = itemClickListener;
		this.lastVisit = lastVisit;
	}

	@Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
			case TYPE_DRINK:
				return new DrinkViewHolder(
						LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_drink, parent, false));
			case TYPE_SECTION:
				return new SectionViewHolder(
						LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_section, parent, false));
			case TYPE_FOOTER:
				return new FooterViewHolder(
						LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_footer, parent, false));
			default:
				return super.onCreateViewHolder(parent, viewType);
		}
	}

	@Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof DrinkViewHolder) {
			DrinkViewHolder drinkViewHolder = (DrinkViewHolder)holder;
			final MenuDrink menuDrink = getElementsObject(position);
			drinkViewHolder.update(menuDrink, lastVisit);
			drinkViewHolder.getMoreButton().setOnClickListener(new View.OnClickListener() {

				@Override public void onClick(View v) {
					PopupMenu popup = new PopupMenu(v.getContext(), v);
					popup.inflate(R.menu.drink_menu);
					popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

						@Override public boolean onMenuItemClick(MenuItem item) {
							itemClickListener.onItemClick(menuDrink);
							return true;
						}
					});
					popup.show();
				}
			});
		}
		else if (holder instanceof SectionViewHolder) {
			((SectionViewHolder)holder).update((CategoryWithDrinksModel)getElementsObject(position), lastVisit);
		}
		else if (holder instanceof FooterViewHolder) {
			((FooterViewHolder)holder).update((Footer)getElementsObject(position));
		}
		else {
			super.onBindViewHolder(holder, position);
		}
	}

	public void setDrinks(List<ListElement> elements) {
		this.elements = elements;
		notifyDataSetChanged();
	}
}

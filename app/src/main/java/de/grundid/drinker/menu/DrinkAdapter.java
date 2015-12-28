package de.grundid.drinker.menu;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import de.grundid.drinker.Category;
import de.grundid.drinker.ItemClickListener;
import de.grundid.drinker.R;
import de.grundid.drinker.utils.EmptyElement;
import de.grundid.drinker.utils.EmptyStateAdapter;
import de.grundid.drinker.utils.EmptyStateViewHolder;

import java.util.*;

public class DrinkAdapter extends EmptyStateAdapter {

	private static final int TYPE_SECTION = 1;
	private static final int TYPE_DRINK = 2;
	private static final int TYPE_FOOTER = 3;
	private final ItemClickListener<MenuDrink> itemClickListener;

	public DrinkAdapter(List<Object> drinks, ItemClickListener<MenuDrink> itemClickListener) {
		super(drinks, R.string.empty_state_drinksmenu);
		this.itemClickListener = itemClickListener;
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
			final MenuDrink menuDrink = (MenuDrink)elements.get(position);
			drinkViewHolder.update(menuDrink);
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
			((SectionViewHolder)holder).update((Category)elements.get(position));
		}
		else if (holder instanceof FooterViewHolder) {
			((FooterViewHolder)holder).update((Footer)elements.get(position));
		}
		else {
			super.onBindViewHolder(holder, position);
		}
	}

	@Override public int getItemViewType(int position) {
		Object object = elements.get(position);
		if (object instanceof Category) {
			return TYPE_SECTION;
		}
		else if (object instanceof MenuDrink) {
			return TYPE_DRINK;
		}
		else if (object instanceof Footer) {
			return TYPE_FOOTER;
		}
		else {
			return super.getItemViewType(position);
		}
	}
}

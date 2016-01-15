package de.grundid.drinker.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import de.grundid.drinker.R;
import de.grundid.drinker.Utils;

public class DrinkViewHolder extends RecyclerView.ViewHolder {

	private TextView brand;
	private TextView name;
	private TextView description;
	private ImageView newIcon;
	private ImageButton moreButton;
	private ViewGroup viewGroup;

	public DrinkViewHolder(View itemView) {
		super(itemView);
		brand = (TextView)itemView.findViewById(R.id.drinkBrand);
		name = (TextView)itemView.findViewById(R.id.drinkName);
		description = (TextView)itemView.findViewById(R.id.drinkDescription);
		moreButton = (ImageButton)itemView.findViewById(R.id.moreActions);
		newIcon = (ImageView)itemView.findViewById(R.id.new_icon);
		viewGroup = (ViewGroup)itemView.findViewById(R.id.priceList);
	}

	public void update(SimpleDrink drink, long lastVisit) {
		if (lastVisit > drink.getModifiedDate())
			newIcon.setVisibility(View.GONE);
		else
			newIcon.setVisibility(View.VISIBLE);
		brand.setVisibility(Utils.hasText(drink.getBrand()) ? View.VISIBLE : View.GONE);
		brand.setText(drink.getBrand());
		name.setText(drink.getName());
		if (Utils.hasText(drink.getDescription())) {
			description.setText(drink.getDescription());
			description.setVisibility(View.VISIBLE);
		}
		else {
			description.setVisibility(View.INVISIBLE);
		}
		viewGroup.removeAllViews();
		LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
		for (VolumePrice volumePrice : drink.getVolumePrices()) {
			View view = layoutInflater.inflate(R.layout.menu_volume_price, viewGroup, false);
			VolumePriceViewHolder volumePriceViewHolder = new VolumePriceViewHolder(view);
			volumePriceViewHolder.update(volumePrice);
			viewGroup.addView(view);
		}
	}

	public ImageButton getMoreButton() {
		return moreButton;
	}

	public void hideMoreActions() {
		itemView.findViewById(R.id.moreActions).setVisibility(View.GONE);
	}
}

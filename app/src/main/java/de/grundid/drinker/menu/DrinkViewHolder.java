package de.grundid.drinker.menu;

import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import de.grundid.drinker.R;
import de.grundid.drinker.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DrinkViewHolder extends RecyclerView.ViewHolder {

	private NumberFormat priceFormat = DecimalFormat.getCurrencyInstance(Locale.GERMANY);
	private NumberFormat volumeFormat = new DecimalFormat("0.0#'l'");
	private TextView brand;
	private TextView name;
	private TextView description;
	private TextView price;
	private TextView volume;
	private TextView pricePerVolume;
	private ImageView newIcon;
	private ImageButton moreButton;

	public DrinkViewHolder(View itemView) {
		super(itemView);
		brand = (TextView)itemView.findViewById(R.id.drinkBrand);
		name = (TextView)itemView.findViewById(R.id.drinkName);
		price = (TextView)itemView.findViewById(R.id.drinkPrice);
		volume = (TextView)itemView.findViewById(R.id.drinkVolume);
		description = (TextView)itemView.findViewById(R.id.drinkDescription);
		pricePerVolume = (TextView)itemView.findViewById(R.id.drinkPricePerVolume);
		moreButton = (ImageButton)itemView.findViewById(R.id.moreActions);
		newIcon = (ImageView)itemView.findViewById(R.id.new_icon);
	}

	public void update(SimpleDrink drink, long lastVisit) {
		if(lastVisit > drink.getModifiedDate())
			newIcon.setVisibility(View.GONE);
		else
			newIcon.setVisibility(View.VISIBLE);
		brand.setVisibility(Utils.hasText(drink.getBrand()) ? View.VISIBLE : View.GONE);
		brand.setText(drink.getBrand());
		name.setText(drink.getName());
		price.setText(priceFormat.format((double)drink.getPrice() / 100));
		volume.setVisibility(drink.getVolume() != null ? View.VISIBLE : View.GONE);
		if (Double.isNaN(drink.getPricePerVolume())) {
			pricePerVolume.setVisibility(View.INVISIBLE);
		}
		else {
			volume.setText(volumeFormat.format((double)drink.getVolume() / 1000));
			pricePerVolume.setText(
					priceFormat.format(drink.getPricePerVolume())
							+ " / 100ml");
			pricePerVolume.setVisibility(View.VISIBLE);
		}
		if (Utils.hasText(drink.getDescription())) {
			description.setText(drink.getDescription());
			description.setVisibility(View.VISIBLE);
		}
		else {
			description.setVisibility(View.INVISIBLE);
		}
	}

	public ImageButton getMoreButton() {
		return moreButton;
	}

	public void hideMoreActions() {
		itemView.findViewById(R.id.moreActions).setVisibility(View.GONE);
	}
}

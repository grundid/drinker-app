package de.grundid.drinker.menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
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
	private TextView price;
	private TextView volume;
	private TextView pricePerVolume;

	public DrinkViewHolder(View itemView) {
		super(itemView);
		brand = (TextView)itemView.findViewById(R.id.drinkBrand);
		name = (TextView)itemView.findViewById(R.id.drinkName);
		price = (TextView)itemView.findViewById(R.id.drinkPrice);
		volume = (TextView)itemView.findViewById(R.id.drinkVolume);
		pricePerVolume = (TextView)itemView.findViewById(R.id.drinkPricePerVolume);
	}

	public void update(MenuDrink drink) {
		brand.setVisibility(Utils.hasText(drink.getBrand()) ? View.VISIBLE : View.GONE);
		brand.setText(drink.getBrand());
		name.setText(drink.getName());
		price.setText(priceFormat.format((double)drink.getPrice() / 100));
		volume.setVisibility(drink.getVolume() != null ? View.VISIBLE : View.GONE);
		if (drink.getVolume() != null) {
			volume.setText(volumeFormat.format((double)drink.getVolume() / 1000));
			pricePerVolume.setText(priceFormat.format(((double)drink.getPrice() / 100) / ((double)drink.getVolume() / 100) )+" / 100ml");
			pricePerVolume.setVisibility(View.VISIBLE);
		} else {
			pricePerVolume.setVisibility(View.INVISIBLE);
		}
	}
}

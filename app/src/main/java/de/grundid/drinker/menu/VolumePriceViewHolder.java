package de.grundid.drinker.menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import de.grundid.drinker.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class VolumePriceViewHolder extends RecyclerView.ViewHolder {

	private NumberFormat priceFormat = DecimalFormat.getCurrencyInstance(Locale.GERMANY);
	private NumberFormat volumeFormat = new DecimalFormat("0.0#'l'");
	private TextView price;
	private TextView volume;
	private TextView pricePerVolume;

	public VolumePriceViewHolder(View itemView) {
		super(itemView);
		price = (TextView)itemView.findViewById(R.id.drinkPrice);
		volume = (TextView)itemView.findViewById(R.id.drinkVolume);
		pricePerVolume = (TextView)itemView.findViewById(R.id.drinkPricePerVolume);
	}

	public void update(VolumePrice volumePrice) {
		price.setText(priceFormat.format((double)volumePrice.getPrice() / 100));
		volume.setVisibility(volumePrice.getVolume() != null ? View.VISIBLE : View.GONE);
		if (Double.isNaN(volumePrice.getPricePerVolume())) {
			pricePerVolume.setVisibility(View.INVISIBLE);
		}
		else {
			volume.setText(volumeFormat.format((double)volumePrice.getVolume() / 1000));
			pricePerVolume.setText(
					priceFormat.format(volumePrice.getPricePerVolume())
							+ " / 100ml");
			pricePerVolume.setVisibility(View.VISIBLE);
		}
	}
}

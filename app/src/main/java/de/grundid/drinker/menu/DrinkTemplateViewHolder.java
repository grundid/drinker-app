package de.grundid.drinker.menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import de.grundid.drinker.R;
import de.grundid.drinker.Utils;

public class DrinkTemplateViewHolder extends RecyclerView.ViewHolder {

    private NumberFormat priceFormat = DecimalFormat.getCurrencyInstance(Locale.GERMANY);
    private NumberFormat volumeFormat = new DecimalFormat("0.0#'l'");
    private TextView brand;
    private TextView name;
    private TextView description;
    private TextView price;
    private TextView volume;
    private CheckBox checkbox;

    public DrinkTemplateViewHolder(View itemView) {
        super(itemView);
        brand = (TextView)itemView.findViewById(R.id.drinkBrand);
        name = (TextView)itemView.findViewById(R.id.drinkName);
        price = (TextView)itemView.findViewById(R.id.drinkPrice);
        volume = (TextView)itemView.findViewById(R.id.drinkVolume);
        description = (TextView)itemView.findViewById(R.id.drinkDescription);
        checkbox = (CheckBox)itemView.findViewById(R.id.checkbox);
    }

    public void update(SimpleDrink drink) {
        brand.setVisibility(Utils.hasText(drink.getBrand()) ? View.VISIBLE : View.GONE);
        brand.setText(drink.getBrand());
        name.setText(drink.getName());
        price.setText(priceFormat.format((double)drink.getPrice() / 100));
        volume.setVisibility(drink.getVolume() != null ? View.VISIBLE : View.GONE);

        if (Utils.hasText(drink.getDescription())) {
            description.setText(drink.getDescription());
            description.setVisibility(View.VISIBLE);
        }
        else {
            description.setVisibility(View.INVISIBLE);
        }
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }
}

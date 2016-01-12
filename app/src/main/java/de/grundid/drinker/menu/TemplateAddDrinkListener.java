package de.grundid.drinker.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.R;
import de.grundid.drinker.utils.DrinkModelHelper;

public class TemplateAddDrinkListener implements View.OnClickListener {

    private TemplateDrinkActivity activity;
    private MenuDrinkContainer menuDrink;
    private TemplateDrinkAdapter adapter;

    public TemplateAddDrinkListener(TemplateDrinkActivity activity, MenuDrinkContainer menuDrink, TemplateDrinkAdapter templateDrinkAdapter) {
        this.activity = activity;
        this.menuDrink = menuDrink;
        this.adapter = templateDrinkAdapter;
    }

    @Override
    public void onClick(View v) {
        final View inputFields = LayoutInflater.from(activity).inflate(R.layout.template_popup, null);
        final TextView price = (TextView) inputFields.findViewById(R.id.drinkPrice);
        price.setKeyListener(new EditDrinkActivity.NumericDigitsKeyListener());
        final TextView volume = (TextView) inputFields.findViewById(R.id.drinkVolume);
        volume.setKeyListener(new EditDrinkActivity.NumericDigitsKeyListener());

        if (!menuDrink.isChecked()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Preis und Menge")
                    .setView(inputFields)
                    .setPositiveButton(
                            "Hinzufügen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MenuDrink drink = menuDrink.getDrink();
                                    drink.setPrice(DrinkModelHelper.parseDrinkPrice(price.getText()));
                                    drink.setVolume(DrinkModelHelper.parseDrinkVolume(volume.getText()));

                                    if (price.getText() != null && volume.getText() != null && drink.getName() != null) {
                                        activity.addDrink(menuDrink);
                                        menuDrink.setChecked(true);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(activity, "Bitte gültige Werte eintragen", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setNegativeButton("Abbrechen", null);
            builder.create().show();
        } else {
            MenuDrink drink = menuDrink.getDrink();
            menuDrink.setChecked(false);
            adapter.notifyDataSetChanged();
        }

    }
}

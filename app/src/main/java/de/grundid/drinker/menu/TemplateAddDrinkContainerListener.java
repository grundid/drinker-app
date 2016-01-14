package de.grundid.drinker.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.R;
import de.grundid.drinker.utils.DrinkModelHelper;

public class TemplateAddDrinkContainerListener implements View.OnClickListener {

    private final List<Object> elements;
    private final MenuDrinkContainer menuDrink;
    private final TemplateDrinkActivity activity;
    private final TemplateDrinkAdapter adapter;

    private NumberFormat priceFormat = DecimalFormat.getCurrencyInstance(Locale.GERMANY);
    private NumberFormat volumeFormat = new DecimalFormat("0.0#'l'");

    public TemplateAddDrinkContainerListener(TemplateDrinkActivity activity, TemplateDrinkAdapter templateDrinkAdapter, List<Object> elements, MenuDrinkContainer menuDrink) {
        this.elements = elements;
        this.menuDrink = menuDrink;
        this.activity = activity;
        this.adapter = templateDrinkAdapter;
    }

    @Override
    public void onClick(View v) {
        final MenuDrink drink = menuDrink.getDrink();
        final View inputFields = LayoutInflater.from(activity).inflate(R.layout.template_popup, null);
        final TextView price = (TextView) inputFields.findViewById(R.id.drinkPrice);
        price.setKeyListener(new EditDrinkActivity.NumericDigitsKeyListener());
        final AutoCompleteTextView volume = (AutoCompleteTextView) inputFields.findViewById(R.id.drinkVolume);
        volume.setKeyListener(new EditDrinkActivity.NumericDigitsKeyListener());
        volume.setAdapter(ArrayAdapter.createFromResource(activity,
                R.array.volumes, android.R.layout.simple_list_item_1));

        try {
            price.setText(priceFormat.format((double) drink.getPrice() / 100));
        } catch (Exception e) {
            price.setText(Integer.toString(0));
        }
        try {
            volume.setText(volumeFormat.format((double) drink.getVolume() / 1000));
        } catch (Exception e) {
            volume.setText(Integer.toString(0));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Preis und Menge")
                .setView(inputFields)
                .setPositiveButton(
                        "Hinzufügen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MenuDrink newDrink = new MenuDrink();
                                newDrink.setPrice(DrinkModelHelper.parseDrinkPrice(price.getText()));
                                newDrink.setVolume(DrinkModelHelper.parseDrinkVolume(volume.getText()));
                                newDrink.setBrand(drink.getBrand());
                                newDrink.setName(drink.getName());
                                newDrink.setDescription(drink.getDescription());
                                newDrink.setCategory(drink.getCategory());
                                MenuDrinkContainer newDrinkContainer = new MenuDrinkContainer(newDrink, false);

                                if (price.getText() != null && volume.getText() != null && newDrink.getName() != null) {
                                    activity.addDrink(newDrinkContainer);
                                    newDrinkContainer.setChecked(true);
                                    elements.add(elements.indexOf(menuDrink), newDrinkContainer);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(activity, "Bitte gültige Werte eintragen", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Abbrechen", null);
        builder.create().show();
    }

}

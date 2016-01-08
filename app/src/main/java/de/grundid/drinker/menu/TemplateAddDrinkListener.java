package de.grundid.drinker.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.support.v7.widget.DialogTitle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.R;
import de.grundid.drinker.utils.DrinkModelHelper;

public class TemplateAddDrinkListener implements View.OnClickListener {

    private TemplateDrinkActivity activity;
    private MenuDrinkContainer menuDrink;

    public TemplateAddDrinkListener(TemplateDrinkActivity activity, MenuDrinkContainer menuDrink) {
        this.activity = activity;
        this.menuDrink = menuDrink;
    }

    @Override
    public void onClick(View v) {
        final View inputFields = LayoutInflater.from(activity).inflate(R.layout.template_popup, null);
        final CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkbox);
        if(!checkBox.isChecked()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Preis und Menge")
                    .setView(inputFields)
                    .setPositiveButton(
                            "Hinzufügen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    TextView price = (TextView) inputFields.findViewById(R.id.drinkPrice);
                                    TextView volume = (TextView) inputFields.findViewById(R.id.drinkVolume);
                                    MenuDrink drink = menuDrink.getDrink();
                                    drink.setPrice(DrinkModelHelper.parseDrinkPrice(price.getText()));
                                    drink.setVolume(DrinkModelHelper.parseDrinkVolume(volume.getText()));
                                    menuDrink.setDrink(drink);
                                    if (price.getText().toString() != null && volume.getText() != null && drink.getName().toString() != null) {
                                        activity.addDrink(drink.getName().toString(), menuDrink);
                                        checkBox.setChecked(true);
                                    } else {
                                        Toast.makeText(activity, "Bitte gültige Werte eintragen", Toast.LENGTH_SHORT);
                                    }
                                }
                            }).setNegativeButton("Abbrechen", null);
            builder.create().show();
        } else {
            MenuDrink drink = menuDrink.getDrink();
            checkBox.setChecked(false);
            activity.deleteDrink(drink.getName());
        }


    }
}

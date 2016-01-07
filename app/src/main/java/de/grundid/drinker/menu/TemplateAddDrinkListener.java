package de.grundid.drinker.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.R;

public class TemplateAddDrinkListener implements View.OnClickListener {

	private TemplateDrinkActivity activity;
	private String locationId;
	private int position;

	public TemplateAddDrinkListener(TemplateDrinkActivity activity, String locationId, int position) {
		this.activity = activity;
		this.locationId = locationId;
		this.position = position;
	}

	@Override public void onClick(View v) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("Preis und Menge")
					.setView(LayoutInflater.from(activity).inflate(R.layout.template_popup, null))
					.setPositiveButton(
							"Hinzufügen", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									activity.addDrink(position, activity.findViewById(R.id.drinkPrice).toString(), activity.findViewById(R.id.drinkVolume).toString());
								}
							}).setNegativeButton("Abbrechen", null);
			builder.create().show();


	}
}

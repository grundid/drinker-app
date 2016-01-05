package de.grundid.drinker.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import de.grundid.drinker.EditDrinkActivity;

public class AddDrinkListener implements View.OnClickListener {

	private Activity activity;
	private String locationId;

	public AddDrinkListener(Activity activity, String locationId) {
		this.activity = activity;
		this.locationId = locationId;
	}

	@Override public void onClick(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		// Set the dialog title
		builder.setTitle("Editiermodus")
				.setItems(new String[] { "Einzeln", "Nach Vorlage" }, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Intent addDrinkIntentSingle = new Intent(activity,
									EditDrinkActivity.class);
							addDrinkIntentSingle
									.putExtra(EditDrinkActivity.EXTRA_LOCATION_ID, locationId);
							activity.startActivity(addDrinkIntentSingle);
						}
						else {
							Intent addDrinkIntentTemplate = new Intent(activity,
									TemplateDrinkActivity.class);
							activity.startActivity(addDrinkIntentTemplate);
						}
					}
				});
		builder.create().show();
	}
}

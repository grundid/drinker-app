package de.grundid.drinker.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;
import de.grundid.drinker.Config;
import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.utils.CategoryHelper;
import de.grundid.drinker.utils.Suggest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddDrinkListener implements View.OnClickListener {

    private Activity activity;
    private String locationId;
    private CategoryHelper categories;
    private Suggest suggest;

    public AddDrinkListener(Activity activity, String locationId) {
        this.activity = activity;
        this.locationId = locationId;
    }

    private void showCategorySelector() {
        Builders.Any.B load = Ion.with(activity).load(Config.BASE_URL + "/suggest");
         load.asInputStream().withResponse()
                .setCallback(new FutureCallback<Response<InputStream>>() {

                    @Override
                    public void onCompleted(Exception e, Response<InputStream> response) {
                        if (e == null) {
                            try {
                                suggest = new ObjectMapper().readValue(response.getResult(), Suggest.class);
                                categories = new CategoryHelper(suggest.getCategories());

                                List<String> categoriesToSort = new ArrayList<String>(categories.getCategories());
                                Collections.sort(categoriesToSort);
                                final String[] sortedCategories = categoriesToSort.toArray(new String[0]);


                                AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
                                builder2.setTitle("Kategorie auswählen")
                                        .setItems(sortedCategories,

                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent addDrinkIntentTemplate = new Intent(activity,
                                                                TemplateDrinkActivity.class);
                                                        addDrinkIntentTemplate
                                                                .putExtra(EditDrinkActivity.EXTRA_LOCATION_ID, locationId);
                                                        addDrinkIntentTemplate
                                                                .putExtra(EditDrinkActivity.EXTRA_CATEGORY, sortedCategories[which]);
                                                        activity.startActivity(addDrinkIntentTemplate);
                                                    }
                                                });
                                builder2.show();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                Toast.makeText(activity,
                                        "Fehler bei initialisieren der Kategorien", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Set the dialog title
        builder.setTitle("Editiermodus")
                .setItems(new String[]{"Einzeln", "Nach Vorlage"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent addDrinkIntentSingle = new Intent(activity,
                                    EditDrinkActivity.class);
                            addDrinkIntentSingle
                                    .putExtra(EditDrinkActivity.EXTRA_LOCATION_ID, locationId);
                            activity.startActivity(addDrinkIntentSingle);
                        } else {
                            showCategorySelector();
                        }
                    }
                });
        builder.create().show();
    }
}

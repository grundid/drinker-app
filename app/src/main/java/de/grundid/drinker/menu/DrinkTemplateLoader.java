package de.grundid.drinker.menu;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koushikdutta.ion.Response;

import java.io.InputStream;

import de.grundid.drinker.utils.IonLoaderHelper;

public class DrinkTemplateLoader extends IonLoaderHelper<MenuDrink[]>{


    public DrinkTemplateLoader(Context context) {
        super(context);
    }

    @Override
    protected MenuDrink[] processResponse(Response<InputStream> response) throws Exception {
        return new ObjectMapper().readValue(response.getResult(), MenuDrink[].class);
    }
}

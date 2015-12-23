package de.grundid.drinker.menu;

import android.content.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koushikdutta.ion.Response;
import de.grundid.drinker.utils.IonLoaderHelper;

import java.io.InputStream;

public class MenuLoader extends IonLoaderHelper<Menu> {

	public MenuLoader(Context context) {
		super(context);
	}

	@Override protected Menu processResponse(Response<InputStream> response) throws Exception {
		return new ObjectMapper().readValue(response.getResult(), Menu.class);
	}
}

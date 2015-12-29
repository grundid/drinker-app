package de.grundid.drinker.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;
import de.grundid.drinker.Config;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class IonLoaderHelper<T> {

	public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
	public static final String PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz";
	public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
	public static final String[] DATE_PATTERNS = { PATTERN_RFC1123, PATTERN_RFC1036, PATTERN_ASCTIME };
	private Context context;

	public IonLoaderHelper(Context context) {
		this.context = context;
	}

	protected abstract T processResponse(Response<InputStream> response) throws Exception;

	protected Date parseHeaderDate(String date) {
		for (String pattern : DATE_PATTERNS) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
			try {
				return sdf.parse(date);
			}
			catch (ParseException e) {
			}
		}
		return new Date();
	}

	public void getForDatedResponse(String url, boolean forceReload, final OnDatedResponse<T> onDatedResponse) {
		Builders.Any.B load = Ion.with(context).load(Config.BASE_URL + url);
		Log.i("DRINKER", "Request URL: " + Config.BASE_URL + url);
		if (forceReload) {
			load.addHeader("cache-control", "max-age=0");
		}
		load.asInputStream().withResponse()
				.setCallback(new FutureCallback<Response<InputStream>>() {

					@Override
					public void onCompleted(Exception e, Response<InputStream> response) {
						if (e == null) {
							try {
								DatedResponse<T> result = new DatedResponse<>();
								result.setDate(parseHeaderDate(response.getHeaders().getHeaders().get("date")));
								Log.i("DRINKER", "Request-Date: " + result.getDate());
								result.setContent(processResponse(response));
								onDatedResponse.onDatedResponse(result, null);
							}
							catch (Exception e1) {
								Toast.makeText(context, e1.getMessage(), Toast.LENGTH_LONG).show();
								onDatedResponse.onDatedResponse(null, e1);
							}
						}
						else {
							Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
							onDatedResponse.onDatedResponse(null, e);
						}
					}
				});
	}

	public interface OnDatedResponse<T> {

		void onDatedResponse(DatedResponse<T> response, Exception e);
	}
}


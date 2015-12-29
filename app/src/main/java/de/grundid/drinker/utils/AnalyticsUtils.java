package de.grundid.drinker.utils;

import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import de.grundid.drinker.R;

public class AnalyticsUtils {

	private Tracker tracker;

	private AnalyticsUtils(Tracker tracker) {
		this.tracker = tracker;
	}

	public static AnalyticsUtils with(Context context) {
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
		Tracker tracker = analytics.newTracker(R.xml.main_tracker);
		return new AnalyticsUtils(tracker);
	}

	public AnalyticsUtils sendScreen(String screenName) {
		tracker.setScreenName(screenName);
		tracker.send(new HitBuilders.ScreenViewBuilder().build());
		return this;
	}

	public AnalyticsUtils sendEvent(String category, String action, String label) {
		tracker.send(new HitBuilders.EventBuilder()
				.setCategory(category)
				.setAction(action)
				.setLabel(label)
				.build());
		return this;
	}
}

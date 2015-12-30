package de.grundid.drinker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import de.grundid.drinker.utils.AnalyticsUtils;

public class InfoActivity extends AppCompatActivity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Über Drinker");
		webView = new WebView(this);
		setContentView(webView);
		webView.setWebViewClient(new WebViewClient() {

			@Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
				return true;
			}
		});
	}

	@Override protected void onStart() {
		super.onStart();
		webView.loadUrl("file:///android_asset/index.html");
		AnalyticsUtils.with(this).sendScreen("/aboutus");
	}
}

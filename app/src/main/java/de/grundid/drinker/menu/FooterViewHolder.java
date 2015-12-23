package de.grundid.drinker.menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import de.grundid.drinker.R;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FooterViewHolder extends RecyclerView.ViewHolder {

	private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
	private TextView responseDate;
	private TextView menuDate;

	public FooterViewHolder(View itemView) {
		super(itemView);
		responseDate = (TextView)itemView.findViewById(R.id.responseDate);
		menuDate = (TextView)itemView.findViewById(R.id.menuDate);
	}

	public void update(Footer footer) {
		responseDate.setText("Zuletzt aktualisiert: " + dateFormat.format(footer.getResponseDate()));
		menuDate.setText("Getränkekarte geändert am: " + dateFormat.format(new Date(footer.getLastUpdated())));
	}
}

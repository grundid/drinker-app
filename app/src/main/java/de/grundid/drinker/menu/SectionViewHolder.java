package de.grundid.drinker.menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import de.grundid.drinker.Category;
import de.grundid.drinker.R;
import de.grundid.drinker.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class SectionViewHolder extends RecyclerView.ViewHolder {

	private TextView title;

	public SectionViewHolder(View itemView) {
		super(itemView);
		title = (TextView)itemView.findViewById(R.id.sectionTitle);
	}

	public void update(Category category) {
		title.setText(category.getLabel());
	}
}

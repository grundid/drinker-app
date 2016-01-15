package de.grundid.drinker.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import de.grundid.drinker.R;

public class EmptyStateViewHolder extends RecyclerView.ViewHolder {

	private TextView textView;

	public EmptyStateViewHolder(View itemView) {
		super(itemView);
		textView = (TextView)itemView.findViewById(R.id.text);
	}

	public void update(EmptyElement emptyElement) {
		textView.setText((Integer)emptyElement.getElement());
	}
}

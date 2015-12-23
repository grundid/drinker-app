package de.grundid.drinker.location;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import de.grundid.drinker.R;
import de.grundid.drinker.storage.Location;

public class LocationViewHolder extends RecyclerView.ViewHolder {

	private TextView name;
	private TextView address;

	public LocationViewHolder(View itemView) {
		super(itemView);
		name = (TextView)itemView.findViewById(R.id.locationName);
		address = (TextView)itemView.findViewById(R.id.locationAddress);
	}

	public void update(Location location) {
		name.setText(location.getName()); //+ " ("+location.getVisits()+")");
		address.setText(location.getAddress());
	}
}

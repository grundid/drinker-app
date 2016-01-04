package de.grundid.drinker.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.grundid.drinker.ItemClickListener;
import de.grundid.drinker.R;
import de.grundid.drinker.storage.DaoManager;
import de.grundid.drinker.storage.Location;
import de.grundid.drinker.utils.EmptyElement;
import de.grundid.drinker.utils.EmptyStateAdapter;

import java.util.List;

public class LocationAdapter extends EmptyStateAdapter {

	private static final int TYPE_LOCATION = 1;
	private ItemClickListener<String> placeIdClickListener;
	private final Activity activity;

	public LocationAdapter(List<Object> locations, ItemClickListener<String> placeIdClickListener, Activity activity) {
		super(locations, R.string.empty_state_locations);
		this.placeIdClickListener = placeIdClickListener;
		this.activity = activity;
	}

	@Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_LOCATION) {
			return new LocationViewHolder(
					LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false));
		}
		else
			return super.onCreateViewHolder(parent, viewType);
	}

	@Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof  LocationViewHolder) {
			final Location location = (Location)elements.get(position);
			((LocationViewHolder)holder).update(location);
			holder.itemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					placeIdClickListener.onItemClick(location.getPlaceId());
				}
			});
			holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setTitle("Location löschen").setMessage("Möchtest du diese Location löschen?")
							.setPositiveButton(
									"Ja", new DialogInterface.OnClickListener() {
										@Override public void onClick(DialogInterface dialog, int which) {
											DaoManager.with(activity).deleteLocation(location.getPlaceId());
											List<?> locations = DaoManager.with(activity).selectAllLocations();
											elements = (List<Object>)locations;
											notifyDataSetChanged();
										}
									}).setNegativeButton("Nein", null).create().show();
					return false;
				}
			});
		}
		else {
			super.onBindViewHolder(holder, position);
		}
	}

	@Override public int getItemViewType(int position) {
		Object element = elements.get(position);
		if (element instanceof Location) {
			return TYPE_LOCATION;
		} else {
			return super.getItemViewType(position);
		}
	}


}

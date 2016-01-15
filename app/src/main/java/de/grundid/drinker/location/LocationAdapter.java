package de.grundid.drinker.location;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.grundid.drinker.DeleteRequestListener;
import de.grundid.drinker.ItemClickListener;
import de.grundid.drinker.R;
import de.grundid.drinker.storage.Location;
import de.grundid.drinker.utils.EmptyStateAdapter;
import de.grundid.drinker.utils.ListElement;

import java.util.List;

public class LocationAdapter extends EmptyStateAdapter {

	public static final int TYPE_LOCATION = 1;
	private ItemClickListener<String> placeIdClickListener;
	private DeleteRequestListener<Location> deleteRequestListener;

	public LocationAdapter(List<ListElement> locations, ItemClickListener<String> placeIdClickListener,
			DeleteRequestListener<Location> deleteRequestListener) {
		super(locations, R.string.empty_state_locations);
		this.placeIdClickListener = placeIdClickListener;
		this.deleteRequestListener = deleteRequestListener;
	}

	public void updateLocations(List<ListElement> locations) {
		this.elements = locations;
		notifyDataSetChanged();
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
		if (holder instanceof LocationViewHolder) {
			final Location location = getElementsObject(position);
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
					deleteRequestListener.requestDelete(location);
					return true;
				}
			});
		}
		else {
			super.onBindViewHolder(holder, position);
		}
	}
}

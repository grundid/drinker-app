package de.grundid.drinker.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import de.grundid.drinker.R;

import java.util.EnumMap;
import java.util.List;

public class EmptyStateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_EMPTY = 0;
	protected List<Object> elements;

	public EmptyStateAdapter(List<Object> elements, int emptyStateResourceId) {
		this.elements = elements;
		if (this.elements.isEmpty()) {
			this.elements.add(new EmptyElement(emptyStateResourceId));
		}
	}

	@Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_EMPTY) {
			return new EmptyStateViewHolder(
					LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_state, parent, false));
		}
		throw new RuntimeException("Unknown view type: " + viewType);
	}

	@Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof EmptyStateViewHolder) {
			((EmptyStateViewHolder)holder).update((EmptyElement)elements.get(position));
		}
	}

	@Override public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override public int getItemCount() {
		return elements.size();
	}
}

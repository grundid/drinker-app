package de.grundid.drinker.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import de.grundid.drinker.R;

import java.util.List;

public class EmptyStateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	public static final int TYPE_EMPTY = 0;
	protected List<ListElement> elements;

	public EmptyStateAdapter(List<ListElement> elements, int emptyStateResourceId) {
		this.elements = elements;
		if (this.elements.isEmpty()) {
			this.elements.add(new EmptyElement(emptyStateResourceId));
		}
	}

	protected <T> T getElementsObject(int position) {
		return (T)elements.get(position).getElement();
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
		return elements.get(position).getType();
	}

	@Override public int getItemCount() {
		return elements.size();
	}
}

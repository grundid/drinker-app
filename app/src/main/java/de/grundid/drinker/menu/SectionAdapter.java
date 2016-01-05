package de.grundid.drinker.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.grundid.drinker.Category;
import de.grundid.drinker.ItemClickListener;
import de.grundid.drinker.R;
import de.grundid.drinker.utils.EmptyStateAdapter;

import java.util.List;

public class SectionAdapter extends EmptyStateAdapter {

	private static final int TYPE_SECTION = 1;
	private static final int TYPE_FOOTER = 2;
	private final ItemClickListener<Category> itemClickListener;

	public SectionAdapter(List<Object> elements, ItemClickListener<Category> itemClickListener) {
		super(elements, R.string.empty_state_drinksmenu);
		this.itemClickListener = itemClickListener;
	}

	@Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		switch (viewType) {
			case TYPE_SECTION:
				return new SectionViewHolder(
						LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_section, parent, false));
			case TYPE_FOOTER:
				return new FooterViewHolder(
						LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_footer, parent, false));
			default:
				return super.onCreateViewHolder(parent, viewType);
		}
	}

	@Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof SectionViewHolder) {
			final Category category = (Category)elements.get(position);
			((SectionViewHolder)holder).update(category);
			holder.itemView.setOnClickListener(new View.OnClickListener() {

				@Override public void onClick(View v) {
					itemClickListener.onItemClick(category);
				}
			});
		}
		else if (holder instanceof FooterViewHolder) {
			((FooterViewHolder)holder).update((Footer)elements.get(position));
		}
		else {
			super.onBindViewHolder(holder, position);
		}
	}

	@Override public int getItemViewType(int position) {
		Object object = elements.get(position);
		if (object instanceof Category) {
			return TYPE_SECTION;
		}
		else if (object instanceof Footer) {
			return TYPE_FOOTER;
		}
		else {
			return super.getItemViewType(position);
		}
	}
}

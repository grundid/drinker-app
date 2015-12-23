package de.grundid.drinker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StringListAdapter extends BaseAdapter implements Filterable {

	private List<String> elements;
	private List<String> originalElements;
	private LayoutInflater layoutInflater;

	private int itemId;
	private int dropdownId;

	public StringListAdapter(List<String> elements, LayoutInflater layoutInflater, int itemId, int dropdownId) {
		this.elements = elements;
		this.originalElements = elements;
		this.layoutInflater = layoutInflater;
		this.itemId = itemId;
		this.dropdownId = dropdownId;
	}

	@Override public int getCount() {
		return elements.size();
	}

	@Override public Object getItem(int i) {
		return elements.get(i);
	}

	@Override public long getItemId(int i) {
		return 0;
	}

	@Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(dropdownId, parent, false);
		}
		TextView textView = (TextView)convertView.findViewById(android.R.id.text1);
		textView.setText(elements.get(position));
		return convertView;
	}

	@Override public View getView(int i, View view, ViewGroup viewGroup) {
		if (view == null) {
			view = layoutInflater.inflate(itemId, viewGroup, false);
		}
		TextView textView = (TextView)view.findViewById(android.R.id.text1);
		textView.setText(elements.get(i));
		return view;
	}

	@Override public Filter getFilter() {
		return new Filter() {

			@Override protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				if (constraint == null || constraint.length() == 0) {
					// No filter implemented we return all the list
					results.values = originalElements;
					results.count = originalElements.size();
				}
				else {
					String stringToFind = constraint.toString();
					List<String> resultList = new ArrayList<>();
					for (String element : originalElements) {
						if (element.startsWith(stringToFind)) {
							resultList.add(element);
						}
					}
					results.count = resultList.size();
					results.values = resultList;
				}
				return results;
			}

			@Override protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
				elements = (List<String>)filterResults.values;
				notifyDataSetChanged();
			}
		};
	}
}


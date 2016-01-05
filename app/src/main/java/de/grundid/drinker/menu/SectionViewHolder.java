package de.grundid.drinker.menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import de.grundid.drinker.Category;
import de.grundid.drinker.R;

import java.util.HashMap;
import java.util.Map;

public class SectionViewHolder extends RecyclerView.ViewHolder {

	private TextView title;
	private ImageView imageView;
	private static Map<Category, Integer> catToImageMap = new HashMap<>();

	static {
		catToImageMap.put(Category.beer, R.drawable.beer);
		catToImageMap.put(Category.cocktails, R.drawable.cocktail);
		catToImageMap.put(Category.coffee, R.drawable.coffee);
		catToImageMap.put(Category.drink, R.drawable.drinks);
		catToImageMap.put(Category.hot, R.drawable.hot);
		catToImageMap.put(Category.longdrink, R.drawable.longdrink);
		catToImageMap.put(Category.other, R.drawable.others);
		catToImageMap.put(Category.shot, R.drawable.shots);
		catToImageMap.put(Category.softdrink, R.drawable.softdrinks);
		catToImageMap.put(Category.sparkling, R.drawable.sparklingwine);
		catToImageMap.put(Category.tee, R.drawable.tea);
		catToImageMap.put(Category.water, R.drawable.water);
		catToImageMap.put(Category.wine, R.drawable.wine);
	}

	public SectionViewHolder(View itemView) {
		super(itemView);
		title = (TextView)itemView.findViewById(R.id.sectionTitle);
		imageView = (ImageView)itemView.findViewById(R.id.tile_image);
	}

	public void update(Category category) {
		title.setText(category.getLabel());
		imageView.setImageResource(catToImageMap.get(category));

	}
}

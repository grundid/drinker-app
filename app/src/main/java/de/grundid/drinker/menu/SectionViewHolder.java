package de.grundid.drinker.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.grundid.drinker.Category;
import de.grundid.drinker.R;

import java.util.HashMap;
import java.util.Map;

public class SectionViewHolder extends RecyclerView.ViewHolder {

	private TextView title;
	private ImageView imageView;
	private ViewGroup viewGroup;
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
		catToImageMap.put(Category.liqueur, R.drawable.spirituosen);
		catToImageMap.put(Category.cocktails_nonalcoholic, R.drawable.cocktail_without);
		catToImageMap.put(Category.aperitiv, R.drawable.aperitiv);
		catToImageMap.put(Category.juice, R.drawable.schorle);
	}

	public SectionViewHolder(View itemView) {
		super(itemView);
		title = (TextView)itemView.findViewById(R.id.sectionTitle);
		imageView = (ImageView)itemView.findViewById(R.id.tile_image);
		viewGroup = (ViewGroup)itemView.findViewById(R.id.drinkList);
	}

	public void update(CategoryWithDrinksModel categoryWithDrinks, long lastVisit) {
		title.setText(categoryWithDrinks.getCategory().getLabel());
		imageView.setImageResource(catToImageMap.get(categoryWithDrinks.getCategory()));
		viewGroup.removeAllViews();
		LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
		for (MenuDrink menuDrink : categoryWithDrinks.getDrinks()) {
			View view = layoutInflater.inflate(R.layout.menu_drink, viewGroup, false);
			DrinkViewHolder drinkViewHolder = new DrinkViewHolder(view);
			drinkViewHolder.update(menuDrink, lastVisit);
			viewGroup.addView(view);
		}
	}
}

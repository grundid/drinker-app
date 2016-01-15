package de.grundid.drinker.menu;

import de.grundid.drinker.Category;

import java.util.List;

public class CategoryWithDrinksModel {

	private Category category;
	private List<MenuDrink> drinks;

	public CategoryWithDrinksModel(Category category, List<MenuDrink> drinks) {
		this.category = category;
		this.drinks = drinks;
	}

	public Category getCategory() {
		return category;
	}

	public List<MenuDrink> getDrinks() {
		return drinks;
	}
}

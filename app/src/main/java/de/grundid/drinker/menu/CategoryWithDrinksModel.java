package de.grundid.drinker.menu;

import java.util.List;

/**
 * Created by lukas on 14.01.16.
 */
public class CategoryWithDrinksModel {
    private String name;
    private List<MenuDrink> drinks;

    public String getName() {
        return name;
    }

    public List<MenuDrink> getDrinks() {
        return drinks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDrinks(List<MenuDrink> drinks) {
        this.drinks = drinks;
    }

    public CategoryWithDrinksModel(String name, List<MenuDrink> drinks) {
        this.name = name;
        this.drinks = drinks;
    }
}

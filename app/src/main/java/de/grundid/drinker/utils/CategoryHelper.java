package de.grundid.drinker.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.grundid.drinker.Category;
import de.grundid.drinker.StringListAdapter;

public class CategoryHelper {

    private List<String> categories = new ArrayList<String>();
    private Map<String, String> categoriesMap;

    public CategoryHelper(Map<String, String> categoriesMap) {
        this.categoriesMap = categoriesMap;
        for (Map.Entry<String, String> entry : categoriesMap.entrySet()) {
            categories.add(entry.getValue());
        }
    }

    public List<String> getCategories() {
        return categories;
    }

    public int indexOfCategory(String menuDrinkCategory) {
        int counter = 0;
        for (Map.Entry<String, String> entry : categoriesMap.entrySet()) {
            if(menuDrinkCategory.equals(entry.getKey())){
                return counter;
            }
            counter++;
        }
        return -1;
    }

    public String getCategoryKey(String label) {
        for (Map.Entry<String, String> entry : categoriesMap.entrySet()) {
            if(label.equals(entry.getValue())){
                return entry.getKey();
            }
        }
        return null;
    }
}

package de.grundid.drinker.menu;

import org.junit.Test;

import static org.junit.Assert.*;

public class DrinkMenuComparatorTest {

	private DrinkMenuComparator comparator = new DrinkMenuComparator();

	@Test
	public void itShouldCompare() throws Exception {
		assertTrue(comparator.compare(create(null, null, 350, 500), create(null, null, 400, 500)) < 0);
		assertTrue(comparator.compare(create(null, null, 350, 500), create(null, null, 400, null)) < 0);
		assertTrue(comparator.compare(create(null, null, 350, null), create(null, null, 400, 500)) > 0);
		assertTrue(comparator.compare(create("Cola", null, 400, null), create("Fanta", null, 400, null)) < 0);
		assertTrue(comparator.compare(create("Cola", "1", 400, null), create("Cola", "2", 400, null)) < 0);
	}

	private MenuDrink create(String name, String drinkId, Integer price, Integer volume) {
		MenuDrink menuDrink = new MenuDrink();
		menuDrink.setName(name);
		menuDrink.setDrinkId(drinkId);
		menuDrink.setPrice(price);
		menuDrink.setVolume(volume);
		return menuDrink;
	}
}
package de.grundid.drinker.utils;

import de.grundid.drinker.Category;
import de.grundid.drinker.menu.DrinkModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class DrinkModelHelperTest {

	@Test
	public void itShouldParseVolume() throws Exception {
		assertEquals(125, DrinkModelHelper.parseDrinkVolume("125ml").intValue());
		assertEquals(125, DrinkModelHelper.parseDrinkVolume("125").intValue());
		assertEquals(125, DrinkModelHelper.parseDrinkVolume("0,125").intValue());
		assertEquals(125, DrinkModelHelper.parseDrinkVolume("0,125l").intValue());
		assertEquals(125, DrinkModelHelper.parseDrinkVolume("0.125l").intValue());
	}

	@Test
	public void itShouldFailParseVolume() throws Exception {
		assertNull(DrinkModelHelper.parseDrinkVolume("0"));
		assertNull(DrinkModelHelper.parseDrinkVolume("abc"));
		assertNull(DrinkModelHelper.parseDrinkVolume("0,0,0"));
		assertNull(DrinkModelHelper.parseDrinkVolume("0l"));
		assertNull(DrinkModelHelper.parseDrinkVolume("0ml"));
	}

	@Test
	public void itShouldParsePrice() throws Exception {
		assertEquals(350, DrinkModelHelper.parseDrinkPrice("3.5").intValue());
		assertEquals(350, DrinkModelHelper.parseDrinkPrice("3,5").intValue());
	}

	@Test
	public void itShouldFailParsePrice() throws Exception {
		assertNull(DrinkModelHelper.parseDrinkPrice("0"));
		assertNull(DrinkModelHelper.parseDrinkPrice("abc"));
	}

	@Test
	public void isShouldTestValid() throws Exception {
		assertFalse(DrinkModelHelper.isDrinkModelValid(create(null, null, null)));
		assertFalse(DrinkModelHelper.isDrinkModelValid(create("", null, null)));
		assertFalse(DrinkModelHelper.isDrinkModelValid(create("abc", null, null)));
		assertFalse(DrinkModelHelper.isDrinkModelValid(create("abc", Category.softdrink, null)));
		assertTrue(DrinkModelHelper.isDrinkModelValid(create("abc", Category.softdrink, 100)));
	}

	private static DrinkModel create(String name, Category cat, Integer price) {
		DrinkModel drinkModel = new DrinkModel();
		drinkModel.setName(name);
		drinkModel.setCategory(cat.name());
		drinkModel.setPrice(price);
		return drinkModel;
	}
}
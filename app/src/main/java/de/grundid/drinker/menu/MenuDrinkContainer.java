package de.grundid.drinker.menu;


public class MenuDrinkContainer {
    private MenuDrink drink;
    private boolean checked;

    public MenuDrinkContainer(MenuDrink drink, boolean checked){
        this.drink = drink;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public MenuDrink getDrink() {
        return drink;
    }

    public void setDrink(MenuDrink drink) {
        this.drink = drink;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

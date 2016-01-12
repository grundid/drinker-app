package de.grundid.drinker.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.grundid.drinker.Category;
import de.grundid.drinker.EditDrinkActivity;
import de.grundid.drinker.R;
import de.grundid.drinker.utils.EmptyStateAdapter;

public class TemplateDrinkAdapter extends EmptyStateAdapter {

    private static final int TYPE_SECTION = 1;
    private static final int TYPE_DRINK = 2;
    private final TemplateDrinkActivity templateDrinkActivity;


    public TemplateDrinkAdapter(List<Object> elements, TemplateDrinkActivity templateDrinkActivity) {
        super(elements, 0);
        this.templateDrinkActivity = templateDrinkActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DRINK:
                return new DrinkTemplateViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_template, parent, false));
            case TYPE_SECTION:
                return new SectionViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_section, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DrinkTemplateViewHolder) {
            final DrinkTemplateViewHolder drinkViewHolder = (DrinkTemplateViewHolder) holder;
            final MenuDrinkContainer menuDrink = (MenuDrinkContainer) elements.get(position);
            drinkViewHolder.update(menuDrink.getDrink(), menuDrink.isChecked());
            drinkViewHolder.itemView.setOnClickListener(new TemplateAddDrinkListener(templateDrinkActivity, menuDrink, this));
        } else if (holder instanceof SectionViewHolder) {
            ((SectionViewHolder) holder).update((Category) elements.get(position));
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = elements.get(position);
        if (object instanceof Category) {
            return TYPE_SECTION;
        } else if (object instanceof MenuDrinkContainer) {
            return TYPE_DRINK;
        } else {
            return super.getItemViewType(position);
        }
    }

}

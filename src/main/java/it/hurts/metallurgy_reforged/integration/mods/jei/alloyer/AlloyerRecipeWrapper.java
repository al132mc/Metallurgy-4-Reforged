package it.hurts.metallurgy_reforged.integration.mods.jei.alloyer;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import it.hurts.metallurgy_reforged.util.recipe.BlockAlloyerRecipes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/*************************************************
 * Author: Davoleo
 * Date / Hour: 03/12/2018 / 20:27
 * Class: AlloyerRecipeWrapper
 * Project: Metallurgy 4 Reforged
 * Copyright - � - Davoleo - 2018
 **************************************************/

public class AlloyerRecipeWrapper implements IRecipeWrapper {

    private final List<ItemStack> inputs;
    private final ItemStack output;

    public AlloyerRecipeWrapper(List<ItemStack> inputs, ItemStack output)
    {
        this.inputs = inputs;
        this.output = output;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients)
    {
        ingredients.setInputs(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }

    public static List<AlloyerRecipeWrapper> getRecipeInputs()
    {
        ArrayList<AlloyerRecipeWrapper> recipes = new ArrayList<>();

        for(Table.Cell<ItemStack, ItemStack, ItemStack> entry : BlockAlloyerRecipes.getInstance().getRecipeTable().cellSet())
        {
            List<ItemStack> inputs = Lists.newArrayList();
            inputs.add(entry.getColumnKey());
            inputs.add(entry.getRowKey());
            recipes.add(new AlloyerRecipeWrapper(inputs, entry.getValue()));
        }

        return recipes;
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }
}

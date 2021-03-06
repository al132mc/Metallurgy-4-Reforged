package it.hurts.metallurgy_reforged.container.slot;

import it.hurts.metallurgy_reforged.util.Utils;
import it.hurts.metallurgy_reforged.util.recipe.BlockCrusherRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;

/*************************************************
 * Author: Davoleo
 * Date: 10/10/2018
 * Hour: 16.59
 * Project: Metallurgy
 * Copyright - � - Davoleo - 2018
 **************************************************/

public class SlotCrusherOutput extends Slot {

    private final EntityPlayer player;

    public SlotCrusherOutput(EntityPlayer player, IInventory inventory, int index, int xPos, int yPos)
    {
        super(inventory, index, xPos, yPos);
        this.player = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack onTake(EntityPlayer thePlayer, @Nonnull ItemStack stack)
    {
        onCrafting(stack);
        return super.onTake(thePlayer, stack);
    }

    @Override
    protected void onCrafting(ItemStack output)
    {
        if (!player.world.isRemote)
        {
            int i = output.getCount();
            output.onCrafting(player.world, player, i);
            Utils.giveExperience(player, i * BlockCrusherRecipes.getInstance().getCrushingExperience(output));
        }
        FMLCommonHandler.instance().firePlayerSmeltedEvent(player, output);
    }
}
package it.hurts.metallurgy_reforged.container;


import it.hurts.metallurgy_reforged.container.slot.SlotCrusherOutput;
import it.hurts.metallurgy_reforged.tileentity.TileEntityCrusher;
import it.hurts.metallurgy_reforged.util.recipe.BlockCrusherRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/*************************************************
 * Author: Davoleo
 * Date: 03/09/2018
 * Hour: 21.55
 * Project: Metallurgy
 * Copyright - � - Davoleo - 2018
 **************************************************/

public class ContainerCrusher extends Container {

    private final IInventory crusher;
    private int crushTime, totalCrushTime, burnTime, currentBurnTime;

    //Default values for player inventory, edit iStart only
    public static final int iStart = 5, iEnd = iStart + 26, hStart = iEnd + 1, hEnd = hStart + 8;

    public ContainerCrusher(InventoryPlayer playerInv, IInventory crusherInv) {
        this.crusher = crusherInv;
//    	playerInventory, Invenotry, Index, X, Y
        this.addSlotToContainer(new Slot(crusherInv, 0, 61, -4));  //Input
        this.addSlotToContainer(new SlotFurnaceFuel(crusherInv, 1, 129, 48));   //Fuel
        this.addSlotToContainer(new SlotCrusherOutput(playerInv.player, crusherInv, 2, 67, 36)); //Result 1
        this.addSlotToContainer(new SlotCrusherOutput(playerInv.player, crusherInv, 3, 48, 36)); //Result 2
        this.addSlotToContainer(new SlotCrusherOutput(playerInv.player, crusherInv, 4, 29, 36)); //Result 3

//        Collegamento all'inventario del player
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

//        Collegamento all'inventario della hotbar
        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
        }
    }

    //    Errore all'add Listener
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.crusher);
    }

    //    Da qui non so come gestirmi
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {
            if (this.crushTime != this.crusher.getField(2))
                listener.sendWindowProperty(this, 2, this.crusher.getField(2));
            if (this.burnTime != this.crusher.getField(0))
                listener.sendWindowProperty(this, 0, this.crusher.getField(0));
            if (this.currentBurnTime != this.crusher.getField(1))
                listener.sendWindowProperty(this, 1, this.crusher.getField(1));
            if (this.totalCrushTime != this.crusher.getField(3))
                listener.sendWindowProperty(this, 3, this.crusher.getField(3));
        }

        this.crushTime = this.crusher.getField(2);
        this.burnTime = this.crusher.getField(0);
        this.currentBurnTime = this.crusher.getField(1);
        this.totalCrushTime = this.crusher.getField(3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        this.crusher.setField(id, data);
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return this.crusher.isUsableByPlayer(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot = this.inventorySlots.get(index);
        ItemStack ret = slot.getStack();

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            ret = itemstack.copy();

            if (TileEntityCrusher.SlotEnum.OUTPUT_SLOT.contains(index)) {
                slot.onTake(playerIn, itemstack); //XP
                if (!this.mergeItemStack(itemstack, iStart, hEnd + 1, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack, ret);
            } else if (index >= iStart) {
                if (!BlockCrusherRecipes.getInstance().getCrushingResult(itemstack).isEmpty()) {
                    if (!this.mergeItemStack(itemstack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (TileEntityCrusher.isItemFuel(itemstack)) {
                    if (!this.mergeItemStack(itemstack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= iStart && index <= iEnd) {
                    if (!this.mergeItemStack(itemstack, hStart, hEnd + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= hStart && index < hEnd + 1 && !this.mergeItemStack(itemstack, iStart, iEnd + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack, iStart, hEnd + 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack.getCount() == ret.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack);
        }
        return ret;
    }

}
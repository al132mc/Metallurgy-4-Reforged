package it.hurts.metallurgy_reforged.item;

import it.hurts.metallurgy_reforged.Metallurgy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;

/***************************
*
* Author : ItHurtsLikeHell
* Project: Metallurgy-5
* Date   : 28 ago 2018
* Time   : 18:24:07
*
***************************/

public class ItemBase extends Item{

	protected String name;

	public ItemBase(String name)
    {
		this.name = name;
		setTranslationKey(name);
		setRegistryName(name);
		ModItems.itemList.add(this);
	}
	
	public void registerItemModel()
    {
		Metallurgy.proxy.registerItemRenderer(this, 0, name);
	}

	public void registerItemModel(String subdirectory)
	{
		Metallurgy.proxy.registerItemRenderer(this, 0, name, subdirectory);
	}

	@Nonnull
	@Override
	public ItemBase setCreativeTab(@Nonnull CreativeTabs tab)
    {
		super.setCreativeTab(tab);
		return this;
	}
	
}

package it.hurts.metallurgy_reforged.block;

import it.hurts.metallurgy_reforged.material.IOreDict;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.world.Explosion;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***************************
*
* Author : ItHurtsLikeHell
* Project: Metallurgy-5
* Date   : 28 ago 2018
* Time   : 18:24:07
*
***************************/

public class BlockOreDict extends BlockBase implements IOreDict {

	private String oreName;
	private List<Item> customDrop = new ArrayList<Item>();
	private Item itemDrop;

	public BlockOreDict(String name, String oreName) {
		this(name, oreName, null,"p", 1, 5F);
		setHardness(3F);
	}

//	toolClass is the classification of tool (Like showel, axe, pickaxe, hoe)
	public BlockOreDict(String name, String oreName, String toolClass, int harvestLevel, float blastResistance){
		this(name, oreName, null, toolClass, harvestLevel, blastResistance);
    }

	public BlockOreDict(String name, String oreName, Item drop, String toolClass, int harvestLevel, float blastResistance){
		super(Material.ROCK, name);
		this.oreName = oreName;
		this.customDrop.add(drop);
		setHardness(3f);
		setResistance(blastResistance);
		this.setHarvestLevel(toolClass, harvestLevel);
	}
	
	public BlockOreDict(String name, String oreName, String toolClass, int harvestLevel, float blastResistance, Item...drop){
		super(Material.ROCK, name);
		this.oreName = oreName;
		for(Item i : drop)
			customDrop.add(i);
		setHardness(3f);
		setResistance(blastResistance);
		this.setHarvestLevel(toolClass, harvestLevel);
	}


	@Nonnull
	@Override
	public BlockOreDict setCreativeTab(@Nonnull CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}

	@Override
	public void initOreDict()
	{
		OreDictionary.registerOre(oreName, this);
	}

	@Nonnull
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
		
//		Check if the drop is an item, else return the same block
        if(!customDrop.isEmpty())
        	if(customDrop.size() == 1) {
        		if(itemDrop != null)
        			customDrop.add(itemDrop);
        		return customDrop.get(0);
        	} else {
        		if(canDrop(customDrop.get(0), 25, "bitumen")) {
        			itemDrop = customDrop.get(0);
            		customDrop.remove(0);
            		getItemDropped(state, rand, fortune);
            		return itemDrop;
        		}else
        			return customDrop.get(1);
        	}
        else
        	return Item.getItemFromBlock(this);
    }

	private boolean canDrop(Item item, int percentance, String name) {
		if(item.getRegistryName().toString().equals("metallurgy:" + name))
			if((int) (Math.random() * 100) < percentance)
				return true;
			else
				return false;
		else
			return true;
	}
	
    @Override
    public int quantityDropped(Random random)
    {
    	if(customDrop != null)
    		return 1 + random.nextInt(4);
    	else
    		return 1;
    }

	@Override
	public boolean canDropFromExplosion(Explosion explosionIn)
	{
		return this.getTranslationKey().contains("_ore");
	}
}

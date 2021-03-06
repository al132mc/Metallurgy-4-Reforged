package it.hurts.metallurgy_reforged.integration.mods.tic.trait;

import javax.annotation.Nullable;

import it.hurts.metallurgy_reforged.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import scala.util.Random;
import slimeknights.tconstruct.library.traits.AbstractTrait;

/***************************
 *
 * Author : ItHurtsLikeHell
 * Project: Metallurgy-4-Reforged
 * Date   : 25 gen 2019
 * Time   : 14:04:01
 *
 ***************************/
public class TraitLifeSteal extends AbstractTrait implements ITrait{
	
	public TraitLifeSteal() {
		super("life_steal_trait", TextFormatting.LIGHT_PURPLE);
	}

	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit)
	{
		if(player instanceof EntityPlayer) {
			EntityPlayer pl = (EntityPlayer) player; 
			if(pl.getHealth() < pl.getMaxHealth())
			{

				int luck_level = Math.round(pl.getLuck());
				//percentage to get healed based on the luck of the player (example: luck 0 = 15%,luck 1 = 20%...)
				int percentage = 15 + (luck_level * 5);
				if(new Random().nextInt(100) < percentage)
				{
					//the heal Amount ,that is the 10% of the damage
					float healAmount = damageDealt * 0.15F;
					if(pl.getHealth() + healAmount < pl.getMaxHealth()) {
						//set the player health
						pl.setHealth(pl.getHealth() + healAmount);
//						TODO Aggiungere lo spawn di particelle
					}
				}
			}
		}
	}
	
	@Override
    public void register(String name, @Nullable String tooltip){
        Utils.localize(String.format(LOC_Name, name));
        if (tooltip != null)
            Utils.localize(String.format(LOC_Name, tooltip));
    }
}

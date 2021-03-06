package it.hurts.metallurgy_reforged.integration.mods.tic;

import it.hurts.metallurgy_reforged.fluid.ModFluids;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.TinkerRegistry;

/***************************
 *
 * Author : ItHurtsLikeHell
 * Project: Metallurgy-4-Reforged
 * Date   : 15 gen 2019
 * Time   : 22:33:52
 *
 ***************************/
public class MetallurgyTinkerFuels {

	public static void init() {
		TinkerRegistry.registerSmelteryFuel(new FluidStack(ModFluids.THERMITE, 50), 100);
	}

}

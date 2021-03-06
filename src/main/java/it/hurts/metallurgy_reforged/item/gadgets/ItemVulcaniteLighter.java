package it.hurts.metallurgy_reforged.item.gadgets;

import it.hurts.metallurgy_reforged.util.MetallurgyTabs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/*************************************************
 * Author: Davoleo
 * Date / Hour: 28/12/2018 / 21:58
 * Class: ItemVulcaniteLighter
 * Project: Metallurgy 4 Reforged
 * Copyright - � - Davoleo - 2018
 **************************************************/

public class ItemVulcaniteLighter extends ItemIgnatiusLighter {

    public ItemVulcaniteLighter(String name)
    {
        super(name);
        setMaxDamage(500);
        setCreativeTab(MetallurgyTabs.tabSpecial);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        BlockPos blockPos = pos.offset(facing);
        ItemStack lighter = player.getHeldItem(hand);

        if(!player.isSneaking())
            if (!player.canPlayerEdit(blockPos, facing, lighter))
            {
                return EnumActionResult.FAIL;
            }
            else
            {
                worldIn.playSound(player, blockPos, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.BLOCKS, 1.0F, 2F);

                createFire(worldIn, blockPos, player);

                if (!player.isCreative())
                    lighter.damageItem(1, player);

                return EnumActionResult.SUCCESS;
            }
        else
        {
            BlockPos targetPos = blockPos.offset(facing);
            ItemStack item = player.getHeldItem(hand);

            if (player.canPlayerEdit(targetPos, facing, item) && worldIn.mayPlace(Blocks.LAVA, targetPos, false, facing, player))
            {
                worldIn.playSound(player, targetPos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
                IBlockState state = Blocks.LAVA.getStateForPlacement(worldIn, targetPos, facing, hitX, hitY, hitZ, 0, player, hand);
                worldIn.setBlockState(targetPos, state);

                if (!player.isCreative())
                    player.getCooldownTracker().setCooldown(this, 120/*0*/);
                    lighter.damageItem(25, player);

                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.FAIL;

        }
    }

    @Override
    public void registerItemModel()
    {
        super.registerItemModel("gadget");
    }
}

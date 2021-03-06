package it.hurts.metallurgy_reforged.block;

import it.hurts.metallurgy_reforged.util.MetallurgyTabs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*************************************************
 * Author: Davoleo
 * Date: 01/09/2018
 * Hour: 21.28
 * Project: Metallurgy
 * Copyright - � - Davoleo - 2018
 **************************************************/

public abstract class BlockTileEntity<TE extends TileEntity> extends BlockBase {

    public BlockTileEntity(Material material, String name)
    {
        super(material, name);
        setCreativeTab(MetallurgyTabs.tabSpecial);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 1);
        setHardness(6F);
        setResistance(8F);
    }

    public abstract Class<TE> getTileEntityClass();

	@SuppressWarnings("unchecked")
	public TE getTileEntity(IBlockAccess world, BlockPos pos)
    {
        return (TE)world.getTileEntity(pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Nullable
    @Override
    public abstract TE createTileEntity(@Nonnull World world, @Nonnull IBlockState state);

}

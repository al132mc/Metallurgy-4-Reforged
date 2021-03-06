package it.hurts.metallurgy_reforged.block;

import it.hurts.metallurgy_reforged.Metallurgy;
import it.hurts.metallurgy_reforged.gui.GuiHandler;
import it.hurts.metallurgy_reforged.tileentity.TileEntityCrusher;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

/*************************************************
 * Author: Davoleo
 * Date: 01/09/2018
 * Hour: 21.41
 * Project: Metallurgy
 * Copyright - � - Davoleo - 2018
 **************************************************/

//VOGLIO MORIRE EDITION
public class BlockCrusher extends BlockTileEntity<TileEntityCrusher> {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool BURNING = PropertyBool.create("burning");
    private static boolean keepInventory;


    public BlockCrusher(String name){
        super(Material.IRON, name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(BURNING, false));
    }


    @Nonnull
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.crusher);
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(ModBlocks.crusher);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);

            if(te instanceof TileEntityCrusher)
            {
                player.openGui(Metallurgy.instance, GuiHandler.CRUSHER, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    //sistema la rotazione del blocco appena prima di essere piazzato
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            IBlockState north = worldIn.getBlockState(pos.north());
            IBlockState south = worldIn.getBlockState(pos.south());
            IBlockState west = worldIn.getBlockState(pos.west());
            IBlockState east = worldIn.getBlockState(pos.east());
            EnumFacing face = state.getValue(FACING);

            if (face == EnumFacing.NORTH && north.isFullBlock() && !south.isFullBlock())
                face = EnumFacing.SOUTH;
            else if (face == EnumFacing.SOUTH && south.isFullBlock() && !north.isFullBlock())
                face = EnumFacing.NORTH;
            else if (face == EnumFacing.WEST && west.isFullBlock() && !east.isFullBlock())
                face = EnumFacing.EAST;
            else if (face == EnumFacing.EAST && east.isFullBlock() && !west.isFullBlock())
                face = EnumFacing.WEST;

            worldIn.setBlockState(pos, state.withProperty(FACING, face), 2);
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state.getValue(BURNING)){
            return 8;
        } else if (!state.getValue(BURNING)){
            return 0;
        } else
            return 0;
    }

    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos);
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        keepInventory = true;

        if(active)
            worldIn.setBlockState(pos, ModBlocks.crusher.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(BURNING, true), 3);
        else
            worldIn.setBlockState(pos, ModBlocks.crusher.getDefaultState().withProperty(FACING, state.getValue(FACING)).withProperty(BURNING, false), 3);

        keepInventory = false;

        if(tileEntity != null)
        {
            tileEntity.validate();
            worldIn.setTileEntity(pos, tileEntity);
        }
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state)
    {
        if(!keepInventory)
        {
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof TileEntityCrusher)
            {
                InventoryHelper.dropInventoryItems(world, pos, (TileEntityCrusher)te);
            }
        }
    }


    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public Class<TileEntityCrusher> getTileEntityClass()
    {
        return TileEntityCrusher.class;
    }

    @Override
    public TileEntityCrusher createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
    {
        return new TileEntityCrusher();
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, @Nonnull EnumHand hand)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName())
        {
            TileEntity te = worldIn.getTileEntity(pos);

            if(te instanceof TileEntityCrusher)
            {
                ((TileEntityCrusher)te).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Nonnull
    @Override
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Nonnull
    @Override
    public IBlockState withMirror(@Nonnull IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BURNING, FACING);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(BURNING, (meta & 4) != 0);
    }  
    
    private static EnumFacing getFacing(int meta)
    {
        switch (meta & 3)
        {
            case 0:
                return EnumFacing.NORTH;
            case 1:
                return EnumFacing.SOUTH;
            case 2:
                return EnumFacing.WEST;
            case 3:
            default:
                return EnumFacing.EAST;
        }
    }

    private static int getMetaForFacing(EnumFacing facing)
    {
        switch (facing)
        {
            case NORTH:
                return 0;
            case SOUTH:
                return 1;
            case WEST:
                return 2;
            case EAST:
            default:
                return 3;
        }
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | getMetaForFacing(state.getValue(FACING));

        if ((state.getValue(BURNING)))
        {
            i |= 4;
        }

        return i;
    }


    @Nonnull
    @Override
    public BlockCrusher setCreativeTab(@Nonnull CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}

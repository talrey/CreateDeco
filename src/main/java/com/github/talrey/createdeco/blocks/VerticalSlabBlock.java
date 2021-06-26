package com.github.talrey.createdeco.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class VerticalSlabBlock extends SlabBlock {
  private static final VoxelShape NORTH = Block.makeCuboidShape(
  0d, 0d, 0d,
  16d, 16d, 8d
  );
  private static final VoxelShape SOUTH = Block.makeCuboidShape(
    0d, 0d, 8d,
    16d, 16d, 16d
  );
  private static final VoxelShape EAST = Block.makeCuboidShape(
    8d, 0d, 0d,
    16d, 16d, 16d
  );
  private static final VoxelShape WEST = Block.makeCuboidShape(
    0d, 0d, 0d,
    8d, 16d, 16d
  );

  public VerticalSlabBlock(Properties props) {
    super(props);
    this.setDefaultState(this.getStateContainer().getBaseState()
      .with(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
      .with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
      .with(BlockStateProperties.WATERLOGGED, false)
    //  .with(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
    );
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockItemUseContext ctx) {
    BlockState state = ctx.getWorld().getBlockState(ctx.getPos());
    if (state.isIn(this)) {
      return state.with(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE).with(BlockStateProperties.WATERLOGGED, false);
    }
    else {
      FluidState fluid = ctx.getWorld().getFluidState(ctx.getPos());
      return getDefaultState()
        .with(BlockStateProperties.HORIZONTAL_FACING, ctx.getPlacementHorizontalFacing())
        .with(BlockStateProperties.WATERLOGGED, fluid.getFluid() == Fluids.WATER);
    }
  }

  @Override
  protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
  }

  @Override
  public boolean isReplaceable(BlockState state, BlockItemUseContext ctx) {
    if ((state.get(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE) && (ctx.getItem().getItem() == this.asItem())) {
      return (ctx.replacingClickedOnBlock() && ctx.getFace().getOpposite().equals(state.get(BlockStateProperties.HORIZONTAL_FACING)));
    }
    return false;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
    if (state.get(BlockStateProperties.SLAB_TYPE).equals(SlabType.DOUBLE)) return VoxelShapes.fullCube();
    switch (state.get(BlockStateProperties.HORIZONTAL_FACING)) {
      case NORTH: return NORTH;
      case SOUTH: return SOUTH;
      case EAST:  return EAST;
      case WEST:  return WEST;
    }
    return super.getShape(state, reader, pos, ctx);
  }
}

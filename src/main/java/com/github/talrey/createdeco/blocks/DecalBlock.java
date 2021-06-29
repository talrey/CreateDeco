package com.github.talrey.createdeco.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class DecalBlock extends Block {
  private static final VoxelShape NORTH = Block.makeCuboidShape(
  0d, 0d, 0d,
  16d, 16d, 1d
  );
  private static final VoxelShape SOUTH = Block.makeCuboidShape(
  0d, 0d, 15d,
  16d, 16d, 16d
  );
  private static final VoxelShape EAST = Block.makeCuboidShape(
  15d, 0d, 0d,
  16d, 16d, 16d
  );
  private static final VoxelShape WEST = Block.makeCuboidShape(
  0d, 0d, 0d,
  1d, 16d, 16d
  );

  public DecalBlock(Properties props) {
    super(props);
    this.setDefaultState(this.getStateContainer().getBaseState()
      .with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
      .with(BlockStateProperties.WATERLOGGED, false)
    );
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockItemUseContext ctx) {
    FluidState fluid = ctx.getWorld().getFluidState(ctx.getPos());
    return getDefaultState()
      .with(BlockStateProperties.HORIZONTAL_FACING, ctx.getPlacementHorizontalFacing())
      .with(BlockStateProperties.WATERLOGGED, fluid.getFluid() == Fluids.WATER);
  }

  @Override
  protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
    builder.add(BlockStateProperties.WATERLOGGED);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
    switch (state.get(BlockStateProperties.HORIZONTAL_FACING)) {
      case NORTH: return NORTH;
      case SOUTH: return SOUTH;
      case EAST:  return EAST;
      case WEST:  return WEST;
    }
    return NORTH;
  }
}

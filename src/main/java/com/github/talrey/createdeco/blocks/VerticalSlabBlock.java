package com.github.talrey.createdeco.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
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
  private static final VoxelShape NORTH = Block.box(
  0d, 0d, 0d,
  16d, 16d, 8d
  );
  private static final VoxelShape SOUTH = Block.box(
    0d, 0d, 8d,
    16d, 16d, 16d
  );
  private static final VoxelShape EAST = Block.box(
    8d, 0d, 0d,
    16d, 16d, 16d
  );
  private static final VoxelShape WEST = Block.box(
    0d, 0d, 0d,
    8d, 16d, 16d
  );

  public VerticalSlabBlock(Properties props) {
    super(props);
    this.registerDefaultState(this.defaultBlockState()
      .setValue(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
      .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
      .setValue(BlockStateProperties.WATERLOGGED, false)
    );
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockItemUseContext ctx) {
    BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
    if (state.is(this)) {
      return state.setValue(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE).setValue(BlockStateProperties.WATERLOGGED, false);
    }
    else {
      FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
      return defaultBlockState()
        .setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection())
        .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
    }
  }

  @Override
  protected void createBlockStateDefinition (StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
  }


  @Override
  public boolean canBeReplaced (BlockState state, BlockItemUseContext ctx) {
    if ((state.getValue(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE) && (ctx.getItemInHand().getItem() == this.asItem())) {
      return (ctx.replacingClickedOnBlock() && ctx.getClickedFace().getOpposite().equals(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }
    return false;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
    if (state.getValue(BlockStateProperties.SLAB_TYPE).equals(SlabType.DOUBLE)) return VoxelShapes.block();
    switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
      case NORTH: return NORTH;
      case SOUTH: return SOUTH;
      case EAST:  return EAST;
      case WEST:  return WEST;
    }
    return super.getShape(state, reader, pos, ctx);
  }
}

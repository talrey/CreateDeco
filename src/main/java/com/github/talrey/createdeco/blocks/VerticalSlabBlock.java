package com.github.talrey.createdeco.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
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
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
  }


  @Override
  public boolean canBeReplaced (BlockState state, BlockPlaceContext ctx) {
    if ((state.getValue(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE) && (ctx.getItemInHand().getItem() == this.asItem())) {
      return (ctx.replacingClickedOnBlock() && ctx.getClickedFace().getOpposite().equals(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }
    return false;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    if (state.getValue(BlockStateProperties.SLAB_TYPE).equals(SlabType.DOUBLE)) return Shapes.block();
    switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
      case NORTH: return NORTH;
      case SOUTH: return SOUTH;
      case EAST:  return EAST;
      case WEST:  return WEST;
    }
    return super.getShape(state, reader, pos, ctx);
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rot) {
    Direction dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
    Direction result;
    switch (rot) {
      case CLOCKWISE_90 -> result = dir.getClockWise(Direction.Axis.Y);
      case COUNTERCLOCKWISE_90 -> result = dir.getCounterClockWise(Direction.Axis.Y);
      case CLOCKWISE_180 -> result = dir.getOpposite();
      default -> result = dir;
    }
    return state.setValue(BlockStateProperties.HORIZONTAL_FACING, result);
  }
}

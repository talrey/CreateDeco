package com.github.talrey.createdeco.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class DecalBlock extends Block implements SimpleWaterloggedBlock {
  private static final VoxelShape NORTH = Block.box(
  1d, 1d, 0d,
  15d, 15d, 1d
  );
  private static final VoxelShape SOUTH = Block.box(
  1d, 1d, 15d,
  15d, 15d, 16d
  );
  private static final VoxelShape EAST = Block.box(
  15d, 1d, 1d,
  16d, 15d, 15d
  );
  private static final VoxelShape WEST = Block.box(
  0d, 1d, 1d,
  1d, 15d, 15d
  );

  public DecalBlock(Properties props) {
    super(props);
    this.registerDefaultState(this.defaultBlockState()
      .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
      .setValue(BlockStateProperties.WATERLOGGED, false)
    );
  }

  public static boolean canSupportDecal (LevelAccessor world, BlockPos pos, Direction side) {
    return canSupportDecal(world, world.getBlockState(pos), pos, side);
  }

  private static boolean canSupportDecal (LevelAccessor world, BlockState state, BlockPos pos, Direction side) {
    return state.isFaceSturdy(world, pos, side, SupportType.CENTER);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    BlockPos neighbor = ctx.getClickedPos().offset(ctx.getHorizontalDirection().getNormal());
    if (!canSupportDecal(ctx.getLevel(), neighbor, ctx.getHorizontalDirection().getOpposite())) return null;

    FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
    return defaultBlockState()
      .setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection())
      .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
  }

  @Override
  public BlockState updateShape (BlockState state, Direction dir, BlockState neighbor, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
    if (!dir.equals(state.getValue(BlockStateProperties.HORIZONTAL_FACING))) return state;
    return neighbor.isFaceSturdy(world, neighborPos, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite(), SupportType.CENTER)
      ? super.updateShape(state, dir, neighbor, world, pos, neighborPos)
      : Blocks.AIR.defaultBlockState();
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
    builder.add(BlockStateProperties.WATERLOGGED);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
      case NORTH: return NORTH;
      case SOUTH: return SOUTH;
      case EAST:  return EAST;
      case WEST:  return WEST;
    }
    return NORTH;
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    return Shapes.empty();
  }
  @Override
  public boolean canPlaceLiquid (BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
    return !state.getValue(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
  }
}

package com.github.talrey.createdeco.blocks;

import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class DecalBlock extends Block implements ProperWaterloggedBlock {
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
      .setValue(WATERLOGGED, false)
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
    return withWater(defaultBlockState()
        .setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection()),
      ctx
    );
  }

  @Override
  public BlockState updateShape (BlockState state, Direction dir, BlockState neighbor, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
    updateWater(world, state, pos);
    if (!dir.equals(state.getValue(BlockStateProperties.HORIZONTAL_FACING))) return state;
    return neighbor.isFaceSturdy(world, neighborPos, state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite(), SupportType.CENTER)
      ? super.updateShape(state, dir, neighbor, world, pos, neighborPos)
      : Blocks.AIR.defaultBlockState();
  }

  @Override
  public FluidState getFluidState (BlockState pState) {
    return fluidState(pState);
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.HORIZONTAL_FACING, WATERLOGGED);
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
}

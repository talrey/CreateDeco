package com.github.talrey.createdeco.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockVoxelShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class DecalBlock extends Block {
  private static final VoxelShape NORTH = Block.makeCuboidShape(
  1d, 1d, 0d,
  15d, 15d, 1d
  );
  private static final VoxelShape SOUTH = Block.makeCuboidShape(
  1d, 1d, 15d,
  15d, 15d, 16d
  );
  private static final VoxelShape EAST = Block.makeCuboidShape(
  15d, 1d, 1d,
  16d, 15d, 15d
  );
  private static final VoxelShape WEST = Block.makeCuboidShape(
  0d, 1d, 1d,
  1d, 15d, 15d
  );

  public DecalBlock(Properties props) {
    super(props);
    this.setDefaultState(this.getStateContainer().getBaseState()
      .with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
      .with(BlockStateProperties.WATERLOGGED, false)
    );
  }

  private boolean canSupportDecal (IWorld world, BlockPos pos, Direction side) {
    return canSupportDecal(world, world.getBlockState(pos), pos, side);
  }

  private boolean canSupportDecal (IWorld world, BlockState state, BlockPos pos, Direction side) {
    return state.isSideSolid(world, pos, side, BlockVoxelShape.CENTER);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockItemUseContext ctx) {
    BlockPos neighbor = ctx.getPos().offset(ctx.getPlacementHorizontalFacing());
    if (!canSupportDecal(ctx.getWorld(), neighbor, ctx.getPlacementHorizontalFacing().getOpposite())) return null;

    FluidState fluid = ctx.getWorld().getFluidState(ctx.getPos());
    return getDefaultState()
      .with(BlockStateProperties.HORIZONTAL_FACING, ctx.getPlacementHorizontalFacing())
      .with(BlockStateProperties.WATERLOGGED, fluid.getFluid() == Fluids.WATER);
  }

  @Override
  public BlockState updatePostPlacement(BlockState state, Direction dir, BlockState neighbor, IWorld world, BlockPos pos, BlockPos neighborPos) {
    if (!dir.equals(state.get(BlockStateProperties.HORIZONTAL_FACING))) return state;
    return neighbor.isSideSolid(world, neighborPos, state.get(BlockStateProperties.HORIZONTAL_FACING).getOpposite(), BlockVoxelShape.CENTER)
      ? super.updatePostPlacement(state, dir, neighbor, world, pos, neighborPos)
      : Blocks.AIR.getDefaultState();
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

  @Override
  public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
    return VoxelShapes.empty();
  }
}

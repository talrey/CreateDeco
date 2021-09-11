package com.github.talrey.createdeco.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CatwalkBlock extends Block {
  private static final VoxelShape VOXEL_BOTTOM = Block.makeCuboidShape(
    0d, 0d, 0d,
    16d, 2d, 16d
  );
  private static final VoxelShape VOXEL_NORTH = Block.makeCuboidShape(
  0d, 0d, 0d,
  16d, 16d, 2d
  );
  private static final VoxelShape VOXEL_SOUTH = Block.makeCuboidShape(
  0d, 0d, 14d,
  16d, 16d, 16d
  );
  private static final VoxelShape VOXEL_EAST = Block.makeCuboidShape(
  14d, 0d, 0d,
  16d, 16d, 16d
  );
  private static final VoxelShape VOXEL_WEST = Block.makeCuboidShape(
  0d, 0d, 0d,
  2d, 16d, 16d
  );

  private static final BooleanProperty NORTH_FENCE = BlockStateProperties.NORTH;
  private static final BooleanProperty SOUTH_FENCE = BlockStateProperties.SOUTH;
  private static final BooleanProperty EAST_FENCE  = BlockStateProperties.EAST;
  private static final BooleanProperty WEST_FENCE  = BlockStateProperties.WEST;
  private static final BooleanProperty LIFTED      = BlockStateProperties.BOTTOM;

  private static boolean hasNeighborTo (Direction side, BlockItemUseContext ctx) {
    return ctx.getWorld().getBlockState(ctx.getPos().offset(side)).getBlock() instanceof CatwalkBlock;
  }

  public CatwalkBlock (Properties props) {
    super(props);
    this.setDefaultState(this.getStateContainer().getBaseState()
      .with(NORTH_FENCE, false)
      .with(SOUTH_FENCE, false)
      .with(EAST_FENCE,  false)
      .with(WEST_FENCE,  false)
      .with(LIFTED,      true)
      .with(BlockStateProperties.WATERLOGGED, false)
    );
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
    VoxelShape shape = VOXEL_BOTTOM;

    if (state.get(NORTH_FENCE)) shape = VoxelShapes.combine(shape, VOXEL_NORTH, IBooleanFunction.OR);
    if (state.get(SOUTH_FENCE)) shape = VoxelShapes.combine(shape, VOXEL_SOUTH, IBooleanFunction.OR);
    if (state.get(EAST_FENCE))  shape = VoxelShapes.combine(shape, VOXEL_EAST,  IBooleanFunction.OR);
    if (state.get(WEST_FENCE))  shape = VoxelShapes.combine(shape, VOXEL_WEST,  IBooleanFunction.OR);

    return shape.withOffset(0d, state.get(BlockStateProperties.BOTTOM) ? 0d : -2/16d, 0d);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockItemUseContext ctx) {
    BlockState state = getDefaultState();
    Direction facing = ctx.getPlacementHorizontalFacing();
    FluidState fluid = ctx.getWorld().getFluidState(ctx.getPos());

    return state
      .with(LIFTED, (ctx.getHitVec().y - ctx.getPos().getY() < 0.5f))
      .with(NORTH_FENCE, (facing == Direction.NORTH) && !hasNeighborTo(Direction.NORTH, ctx))
      .with(SOUTH_FENCE, (facing == Direction.SOUTH) && !hasNeighborTo(Direction.SOUTH, ctx))
      .with(EAST_FENCE,  (facing == Direction.EAST)  && !hasNeighborTo(Direction.EAST,  ctx))
      .with(WEST_FENCE,  (facing == Direction.WEST)  && !hasNeighborTo(Direction.WEST,  ctx))
      .with(BlockStateProperties.WATERLOGGED, fluid.getFluid() == Fluids.WATER);
  }

  @Override
  protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(NORTH_FENCE);
    builder.add(SOUTH_FENCE);
    builder.add(EAST_FENCE);
    builder.add(WEST_FENCE);
    builder.add(LIFTED);
    builder.add(BlockStateProperties.WATERLOGGED);
  }
}

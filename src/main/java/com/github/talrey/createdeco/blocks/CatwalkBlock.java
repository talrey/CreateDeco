package com.github.talrey.createdeco.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CatwalkBlock extends Block {
  private static final VoxelShape BOTTOM = Block.makeCuboidShape(
    0d, 0d, 0d,
    16d, 2d, 16d
  );
  private static final VoxelShape NORTH = Block.makeCuboidShape(
  0d, 0d, 0d,
  16d, 16d, 2d
  );
  private static final VoxelShape SOUTH = Block.makeCuboidShape(
  0d, 0d, 14d,
  16d, 16d, 16d
  );
  private static final VoxelShape EAST = Block.makeCuboidShape(
  14d, 0d, 0d,
  16d, 16d, 16d
  );
  private static final VoxelShape WEST = Block.makeCuboidShape(
  0d, 0d, 0d,
  2d, 16d, 16d
  );

  public CatwalkBlock (Properties props) {
    super(props);
    this.setDefaultState(this.getStateContainer().getBaseState()
      .with(BlockStateProperties.NORTH, false)
      .with(BlockStateProperties.SOUTH, false)
      .with(BlockStateProperties.EAST, false)
      .with(BlockStateProperties.WEST, false)
      .with(BlockStateProperties.BOTTOM, false)
    );
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
    VoxelShape shape = BOTTOM;

    if (state.get(BlockStateProperties.NORTH)) shape = VoxelShapes.combine(shape, NORTH, IBooleanFunction.OR);
    if (state.get(BlockStateProperties.SOUTH)) shape = VoxelShapes.combine(shape, SOUTH, IBooleanFunction.OR);
    if (state.get(BlockStateProperties.EAST))  shape = VoxelShapes.combine(shape, EAST,  IBooleanFunction.OR);
    if (state.get(BlockStateProperties.WEST))  shape = VoxelShapes.combine(shape, WEST,  IBooleanFunction.OR);

    return shape.withOffset(0d, state.get(BlockStateProperties.BOTTOM) ? 0d : -2/16d, 0d);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockItemUseContext ctx) {
    BlockState state = getDefaultState();
    Direction facing = ctx.getPlacementHorizontalFacing();

    if (ctx.getHitVec().y - ctx.getPos().getY() < 0.5f) {
      state = state.with(BlockStateProperties.BOTTOM, true)
        .with(BlockStateProperties.NORTH, (facing == Direction.NORTH) )
        .with(BlockStateProperties.SOUTH, (facing == Direction.SOUTH) )
        .with(BlockStateProperties.EAST,  (facing == Direction.EAST)  )
        .with(BlockStateProperties.WEST,  (facing == Direction.WEST)  );
    }

    return state;
  }

  @Override
  protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(BlockStateProperties.NORTH);
    builder.add(BlockStateProperties.SOUTH);
    builder.add(BlockStateProperties.EAST);
    builder.add(BlockStateProperties.WEST);
    builder.add(BlockStateProperties.BOTTOM);
  }
}

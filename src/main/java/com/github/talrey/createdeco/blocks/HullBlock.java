package com.github.talrey.createdeco.blocks;

import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HullBlock extends HorizontalDirectionalBlock {
  private static final VoxelShape NORTH = Block.box(
    0d, 0d, 0d,
    16d, 16d, 2d
  );
  private static final VoxelShape SOUTH = Block.box(
    0d, 0d, 14d,
    16d, 16d, 16d
  );
  private static final VoxelShape EAST = Block.box(
    0d, 0d, 0d,
    2d, 16d, 16d
  );
  private static final VoxelShape WEST = Block.box(
    14d, 0d, 0d,
    16d, 16d, 16d
  );
  private static final VoxelShape UP = Block.box(
    0d, 14d, 0d,
    16d, 16d, 16d
  );
  private static final VoxelShape DOWN = Block.box(
    0d, 0d, 0d,
    16d, 2d, 16d
  );
  private static final VoxelShape CUBE = // FFS if this works...
    Shapes.join(
      Shapes.join(UP, DOWN, BooleanOp.OR),
      Shapes.join(
        Shapes.join(EAST, WEST, BooleanOp.OR),
        Shapes.join(NORTH, SOUTH, BooleanOp.OR),
        BooleanOp.OR),
      BooleanOp.OR
  );

  public HullBlock (Properties props) {
    super(props);
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    BlockState result = super.getStateForPlacement(ctx).setValue(FACING,
      ctx.getHorizontalDirection()
    );
    for (Direction side : Iterate.horizontalDirections) {
      BlockState neighbor = ctx.getLevel()
        .getBlockState(ctx.getClickedPos()
          .relative(side));
      if (neighbor.getBlock() instanceof HullBlock) {
        result = result.setValue(FACING, neighbor.getValue(FACING));
      }
    }
    return result;
  }

  @Override
  public VoxelShape getShape (BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    return CUBE;
  }
}

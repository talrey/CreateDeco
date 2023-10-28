package com.github.talrey.createdeco.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HullBlock extends DirectionalBlock {
  private static final VoxelShape OUTER = Block.box(
    0d, 0d, 0d,
    16d, 16d, 16d
  );
  private static final VoxelShape INNER = Block.box(
    2d, 2d, 2d,
    14d, 14d, 14d
  );
  private static final VoxelShape CUBE =
    Shapes.join(OUTER, INNER, BooleanOp.ONLY_FIRST
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
      ctx.getClickedFace().getOpposite()
    );
    return result;
  }

  @Override
  public VoxelShape getShape (BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    return CUBE;
  }
}
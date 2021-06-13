package com.github.talrey.createdeco.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CoinStackBlock extends Block {
  private static final VoxelShape[] SHAPE = {
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 2d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 4d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 6d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 8d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 10d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 12d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 14d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 16d, 16d
    )
  };

  public CoinStackBlock (Properties properties) {
    super(properties);
    this.setDefaultState(this.getStateContainer().getBaseState().with(BlockStateProperties.LAYERS_1_8, 1));
  }

  @Override
  public VoxelShape getShape (BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
    return SHAPE[state.get(BlockStateProperties.LAYERS_1_8)-1];
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockItemUseContext ctx) {
    return getDefaultState();
  }

  @Override
  protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) { builder.add(BlockStateProperties.LAYERS_1_8); }
}

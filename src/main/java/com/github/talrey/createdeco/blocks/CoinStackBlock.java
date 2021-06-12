package com.github.talrey.createdeco.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;

import javax.annotation.Nullable;

public class CoinStackBlock extends Block {
  public CoinStackBlock (Properties properties) {
    super(properties);
    this.setDefaultState(this.getStateContainer().getBaseState().with(BlockStateProperties.LAYERS_1_8, 1));
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockItemUseContext ctx) {
    return getDefaultState();
  }

  @Override
  protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) { builder.add(BlockStateProperties.LAYERS_1_8); }
}

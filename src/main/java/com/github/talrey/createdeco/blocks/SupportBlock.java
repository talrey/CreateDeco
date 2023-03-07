package com.github.talrey.createdeco.blocks;

import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

public class SupportBlock extends DirectionalBlock {
  public SupportBlock (Properties props) {
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
      ctx.getClickedFace()
    );
    for (Direction side : Iterate.directions) {
      BlockState neighbor = ctx.getLevel()
        .getBlockState(ctx.getClickedPos()
          .relative(side));
      if (neighbor.getBlock() instanceof SupportBlock) {
        result = result.setValue(FACING, neighbor.getValue(FACING));
      }
    }
    return result;
  }
}

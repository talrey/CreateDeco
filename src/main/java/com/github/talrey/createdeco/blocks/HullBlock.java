package com.github.talrey.createdeco.blocks;

import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class HullBlock extends HorizontalDirectionalBlock {
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
}

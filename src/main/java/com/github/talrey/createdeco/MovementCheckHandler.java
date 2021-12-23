package com.github.talrey.createdeco;

import com.github.talrey.createdeco.blocks.DecalBlock;
import com.simibubi.create.content.contraptions.components.structureMovement.BlockMovementChecks;
import com.simibubi.create.content.contraptions.components.structureMovement.BlockMovementChecks.CheckResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MovementCheckHandler implements BlockMovementChecks.AttachedCheck {
  private static MovementCheckHandler singleton;

  private MovementCheckHandler () {
  }

  public static MovementCheckHandler get () { return singleton; }

  public static void register () {
    if (singleton == null) singleton = new MovementCheckHandler();
    BlockMovementChecks.registerAttachedCheck(singleton);
  }

  @Override
  public BlockMovementChecks.CheckResult isBlockAttachedTowards(BlockState state, Level world, BlockPos pos, Direction direction) {
    if (! (state.getBlock() instanceof DecalBlock)) return CheckResult.PASS;
    return DecalBlock.canSupportDecal(world, pos.offset(state.getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal()), direction) ? CheckResult.SUCCESS : CheckResult.FAIL;
  }
}

package com.github.talrey.createdeco;

import com.github.talrey.createdeco.blocks.DecalBlock;
import com.simibubi.create.content.contraptions.BlockMovementChecks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class MovementCheckHandler implements BlockMovementChecks.AllChecks {
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
    if (! (state.getBlock() instanceof DecalBlock)) return BlockMovementChecks.CheckResult.PASS;
    return DecalBlock.canSupportDecal(world, pos.offset(state.getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal()), direction) ? BlockMovementChecks.CheckResult.SUCCESS : BlockMovementChecks.CheckResult.FAIL;
  }

  @Override
  public BlockMovementChecks.CheckResult isBrittle(BlockState state) {
    if (state.getBlock() instanceof DecalBlock) return BlockMovementChecks.CheckResult.SUCCESS;
    /*else*/ return BlockMovementChecks.CheckResult.PASS;
  }

  @Override
  public BlockMovementChecks.CheckResult isMovementAllowed(BlockState state, Level world, BlockPos pos) {
    return BlockMovementChecks.CheckResult.PASS; // none of our blocks stop movement
  }

  @Override
  public BlockMovementChecks.CheckResult isMovementNecessary(BlockState state, Level world, BlockPos pos) {
    if (state.getBlock() instanceof DecalBlock) return BlockMovementChecks.CheckResult.SUCCESS;
    /*else*/return BlockMovementChecks.CheckResult.PASS;
  }

  @Override
  public BlockMovementChecks.CheckResult isNotSupportive(BlockState state, Direction direction) {
    if (state.getBlock() instanceof DecalBlock) return BlockMovementChecks.CheckResult.SUCCESS;
    /*else*/return BlockMovementChecks.CheckResult.PASS;
  }
}

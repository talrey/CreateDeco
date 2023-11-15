package com.github.talrey.createdeco.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DecalBlock extends FaceAttachedHorizontalDirectionalBlock {
  protected static final VoxelShape CEILING_AABB;
  protected static final VoxelShape FLOOR_AABB;
  protected static final VoxelShape NORTH_AABB;
  protected static final VoxelShape SOUTH_AABB;
  protected static final VoxelShape WEST_AABB;
  protected static final VoxelShape EAST_AABB;

  public DecalBlock(Properties properties) {
    super(properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FACE, AttachFace.WALL));
  }

  public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    Direction direction = state.getValue(FACING);
    return switch (state.getValue(FACE)) {
      case FLOOR -> FLOOR_AABB;
      case CEILING -> CEILING_AABB;
      case WALL -> switch (direction) {
        case EAST -> EAST_AABB;
        case WEST -> WEST_AABB;
        case SOUTH -> SOUTH_AABB;
        case NORTH, UP, DOWN -> NORTH_AABB;
      };
    };
  }

  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, FACE);
  }

  static {
    CEILING_AABB = Block.box(2.0D, 14.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    FLOOR_AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);
    NORTH_AABB = Block.box(2.0D, 2.0D, 14.0D, 14.0D, 14.0D, 16.0D);
    SOUTH_AABB = Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 2.0D);
    WEST_AABB = Block.box(14.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D);
    EAST_AABB = Block.box(0.0D, 2.0D, 2.0D, 2.0D, 14.0D, 14.0D);
  }
}

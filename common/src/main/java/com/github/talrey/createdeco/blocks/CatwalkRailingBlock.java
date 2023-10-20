package com.github.talrey.createdeco.blocks;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CatwalkRailingBlock extends Block implements IWrenchable, SimpleWaterloggedBlock {
  private static final VoxelShape VOXEL_NORTH = Block.box(
    0d, 0d, 0d,
    16d, 16d, 2d
  );
  private static final VoxelShape VOXEL_SOUTH = Block.box(
    0d, 0d, 14d,
    16d, 16d, 16d
  );
  private static final VoxelShape VOXEL_EAST = Block.box(
    14d, 0d, 0d,
    16d, 16d, 16d
  );
  private static final VoxelShape VOXEL_WEST = Block.box(
    0d, 0d, 0d,
    2d, 16d, 16d
  );

  public static final BooleanProperty NORTH_FENCE = BlockStateProperties.NORTH;
  public static final BooleanProperty SOUTH_FENCE = BlockStateProperties.SOUTH;
  public static final BooleanProperty EAST_FENCE  = BlockStateProperties.EAST;
  public static final BooleanProperty WEST_FENCE  = BlockStateProperties.WEST;

  public CatwalkRailingBlock (Properties props) {
    super(props);
    this.registerDefaultState(this.defaultBlockState()
      .setValue(NORTH_FENCE, false)
      .setValue(SOUTH_FENCE, false)
      .setValue(EAST_FENCE,  false)
      .setValue(WEST_FENCE,  false)
      .setValue(BlockStateProperties.WATERLOGGED, false)
    );
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.WATERLOGGED);
  }

  @Override
  public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
    if (targetedFace.getAxis() == Direction.Axis.Y) {
      int state =
        (originalState.getValue(NORTH_FENCE) ? 8 : 0) +
          (originalState.getValue(EAST_FENCE)  ? 4 : 0) +
          (originalState.getValue(SOUTH_FENCE) ? 2 : 0) +
          (originalState.getValue(WEST_FENCE)  ? 1 : 0);
      return originalState
        .setValue(NORTH_FENCE, (state & 1) == 1)
        .setValue(EAST_FENCE,  (state & 8) == 8)
        .setValue(SOUTH_FENCE, (state & 4) == 4)
        .setValue(WEST_FENCE,  (state & 2) == 2);
    }
    return originalState;
  }

  @Override
  public boolean canPlaceLiquid (BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
    return !state.getValue(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
  }
}

package com.github.talrey.createdeco.blocks;

import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@MethodsReturnNonnullByDefault
public class DecalBlock extends FaceAttachedHorizontalDirectionalBlock implements ProperWaterloggedBlock {
  private static final VoxelShape NORTH;
  private static final VoxelShape SOUTH;
  private static final VoxelShape EAST;
  private static final VoxelShape WEST;
  private static final VoxelShape CEILING;
  private static final VoxelShape FLOOR;

  public DecalBlock(Properties props) {
    super(props);
    this.registerDefaultState(this.defaultBlockState()
      .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
      .setValue(BlockStateProperties.WATERLOGGED, false)
    );
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    BlockState stateForPlacement = super.getStateForPlacement(ctx);
    return withWater(stateForPlacement, ctx);
  }

  @Override
  public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
    updateWater(world, state, pos);
    return super.updateShape(state, dir, neighbor, world, pos, neighborPos);
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
    builder.add(BlockStateProperties.WATERLOGGED);
    builder.add(FACE);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
      Direction dir = state.getValue(FACING);
      return switch (state.getValue(FACE)) {
          case FLOOR -> FLOOR;
          case CEILING -> CEILING;
          case WALL -> switch (dir) {
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            default -> NORTH;
          };
      };
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
  }

  static {
    NORTH = Block.box(1D, 1D, 15D, 15D, 15D, 16D);
    SOUTH = Block.box(1D, 1D, 0D, 15D, 15D, 1D);
    WEST = Block.box(15D, 1D, 1D, 16D, 15D, 15D);
    EAST = Block.box(0D, 1D, 1D, 1D, 15D, 15D);
    CEILING = Block.box(1D, 15D, 1D, 15D, 16D, 15D);
    FLOOR = Block.box(1D, 0D, 1D, 15D, 1D, 15D);
  }
}

package com.github.talrey.createdeco.blocks;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CatwalkBlock extends Block implements IWrenchable, ProperWaterloggedBlock {
  private static final VoxelShape VOXEL_TOP = Block.box(
    0d, 14d, 0d,
    16d, 16d, 16d
  );
  private static final VoxelShape SUPPORTED = Shapes.block();
  public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

  public CatwalkBlock (Properties props) {
    super(props);
    this.registerDefaultState(this.defaultBlockState()
        .setValue(BOTTOM, false)
        .setValue(WATERLOGGED, false));
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    return getInteractionShape(state, reader, pos);
  }

  @Override
  public VoxelShape getInteractionShape (BlockState state, BlockGetter world, BlockPos pos) {
    return state.getValue(BOTTOM) ? SUPPORTED : VOXEL_TOP;
  }


  private boolean isBottom(BlockGetter level, BlockPos pos) {
    return level.getBlockState(pos.below()).getBlock() instanceof SupportBlock;
  }

  public static boolean isCatwalk (ItemStack test) {
    return (test.getItem() instanceof BlockItem be)
      && be.getBlock() instanceof CatwalkBlock;
    //isCatwalk(((BlockItem)test.getItem()).getBlock());
  }

  public static boolean isCatwalk (Block test) {
    return test instanceof CatwalkBlock || test instanceof CatwalkStairBlock;
  }

  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
    BlockPos blockPos = ctx.getClickedPos();
    Level level = ctx.getLevel();

    return defaultBlockState()
        .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER)
        .setValue(BOTTOM, this.isBottom(level, blockPos));
  }

  public static boolean canPlaceCatwalk (Level world, BlockPos pos) {
    return world.getBlockState(pos).canBeReplaced();
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.WATERLOGGED, BOTTOM);
  }

  @Override
  public boolean canPlaceLiquid (BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
    return !state.getValue(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
  }

  public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    BlockState blockState = state.setValue(BOTTOM, this.isBottom(level, pos));
    if (state != blockState) {
      level.setBlock(pos, blockState, 3);
    }
  }

  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    if (!level.isClientSide) {
      level.scheduleTick(pos, this, 1);
    }

  }

  public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
    if (state.getValue(WATERLOGGED)) {
      level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
    }

    if (!level.isClientSide()) {
      level.scheduleTick(pos, this, 1);
    }

    return state;
  }
}

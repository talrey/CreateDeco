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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CatwalkBlock extends Block implements IWrenchable, ProperWaterloggedBlock {
  private static final VoxelShape VOXEL_TOP = Block.box(
    0d, 14d, 0d,
    16d, 16d, 16d
  );
  private static final VoxelShape VOXEL_BOTTOM = Block.box(
    0d, 0d, 0d,
    16d, 2d, 16d
  );
  private static final VoxelShape VOXEL_RAILING_NORTH = Block.box(
    0d, 0d, 0d,
    16d, 14d, 2d
  );
  private static final VoxelShape VOXEL_RAILING_SOUTH = Block.box(
    0d, 0d, 14d,
    16d, 14d, 16d
  );
  private static final VoxelShape VOXEL_RAILING_EAST = Block.box(
    14d, 0d, 0d,
    16d, 14d, 16d
  );
  private static final VoxelShape VOXEL_RAILING_WEST = Block.box(
    0d, 0d, 0d,
    2d, 14d, 16d
  );

  private static final VoxelShape SUPPORTED = Shapes.block();
  // This property indicates whether there's a support block under it.
  public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

  // Properties to indicate whether there's a catwalk on the top or bottom
  public static final BooleanProperty CATWALK_TOP = BooleanProperty.create("catwalk_top");
  public static final BooleanProperty CATWALK_BOTTOM = BooleanProperty.create("catwalk_bottom");
  // Properties to indicate the presence of railings on any of the four sides
  public static final BooleanProperty RAILING_NORTH = BooleanProperty.create("railing_north");
  public static final BooleanProperty RAILING_SOUTH = BooleanProperty.create("railing_south");
  public static final BooleanProperty RAILING_EAST  = BooleanProperty.create("railing_east");
  public static final BooleanProperty RAILING_WEST  = BooleanProperty.create("railing_west");

  // A string indicating the material of this block. This is used in
  // interactions between the catwalk block and catwalk items and railing items.
  // Only items of the same material can be combined together.
  public final String metal;

  public CatwalkBlock (Properties props, String metal) {
    super(props);
    this.metal = metal;
    this.registerDefaultState(this.defaultBlockState()
        .setValue(BOTTOM, false)
        .setValue(WATERLOGGED, false)
        .setValue(CATWALK_TOP, true)
        .setValue(CATWALK_BOTTOM, true)
        .setValue(RAILING_NORTH, true)
        .setValue(RAILING_SOUTH, true)
        .setValue(RAILING_EAST, true)
        .setValue(RAILING_WEST, true)
      );
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    return getInteractionShape(state, reader, pos);
  }

  @Override
  public VoxelShape getInteractionShape (BlockState state, BlockGetter world, BlockPos pos) {
    VoxelShape shape = Shapes.empty();
    if (state.getValue(BOTTOM))
      shape = Shapes.join(shape, SUPPORTED, BooleanOp.OR);

    if (state.getValue(CATWALK_TOP))
      shape = Shapes.join(shape, VOXEL_TOP, BooleanOp.OR);
    if (state.getValue(CATWALK_BOTTOM))
      shape = Shapes.join(shape, VOXEL_BOTTOM, BooleanOp.OR);

    if (state.getValue(RAILING_NORTH))
      shape = Shapes.join(shape, VOXEL_RAILING_NORTH, BooleanOp.OR);
    if (state.getValue(RAILING_SOUTH))
      shape = Shapes.join(shape, VOXEL_RAILING_SOUTH, BooleanOp.OR);
    if (state.getValue(RAILING_EAST))
      shape = Shapes.join(shape, VOXEL_RAILING_EAST, BooleanOp.OR);
    if (state.getValue(RAILING_WEST))
      shape = Shapes.join(shape, VOXEL_RAILING_WEST, BooleanOp.OR);

    return shape;
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
    builder.add(CATWALK_BOTTOM);
    builder.add(CATWALK_TOP);
    builder.add(RAILING_NORTH);
    builder.add(RAILING_SOUTH);
    builder.add(RAILING_EAST);
    builder.add(RAILING_WEST);
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

  // TODO: use this to delete empty blocks
  public static boolean isEmpty(BlockState state) {
    boolean isEmpty = true;

    isEmpty |= state.getValue(CATWALK_TOP);
    isEmpty |= state.getValue(CATWALK_BOTTOM);

    isEmpty |= state.getValue(RAILING_NORTH);
    isEmpty |= state.getValue(RAILING_SOUTH);
    isEmpty |= state.getValue(RAILING_EAST);
    isEmpty |= state.getValue(RAILING_WEST);

    return isEmpty;
  }
}

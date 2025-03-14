package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.ItemRegistry;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.network.chat.Component;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class CatwalkBlock extends Block implements IWrenchable, ProperWaterloggedBlock, ISpecialBlockItemRequirement {
  private static final VoxelShape VOXEL_CATWALK_TOP = Block.box(
    0d, 14d, 0d,
    16d, 16d, 16d
  );
  private static final VoxelShape VOXEL_CATWALK_BOTTOM = Block.box(
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

  private static final HashMap<VoxelShape, BooleanProperty> SHAPE_PROPERTY_MAPPING = new HashMap<VoxelShape, BooleanProperty>();

  static {
    SHAPE_PROPERTY_MAPPING.put(VOXEL_CATWALK_TOP,    CATWALK_TOP);
    SHAPE_PROPERTY_MAPPING.put(VOXEL_CATWALK_BOTTOM, CATWALK_BOTTOM);
    SHAPE_PROPERTY_MAPPING.put(VOXEL_RAILING_NORTH, RAILING_NORTH);
    SHAPE_PROPERTY_MAPPING.put(VOXEL_RAILING_SOUTH, RAILING_SOUTH);
    SHAPE_PROPERTY_MAPPING.put(VOXEL_RAILING_EAST,  RAILING_EAST);
    SHAPE_PROPERTY_MAPPING.put(VOXEL_RAILING_WEST,  RAILING_WEST);
  }

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
        .setValue(CATWALK_BOTTOM, false)
        .setValue(RAILING_NORTH, false)
        .setValue(RAILING_SOUTH, false)
        .setValue(RAILING_EAST, false)
        .setValue(RAILING_WEST, false)
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
      shape = Shapes.join(shape, VOXEL_CATWALK_TOP, BooleanOp.OR);
    if (state.getValue(CATWALK_BOTTOM))
      shape = Shapes.join(shape, VOXEL_CATWALK_BOTTOM, BooleanOp.OR);

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

  private Optional<VoxelShape> clickedShape(BlockState state, Vec3 subboxClickLocation) {
    double min_distance = 1.0;

    Optional<VoxelShape> closestShape = Optional.empty();

    for (Map.Entry<VoxelShape, BooleanProperty> entry: SHAPE_PROPERTY_MAPPING.entrySet()) {
      VoxelShape shape = entry.getKey();
      BooleanProperty property = entry.getValue();

      // Ignore shapes that are not enabled
      if (!state.getValue(property)) continue;

      Optional<Vec3> point = shape.closestPointTo(subboxClickLocation);
      if (point.isEmpty()) continue;

      double distance = point.get().distanceToSqr(subboxClickLocation);
      if (distance < min_distance) {
	closestShape = Optional.of(shape);
	min_distance = distance;
      }
    }

    return closestShape;
  }

  @Override
  public InteractionResult onSneakWrenched (BlockState state, UseOnContext context) {
    BlockPos pos   = context.getClickedPos();
    Direction face = context.getClickedFace();
    Level level    = context.getLevel();
    Player player  = context.getPlayer();

    // This position will initially be in [-0.5, 0.5].
    Vec3 subbox    = context.getClickLocation().subtract(pos.getCenter());
    // We want to map that to a position in [0, 1]
    subbox = subbox.add(0.5, 0.5, 0.5);

    if (level.isClientSide()) return InteractionResult.PASS;

    // Check which of the shapes was clicked
    Optional<VoxelShape> clickedShapeOption = clickedShape(state, subbox);

    if (clickedShapeOption.isPresent()) {
      VoxelShape clickedShape = clickedShapeOption.get();
      BooleanProperty property = SHAPE_PROPERTY_MAPPING.get(clickedShape);
      // Set the property to false
      state = state.setValue(property, false);
      level.setBlock(pos, state, 3);
      playRemoveSound(level, pos);
      if (!player.getAbilities().instabuild) {
	// Return the corresponding item to the player
	if (clickedShape == VOXEL_CATWALK_TOP || clickedShape == VOXEL_CATWALK_BOTTOM) {
	  player.addItem(
	    new ItemStack(BlockRegistry.CATWALKS.get(this.metal))
	  );
	} else {
	  player.addItem(
	    new ItemStack(BlockRegistry.CATWALK_RAILINGS.get(this.metal))
	  );
	}
      }
      // Check if we removed the last element of the block.
      // In that case, clear the block
      if (isEmpty(state)) {
	level.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
      }

      return InteractionResult.SUCCESS;
    } else {
      return InteractionResult.PASS;
    }
  }

  private boolean isBottom(BlockGetter level, BlockPos pos) {
    return
      (
        !(level.getBlockState(pos).getBlock() instanceof CatwalkBlock)
        || level.getBlockState(pos).getValue(CATWALK_TOP)
      ) &&
      level.getBlockState(pos.below()).getBlock() instanceof SupportBlock;
  }

  public static boolean isCatwalk (ItemStack test) {
    return (test.getItem() instanceof BlockItem be)
      && be.getBlock() instanceof CatwalkBlock;
    //isCatwalk(((BlockItem)test.getItem()).getBlock());
  }

  public static boolean isCatwalk (Block test) {
    return test instanceof CatwalkBlock;
  }

  public static boolean isCatwalkOrStair (Block test) {
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

  public static BooleanProperty fromDirection (Direction face) {
    return switch (face) {
      case SOUTH -> RAILING_SOUTH;
      case EAST  -> RAILING_EAST;
      case WEST  -> RAILING_WEST;
      default -> RAILING_NORTH;
    };
  }

  public static boolean hasAnyCatwalks(BlockState state) {
    boolean hasAnyCatwalks = false;

    hasAnyCatwalks |= state.getValue(CATWALK_TOP);
    hasAnyCatwalks |= state.getValue(CATWALK_BOTTOM);

    return hasAnyCatwalks;
  }

  public static boolean hasAnyRailings(BlockState state) {
    boolean hasAnyRailings = false;

    hasAnyRailings |= state.getValue(RAILING_NORTH);
    hasAnyRailings |= state.getValue(RAILING_SOUTH);
    hasAnyRailings |= state.getValue(RAILING_EAST);
    hasAnyRailings |= state.getValue(RAILING_WEST);

    return hasAnyRailings;
  }

  public static boolean isEmpty(BlockState state) {
    boolean hasAnyElement = false;

    hasAnyElement |= hasAnyCatwalks(state);
    hasAnyElement |= hasAnyRailings(state);

    return !hasAnyElement;
  }

  /**
     Wrenching a catwalk block behaves differently depending on the block state.
     If the catwalk block doesn't have any railings, then wrenching it will
     toggle the catwalk state between the top and bottom version.

     If it does have railings then we use the default implementation, which
     rotates the block.
   **/
  @Override
  public InteractionResult onWrenched (BlockState state, UseOnContext context) {
    BlockPos pos   = context.getClickedPos();
    Level level    = context.getLevel();

    BlockState new_state;

    boolean hasAnyRailings = hasAnyRailings(state);

    if (!hasAnyRailings && state.getValue(CATWALK_TOP)) {
      new_state = state
	.setValue(CATWALK_TOP, false)
	.setValue(CATWALK_BOTTOM, true);
    } else if (!hasAnyRailings && state.getValue(CATWALK_BOTTOM)) {
      new_state = state
	.setValue(CATWALK_TOP, true)
	.setValue(CATWALK_BOTTOM, false);
    } else {
      // Otherwise use the default interaction to rotate the block
      return IWrenchable.super.onWrenched(state, context);
    }

    level.setBlock(pos, new_state, 3);
    return InteractionResult.SUCCESS;
  }

  @Override
  public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
    if (targetedFace.getAxis() == Direction.Axis.Y) {
      int state =
              (originalState.getValue(RAILING_NORTH) ? 8 : 0) +
                      (originalState.getValue(RAILING_EAST)  ? 4 : 0) +
                      (originalState.getValue(RAILING_SOUTH) ? 2 : 0) +
                      (originalState.getValue(RAILING_WEST)  ? 1 : 0);
      return originalState
              .setValue(RAILING_NORTH, (state & 1) == 1)
              .setValue(RAILING_EAST,  (state & 8) == 8)
              .setValue(RAILING_SOUTH, (state & 4) == 4)
              .setValue(RAILING_WEST,  (state & 2) == 2);
    }
    return originalState;
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    // A bitmask that contains the state of the railings
    int bits =
      (state.getValue(RAILING_NORTH) ? 8 : 0) +
      (state.getValue(RAILING_EAST)  ? 4 : 0) +
      (state.getValue(RAILING_SOUTH) ? 2 : 0) +
      (state.getValue(RAILING_WEST)  ? 1 : 0);
    return switch (rotation) {
      case CLOCKWISE_90 ->
        state
          .setValue(RAILING_NORTH, (bits & 1) == 1)
          .setValue(RAILING_EAST,  (bits & 8) == 8)
          .setValue(RAILING_SOUTH, (bits & 4) == 4)
          .setValue(RAILING_WEST,  (bits & 2) == 2)
          ;
      case CLOCKWISE_180 ->
        state
          .setValue(RAILING_NORTH, (bits & 2) == 2)
          .setValue(RAILING_EAST,  (bits & 1) == 1)
          .setValue(RAILING_SOUTH, (bits & 8) == 8)
          .setValue(RAILING_WEST,  (bits & 4) == 4)
          ;
      case COUNTERCLOCKWISE_90 ->
        state
          .setValue(RAILING_NORTH, (bits & 4) == 4)
          .setValue(RAILING_EAST,  (bits & 2) == 2)
          .setValue(RAILING_SOUTH, (bits & 1) == 1)
          .setValue(RAILING_WEST,  (bits & 8) == 8)
          ;
      default -> state;
    };
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    // A bitmask that contains the state of the railings
    int bits =
      (state.getValue(RAILING_NORTH) ? 8 : 0) +
      (state.getValue(RAILING_EAST)  ? 4 : 0) +
      (state.getValue(RAILING_SOUTH) ? 2 : 0) +
      (state.getValue(RAILING_WEST)  ? 1 : 0);
    return switch (mirror) {
      case LEFT_RIGHT ->
        state
          .setValue(RAILING_NORTH, (bits & 2) == 2)
          .setValue(RAILING_SOUTH, (bits & 8) == 8)
          ;
      case FRONT_BACK ->
        state
          .setValue(RAILING_EAST, (bits & 1) == 1)
          .setValue(RAILING_WEST, (bits & 4) == 4)
          ;
      default -> state;
    };
  }

  @Override
  public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
    ArrayList<ItemStack> stacks = new ArrayList<>();

    int catwalk_count = 0;
    catwalk_count += state.getValue(CATWALK_TOP)? 1 : 0;
    catwalk_count += state.getValue(CATWALK_BOTTOM)? 1 : 0;
    stacks.add(new ItemStack(this.asItem(), catwalk_count));

    int railing_count = 0;
    railing_count += state.getValue(RAILING_NORTH)? 1 : 0;
    railing_count += state.getValue(RAILING_EAST)? 1 : 0;
    railing_count += state.getValue(RAILING_SOUTH)? 1 : 0;
    railing_count += state.getValue(RAILING_WEST)? 1 : 0;
    stacks.add(new ItemStack(
                 BlockRegistry.CATWALK_RAILINGS.get(this.metal).get().asItem(),
                 railing_count)
    );

    return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, stacks);
  }

}

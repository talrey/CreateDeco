package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.api.RailingColor;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CatwalkStairBlock extends Block implements IWrenchable, ProperWaterloggedBlock {
  public static final BooleanProperty RAILING_LEFT = BooleanProperty.create("railing_left");
  public static final BooleanProperty RAILING_RIGHT = BooleanProperty.create("railing_right");

  public static final EnumProperty<RailingColor> RAILING_LEFT_COLOR = EnumProperty.create("railing_left_color", RailingColor.class);
  public static final EnumProperty<RailingColor> RAILING_RIGHT_COLOR = EnumProperty.create("railing_right_color", RailingColor.class);

  public final String metal;

  private static final VoxelShape BOX_NORTH = Shapes.join(
    Block.box(0d, 14d, 8d, 16d, 16d, 16d),
    Block.box(0d, 6d, 0d, 16d, 8d, 8d),
    BooleanOp.OR
  );
  private static final VoxelShape RAILING_WEST = Shapes.or(
    Block.box(14,0,0, 16,20,4),
    Block.box(14,4,4, 16,24,8),
    Block.box(14,8,8, 16,28,12),
    Block.box(14,12,12, 16,32,16)
  );
  private static final VoxelShape RAILING_EAST = Shapes.or(
    Block.box(0,0,0, 2,20,4),
    Block.box(0,4,4, 2,24,8),
    Block.box(0,8,8, 2,28,12),
    Block.box(0,12,12, 2,32,16)
  );

  private static final VoxelShape NORTH_LEFT  = Shapes.or(BOX_NORTH, RAILING_WEST);
  private static final VoxelShape NORTH_RIGHT = Shapes.or(BOX_NORTH, RAILING_EAST);
  private static final VoxelShape NORTH_BOTH  = Shapes.or(BOX_NORTH, RAILING_WEST, RAILING_EAST);

  private static final VoxelShape[] SHAPES = {
    BOX_NORTH, NORTH_LEFT, NORTH_RIGHT, NORTH_BOTH,
    rotate(BOX_NORTH, Direction.SOUTH), rotate(NORTH_LEFT, Direction.SOUTH), rotate(NORTH_RIGHT, Direction.SOUTH), rotate(NORTH_BOTH, Direction.SOUTH),
    rotate(BOX_NORTH, Direction.EAST),  rotate(NORTH_LEFT, Direction.EAST), rotate(NORTH_RIGHT, Direction.EAST), rotate(NORTH_BOTH, Direction.EAST),
    rotate(BOX_NORTH, Direction.WEST),  rotate(NORTH_LEFT, Direction.WEST), rotate(NORTH_RIGHT, Direction.WEST), rotate(NORTH_BOTH, Direction.WEST)
  };
  private static final int LOOKUP_NORTH = 0;
  private static final int LOOKUP_SOUTH = 4;
  private static final int LOOKUP_EAST  = 8;
  private static final int LOOKUP_WEST  = 12;
  private static final int ADJ_LEFT  = 1;
  private static final int ADJ_RIGHT = 2;

  public CatwalkStairBlock (Properties props, String metal) {
    super(props);
    this.metal = metal;
    this.registerDefaultState(this.defaultBlockState()
      .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
      .setValue(BlockStateProperties.WATERLOGGED, false)
      .setValue(RAILING_LEFT, false)
      .setValue(RAILING_RIGHT, false)
      .setValue(RAILING_LEFT_COLOR, RailingColor.NATURAL)
      .setValue(RAILING_RIGHT_COLOR, RailingColor.NATURAL)
    );
  }

  public static boolean isCatwalkStair (ItemStack test) {
    return (test.getItem() instanceof BlockItem be)
      && be.getBlock() instanceof CatwalkStairBlock;
  }

  public static boolean isCatwalkStair (Block test) {
    return test instanceof CatwalkBlock || test instanceof CatwalkStairBlock;
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    Direction facing = ctx.getHorizontalDirection();
    FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());

    return defaultBlockState()
      .setValue(BlockStateProperties.HORIZONTAL_FACING, facing.getOpposite())
      .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
    builder.add(BlockStateProperties.WATERLOGGED);
    builder.add(RAILING_LEFT);
    builder.add(RAILING_RIGHT);
    builder.add(RAILING_LEFT_COLOR);
    builder.add(RAILING_RIGHT_COLOR);
  }

  @Override
  public boolean canPlaceLiquid(@Nullable Player playerEntity, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
    return !state.getValue(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
  }

  @Override
  public VoxelShape getShape (BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
    int index = switch(state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
      case SOUTH -> LOOKUP_SOUTH;
      case EAST  -> LOOKUP_EAST;
      case WEST  -> LOOKUP_WEST;
      default    -> LOOKUP_NORTH;
    };
    if (state.getValue(RAILING_LEFT))  index += ADJ_LEFT;
    if (state.getValue(RAILING_RIGHT)) index += ADJ_RIGHT;
    return SHAPES[index];
  }

  private static VoxelShape rotate (VoxelShape input, Direction to) {
    VoxelShape[] temp = new VoxelShape[]{ input, Shapes.empty() };
    int iterations = (to.get2DDataValue() - Direction.NORTH.get2DDataValue() + 4) % 4;
    for (int count=0; count<iterations; count++) {
      temp[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
        temp[1] = Shapes.or(temp[1],
          Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX))
      );
      temp[0] = temp[1];
      temp[1] = Shapes.empty();
    }
    return temp[0];
  }

  @Override
  public InteractionResult onSneakWrenched (BlockState state, UseOnContext context) {
    BlockPos pos   = context.getClickedPos();
    Vec3 subbox    = context.getClickLocation().subtract(pos.getCenter());
    Level level    = context.getLevel();
    Player player  = context.getPlayer();

    if (state.getValue(RAILING_RIGHT) || state.getValue(RAILING_LEFT)) {
      var xPos = subbox.x;
      var zPos = subbox.z;

      var dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
      boolean left = false;

      if (dir == Direction.NORTH) left = xPos > 0;
      if (dir == Direction.SOUTH) left = xPos < 0;
      if (dir == Direction.EAST) left = zPos > 0;
      if (dir == Direction.WEST) left = zPos < 0;

      BooleanProperty sideFlag = left ? RAILING_LEFT : RAILING_RIGHT;
      EnumProperty<RailingColor> sideColor = left ? RAILING_LEFT_COLOR : RAILING_RIGHT_COLOR;

      if (level.isClientSide() || !state.getValue(sideFlag)) return InteractionResult.PASS;

      RailingColor removedColor = state.getValue(sideColor);

      level.setBlock(pos, state
        .setValue(sideFlag, false)
        .setValue(sideColor, RailingColor.NATURAL), 3);

      var railingEntry = removedColor.isNatural()
        ? BlockRegistry.CATWALK_RAILINGS.get(metal)
        : BlockRegistry.CATWALK_RAILINGS.get(removedColor.getRegistryKey());

      if (!player.getAbilities().instabuild && railingEntry != null) {
        player.addItem(new ItemStack(railingEntry));
      }
      IWrenchable.playRemoveSound(level, pos);
      return InteractionResult.SUCCESS;
    }

    level.removeBlock(pos, false);
    if (!player.getAbilities().instabuild) player.addItem(new ItemStack(state.getBlock().asItem()));
    IWrenchable.playRemoveSound(level, pos);
    return InteractionResult.SUCCESS;

  }

  @Override
  protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
    return false;
  }

  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
  }
}
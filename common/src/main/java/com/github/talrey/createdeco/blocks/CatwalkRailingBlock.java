package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.CreateDecoMod;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
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

import javax.annotation.Nullable;

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
  public InteractionResult onSneakWrenched (BlockState state, UseOnContext context) {
    BlockPos pos   = context.getClickedPos();
    Vec3 subbox    = context.getClickLocation().subtract(pos.getCenter());
    //Direction face = context.getClickedFace();
    Level level    = context.getLevel();
    Player player  = context.getPlayer();
    if (level instanceof ServerLevel) {
      Direction near = Direction.getNearest(subbox.x, 0.5, subbox.z);
      //CreateDecoMod.LOGGER.info("Nearest to " + subbox + " is " + near);
      if (!BlockStateProperties.HORIZONTAL_FACING.getPossibleValues().contains(near))
        near = context.getHorizontalDirection();
      if (state.getValue(fromDirection(near))) {
        state = state.setValue(fromDirection(near), false);
        // return 1 railing?
//      if (level instanceof ServerLevel && player != null && !player.isCreative()) {
//        player.getInventory().placeItemBackInInventory();
//      }
        if (isEmpty(state)) {
          level.destroyBlock(pos, false);
          playRemoveSound(level, pos);
        }
        else level.setBlock(pos, state, 3);
      }
    }
    return InteractionResult.SUCCESS;
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    Direction facing = ctx.getHorizontalDirection();
    FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
    BlockState state = defaultBlockState()
      .setValue(NORTH_FENCE, (facing == Direction.NORTH))
      .setValue(SOUTH_FENCE, (facing == Direction.SOUTH))
      .setValue(EAST_FENCE,  (facing == Direction.EAST))
      .setValue(WEST_FENCE,  (facing == Direction.WEST))
      .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
    return state;
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(NORTH_FENCE);
    builder.add(SOUTH_FENCE);
    builder.add(EAST_FENCE);
    builder.add(WEST_FENCE);
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
  public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    return getInteractionShape(state, reader, pos);
  }

  @Override
  public VoxelShape getInteractionShape (BlockState state, BlockGetter world, BlockPos pos) {
    VoxelShape shape = Shapes.empty();
    if (state.getValue(NORTH_FENCE)) shape = Shapes.join(shape, VOXEL_NORTH, BooleanOp.OR);
    if (state.getValue(SOUTH_FENCE)) shape = Shapes.join(shape, VOXEL_SOUTH, BooleanOp.OR);
    if (state.getValue(EAST_FENCE))  shape = Shapes.join(shape, VOXEL_EAST,  BooleanOp.OR);
    if (state.getValue(WEST_FENCE))  shape = Shapes.join(shape, VOXEL_WEST,  BooleanOp.OR);

    return shape;
  }

  @Override
  public boolean canPlaceLiquid (BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
    return !state.getValue(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
  }

  // used to ensure the block doesn't leave a ghost behind if all 4 sides are gone
  @Override
  public void neighborChanged (
    BlockState state, Level level, BlockPos pos,
    Block neighborBlock, BlockPos neighborPos, boolean movedByPiston
  ) {

    if (isEmpty(state)) level.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
    super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
  }

  public static boolean isRailing (ItemStack test) {
    return (test.getItem() instanceof BlockItem) && isRailing(((BlockItem)test.getItem()).getBlock());
  }

  public static boolean isRailing (Block test) {
    return test instanceof CatwalkRailingBlock;
  }

  public static BooleanProperty fromDirection (Direction face) {
    return switch (face) {
      case SOUTH -> SOUTH_FENCE;
      case EAST  -> EAST_FENCE;
      case WEST  -> WEST_FENCE;
      default -> NORTH_FENCE;
    };
  }

  public static boolean isEmpty (BlockState state) {
    boolean safe = false;
    for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
      safe |= state.getValue(fromDirection(dir));
    }
    return !safe;
  }
}

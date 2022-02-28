package com.github.talrey.createdeco.blocks;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
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

public class CatwalkBlock extends Block implements IWrenchable, SimpleWaterloggedBlock {
  private static final VoxelShape VOXEL_BOTTOM = Block.box(
    0d, 0d, 0d,
    16d, 2d, 16d
  );
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
  public static final BooleanProperty LIFTED      = BlockStateProperties.BOTTOM;

  private static boolean hasNeighborTo (Direction side, BlockPlaceContext ctx) {
    return ctx.getLevel().getBlockState(ctx.getClickedPos().offset(side.getNormal())).getBlock() instanceof CatwalkBlock;
  }

  public CatwalkBlock (Properties props) {
    super(props);
    this.registerDefaultState(this.defaultBlockState()
      .setValue(NORTH_FENCE, false)
      .setValue(SOUTH_FENCE, false)
      .setValue(EAST_FENCE,  false)
      .setValue(WEST_FENCE,  false)
      .setValue(LIFTED,      true)
      .setValue(BlockStateProperties.WATERLOGGED, false)
    );
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    return getInteractionShape(state, reader, pos);
  }

  @Override
  public VoxelShape getInteractionShape (BlockState state, BlockGetter world, BlockPos pos) {
    VoxelShape shape = VOXEL_BOTTOM;

    if (state.getValue(NORTH_FENCE)) shape = Shapes.join(shape, VOXEL_NORTH, BooleanOp.OR);
    if (state.getValue(SOUTH_FENCE)) shape = Shapes.join(shape, VOXEL_SOUTH, BooleanOp.OR);
    if (state.getValue(EAST_FENCE))  shape = Shapes.join(shape, VOXEL_EAST,  BooleanOp.OR);
    if (state.getValue(WEST_FENCE))  shape = Shapes.join(shape, VOXEL_WEST,  BooleanOp.OR);

    return shape.move(0d, state.getValue(BlockStateProperties.BOTTOM) ? 0d : -2/16d, 0d);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    Direction facing = ctx.getHorizontalDirection();
    FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
    boolean lift     = (ctx.getClickLocation().y - ctx.getClickedPos().getY()) < 0.5f;

    BlockState state = defaultBlockState()
      .setValue(LIFTED, lift)
      .setValue(NORTH_FENCE, (facing == Direction.NORTH) && !hasNeighborTo(Direction.NORTH, ctx))
      .setValue(SOUTH_FENCE, (facing == Direction.SOUTH) && !hasNeighborTo(Direction.SOUTH, ctx))
      .setValue(EAST_FENCE,  (facing == Direction.EAST)  && !hasNeighborTo(Direction.EAST,  ctx))
      .setValue(WEST_FENCE,  (facing == Direction.WEST)  && !hasNeighborTo(Direction.WEST,  ctx))
      .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);

    if (!lift) {
      Level world = ctx.getLevel();
      if (canPlaceCatwalk(world, ctx.getClickedPos().offset(0,1,0))) {
        world.setBlock(ctx.getClickedPos().offset(0,1,0), state, 3);
        ctx.getPlayer().getItemInHand(ctx.getHand()).shrink(1);
        world.playSound(ctx.getPlayer(), ctx.getClickedPos().offset(0,1,0),
          SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 1f, 1f
        );
        return world.getBlockState(ctx.getClickedPos());
      }
    }
    return state;
  }



  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(NORTH_FENCE);
    builder.add(SOUTH_FENCE);
    builder.add(EAST_FENCE);
    builder.add(WEST_FENCE);
    builder.add(LIFTED);
    builder.add(BlockStateProperties.WATERLOGGED);
  }

  @Override
  public InteractionResult onWrenched(BlockState state, UseOnContext ctx) {
    BlockState result = state;

    Vec3 relative = ctx.getClickLocation().subtract(ctx.getClickedPos().getX(), ctx.getClickedPos().getY(), ctx.getClickedPos().getZ());
    if (relative.z > 0.66) {
      result = result.setValue(SOUTH_FENCE, !state.getValue(SOUTH_FENCE));
    } else if (relative.z < 0.33) {
      result = result.setValue(NORTH_FENCE, !state.getValue(NORTH_FENCE));
    }
    if (relative.x > 0.66) {
      result = result.setValue(EAST_FENCE,  !state.getValue(EAST_FENCE));
    } else if (relative.x < 0.33) {
      result = result.setValue(WEST_FENCE,  !state.getValue(WEST_FENCE));
    }
    // if we're near the center
    if (result.equals(state)) result = getRotatedBlockState(state, Direction.UP);

    ctx.getLevel().setBlock(ctx.getClickedPos(), result, 1 | 2);
    return InteractionResult.SUCCESS;
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
/*
  @Override
  public boolean placeLiquid (LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidstate) {
    if (!state.getValue(BlockStateProperties.WATERLOGGED) && (fluidstate.getType() == Fluids.WATER)) {
      if (!world.isClientSide()) {
        world.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, true), 1 | 2);
        world.getFluidTicks().schedule(new ScheduledTick<Fluid>(Fluids.WATER, pos, Fluids.WATER.getTickDelay(world), ));
      }
      return true;
    }
    return false;
  }

  @Override
  public Fluid takeLiquid (LevelAccessor world, BlockPos pos, BlockState state) {
    if (state.getValue(BlockStateProperties.WATERLOGGED)) {
      world.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, false), 1 | 2);
      return Fluids.WATER;
    }
    return Fluids.EMPTY;
  }
*/

  @Override
  public BlockState updateShape (BlockState state, Direction dir, BlockState neighbor, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
    if (state.getValue(BlockStateProperties.WATERLOGGED)) {
      world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    }
    if (isCatwalk(neighbor.getBlock())) {
      state = state.setValue(getPropertyFromDirection(dir), false);
    }
    return state;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
  }

  public static boolean isCatwalk (ItemStack test) {
    return (test.getItem() instanceof BlockItem) && isCatwalk(((BlockItem)test.getItem()).getBlock());
  }

  public static boolean isCatwalk (Block test) {
    return test instanceof CatwalkBlock;
  }

  public static boolean canPlaceCatwalk (Level world, BlockPos pos) {
    return world.getBlockState(pos).getMaterial().isReplaceable();
  }

  public static BooleanProperty getPropertyFromDirection (Direction dir) {
    switch (dir) {
      case NORTH: return NORTH_FENCE;
      case SOUTH: return SOUTH_FENCE;
      case EAST:  return EAST_FENCE;
      case WEST:  return WEST_FENCE;
      default:    return LIFTED;
    }
  }
  
  @Override
  public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
    return pState.getBlock() == pAdjacentBlockState.getBlock();
  }
}

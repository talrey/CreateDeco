package com.github.talrey.createdeco.blocks;

import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CatwalkBlock extends Block implements IWrenchable, IWaterLoggable {
  private static final VoxelShape VOXEL_BOTTOM = Block.makeCuboidShape(
    0d, 0d, 0d,
    16d, 2d, 16d
  );
  private static final VoxelShape VOXEL_NORTH = Block.makeCuboidShape(
  0d, 0d, 0d,
  16d, 16d, 2d
  );
  private static final VoxelShape VOXEL_SOUTH = Block.makeCuboidShape(
  0d, 0d, 14d,
  16d, 16d, 16d
  );
  private static final VoxelShape VOXEL_EAST = Block.makeCuboidShape(
  14d, 0d, 0d,
  16d, 16d, 16d
  );
  private static final VoxelShape VOXEL_WEST = Block.makeCuboidShape(
  0d, 0d, 0d,
  2d, 16d, 16d
  );

  public static final BooleanProperty NORTH_FENCE = BlockStateProperties.NORTH;
  public static final BooleanProperty SOUTH_FENCE = BlockStateProperties.SOUTH;
  public static final BooleanProperty EAST_FENCE  = BlockStateProperties.EAST;
  public static final BooleanProperty WEST_FENCE  = BlockStateProperties.WEST;
  public static final BooleanProperty LIFTED      = BlockStateProperties.BOTTOM;

  private static boolean hasNeighborTo (Direction side, BlockItemUseContext ctx) {
    return ctx.getWorld().getBlockState(ctx.getPos().offset(side)).getBlock() instanceof CatwalkBlock;
  }

  public CatwalkBlock (Properties props) {
    super(props);
    this.setDefaultState(this.getStateContainer().getBaseState()
      .with(NORTH_FENCE, false)
      .with(SOUTH_FENCE, false)
      .with(EAST_FENCE,  false)
      .with(WEST_FENCE,  false)
      .with(LIFTED,      true)
      .with(BlockStateProperties.WATERLOGGED, false)
    );
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
    return getRaytraceShape(state, reader, pos);
  }

  @Override
  public VoxelShape getRaytraceShape(BlockState state, IBlockReader world, BlockPos pos) {
    VoxelShape shape = VOXEL_BOTTOM;

    if (state.get(NORTH_FENCE)) shape = VoxelShapes.combine(shape, VOXEL_NORTH, IBooleanFunction.OR);
    if (state.get(SOUTH_FENCE)) shape = VoxelShapes.combine(shape, VOXEL_SOUTH, IBooleanFunction.OR);
    if (state.get(EAST_FENCE))  shape = VoxelShapes.combine(shape, VOXEL_EAST,  IBooleanFunction.OR);
    if (state.get(WEST_FENCE))  shape = VoxelShapes.combine(shape, VOXEL_WEST,  IBooleanFunction.OR);

    return shape.withOffset(0d, state.get(BlockStateProperties.BOTTOM) ? 0d : -2/16d, 0d);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockItemUseContext ctx) {
    Direction facing = ctx.getPlacementHorizontalFacing();
    FluidState fluid = ctx.getWorld().getFluidState(ctx.getPos());
    boolean lift     = (ctx.getHitVec().y - ctx.getPos().getY()) < 0.5f;

    BlockState state = getDefaultState()
      .with(LIFTED, lift)
      .with(NORTH_FENCE, (facing == Direction.NORTH) && !hasNeighborTo(Direction.NORTH, ctx))
      .with(SOUTH_FENCE, (facing == Direction.SOUTH) && !hasNeighborTo(Direction.SOUTH, ctx))
      .with(EAST_FENCE,  (facing == Direction.EAST)  && !hasNeighborTo(Direction.EAST,  ctx))
      .with(WEST_FENCE,  (facing == Direction.WEST)  && !hasNeighborTo(Direction.WEST,  ctx))
      .with(BlockStateProperties.WATERLOGGED, fluid.getFluid() == Fluids.WATER);

    if (!lift) {
      World world = ctx.getWorld();
      if (canPlaceCatwalk(world, ctx.getPos().add(0,1,0))) {
        world.setBlockState(ctx.getPos().add(0,1,0), state, 3);
        ctx.getPlayer().getHeldItem(ctx.getHand()).shrink(1);
        world.playSound(ctx.getPlayer(), ctx.getPos().add(0,1,0),
          SoundEvents.BLOCK_NETHERITE_BLOCK_PLACE, SoundCategory.BLOCKS, 1f, 1f
        );
        return world.getBlockState(ctx.getPos());
      }
    }
    return state;
  }

  @Override
  protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(NORTH_FENCE);
    builder.add(SOUTH_FENCE);
    builder.add(EAST_FENCE);
    builder.add(WEST_FENCE);
    builder.add(LIFTED);
    builder.add(BlockStateProperties.WATERLOGGED);
  }

  @Override
  public ActionResultType onWrenched(BlockState state, ItemUseContext ctx) {
    BlockState result = state;

    Vector3d relative = ctx.getHitVec().subtract(ctx.getPos().getX(), ctx.getPos().getY(), ctx.getPos().getZ());
    if (relative.z > 0.66) {
      result = result.with(SOUTH_FENCE, !state.get(SOUTH_FENCE));
    } else if (relative.z < 0.33) {
      result = result.with(NORTH_FENCE, !state.get(NORTH_FENCE));
    }
    if (relative.x > 0.66) {
      result = result.with(EAST_FENCE,  !state.get(EAST_FENCE));
    } else if (relative.x < 0.33) {
      result = result.with(WEST_FENCE,  !state.get(WEST_FENCE));
    }
    // if we're near the center
    if (result.equals(state)) result = getRotatedBlockState(state, Direction.UP);

    ctx.getWorld().setBlockState(ctx.getPos(), result, 1 | 2);
    return ActionResultType.SUCCESS;
  }

  @Override
  public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
    if (targetedFace.getAxis() == Direction.Axis.Y) {
      int state =
        (originalState.get(NORTH_FENCE) ? 8 : 0) +
        (originalState.get(EAST_FENCE)  ? 4 : 0) +
        (originalState.get(SOUTH_FENCE) ? 2 : 0) +
        (originalState.get(WEST_FENCE)  ? 1 : 0);
      return originalState
        .with(NORTH_FENCE, (state & 1) == 1)
        .with(EAST_FENCE,  (state & 8) == 8)
        .with(SOUTH_FENCE, (state & 4) == 4)
        .with(WEST_FENCE,  (state & 2) == 2);
    }
    return originalState;
  }

  @Override
  public boolean canContainFluid(IBlockReader world, BlockPos pos, BlockState state, Fluid fluid) {
    return !state.get(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
  }

  @Override
  public boolean receiveFluid(IWorld world, BlockPos pos, BlockState state, FluidState fluidstate) {
    if (!state.get(BlockStateProperties.WATERLOGGED) && (fluidstate.getFluid() == Fluids.WATER)) {
      if (!world.isRemote()) {
        world.setBlockState(pos, state.with(BlockStateProperties.WATERLOGGED, true), 1 | 2);
        world.getPendingFluidTicks().scheduleTick(pos, fluidstate.getFluid(), fluidstate.getFluid().getTickRate(world));
      }
      return true;
    }
    return false;
  }

  @Override
  public Fluid pickupFluid(IWorld world, BlockPos pos, BlockState state) {
    if (state.get(BlockStateProperties.WATERLOGGED)) {
      world.setBlockState(pos, state.with(BlockStateProperties.WATERLOGGED, false), 1 | 2);
      return Fluids.WATER;
    }
    return Fluids.EMPTY;
  }

  @Override
  public BlockState updatePostPlacement(BlockState state, Direction dir, BlockState neighbor, IWorld world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(BlockStateProperties.WATERLOGGED)) {
      world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    if (isCatwalk(neighbor.getBlock())) {
      state = state.with(getPropertyFromDirection(dir), false);
    }
    return state;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
  }

  public static boolean isCatwalk (ItemStack test) {
    return (test.getItem() instanceof BlockItem) && isCatwalk(((BlockItem)test.getItem()).getBlock());
  }

  public static boolean isCatwalk (Block test) {
    return test instanceof CatwalkBlock;
  }

  public static boolean canPlaceCatwalk (World world, BlockPos pos) {
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
}

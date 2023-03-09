package com.github.talrey.createdeco.blocks;

import com.simibubi.create.content.contraptions.relays.advanced.GantryShaftBlock;
import com.simibubi.create.foundation.utility.placement.IPlacementHelper;
import com.simibubi.create.foundation.utility.placement.PlacementHelpers;
import com.simibubi.create.foundation.utility.placement.PlacementOffset;
import com.simibubi.create.foundation.utility.placement.util.PoleHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SupportBlock extends DirectionalBlock implements SimpleWaterloggedBlock {
  private static final VoxelShape NORTH = Block.box(
    0d, 0d, 0d,
    16d, 16d, 2d
  );
  private static final VoxelShape SOUTH = Block.box(
    0d, 0d, 14d,
    16d, 16d, 16d
  );
  private static final VoxelShape EAST = Block.box(
    0d, 0d, 0d,
    2d, 16d, 16d
  );
  private static final VoxelShape WEST = Block.box(
    14d, 0d, 0d,
    16d, 16d, 16d
  );
  private static final VoxelShape UP = Block.box(
    0d, 14d, 0d,
    16d, 16d, 16d
  );
  private static final VoxelShape DOWN = Block.box(
    0d, 0d, 0d,
    16d, 2d, 16d
  );
  private static final VoxelShape X = Shapes.join(EAST,  WEST,  BooleanOp.OR);
  private static final VoxelShape Y = Shapes.join(UP,    DOWN,  BooleanOp.OR);
  private static final VoxelShape Z = Shapes.join(NORTH, SOUTH, BooleanOp.OR);

  private static final int placementHelperId = PlacementHelpers.register(new SupportBlock.PlacementHelper());

  public SupportBlock (Properties props) {
    super(props);
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
    builder.add(BlockStateProperties.WATERLOGGED);
  }

  @Override
  public InteractionResult use(
    BlockState state, Level world, BlockPos pos, Player player,
    InteractionHand hand, BlockHitResult ray
  ) {
    ItemStack heldItem = player.getItemInHand(hand);

    IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
    if (!placementHelper.matchesItem(heldItem))
      return InteractionResult.PASS;

    return placementHelper.getOffset(player, world, state, pos, ray)
      .placeInWorld(world, ((BlockItem) heldItem.getItem()), player, hand, ray);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
    BlockState result = defaultBlockState()
      .setValue(FACING,ctx.getClickedFace())
      .setValue(BlockStateProperties.WATERLOGGED, fluid.is(Fluids.WATER));
    return result;
  }

  @Override
  public boolean canPlaceLiquid (BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
    return !state.getValue(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
  }

  @Override
  public FluidState getFluidState (BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
  }

  @Override
  public VoxelShape getVisualShape (BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
    return Shapes.empty();
  }

  @Override
  public VoxelShape getShape (BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    return switch (state.getValue(FACING).getAxis()) {
      case X  -> Shapes.join(Y,Z, BooleanOp.OR);
      case Z  -> Shapes.join(X,Y, BooleanOp.OR);
      default -> Shapes.join(X,Z, BooleanOp.OR);
    };
  }

  public static boolean isSupportBlock (ItemStack test) {
    return (test.getItem() instanceof BlockItem)
      && isSupportBlock(((BlockItem)test.getItem()).getBlock());
  }

  public static boolean isSupportBlock (Block test) {
    return test instanceof SupportBlock;
  }

  @MethodsReturnNonnullByDefault
  private static class PlacementHelper extends PoleHelper<Direction> {
    public PlacementHelper() {
      super(state -> SupportBlock.isSupportBlock(state.getBlock()),
        state -> state.getValue(SupportBlock.FACING).getAxis(), SupportBlock.FACING
      );
    }

    @Override
    public Predicate<ItemStack> getItemPredicate () {
      return (Predicate<ItemStack>) SupportBlock::isSupportBlock;
    }

    @Override
    public Predicate<BlockState> getStatePredicate () {
      return state -> SupportBlock.isSupportBlock(state.getBlock());
    }

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                     BlockHitResult ray) {
      PlacementOffset offset = super.getOffset(player, world, state, pos, ray);
      offset.withTransform(offset.getTransform());
      return offset;
    }
  }
}

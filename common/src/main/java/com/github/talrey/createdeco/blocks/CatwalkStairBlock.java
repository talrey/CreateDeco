package com.github.talrey.createdeco.blocks;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
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

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class CatwalkStairBlock  extends Block implements IWrenchable, SimpleWaterloggedBlock {

  private static final VoxelShape BOX_NORTH = Shapes.join(
    Block.box(0d, 14d, 8d, 16d, 16d, 16d),
    Block.box(0d, 6d, 0d, 16d, 8d, 8d),
    BooleanOp.OR
  );
  private static final VoxelShape BOX_SOUTH = Shapes.join(
    Block.box(0d, 14d, 0d, 16d, 16d, 8d),
    Block.box(0d, 6d, 8d, 16d, 8d, 16d),
    BooleanOp.OR
  );
  private static final VoxelShape BOX_WEST = Shapes.join(
    Block.box(8d, 14d, 0d, 16d, 16d, 16d),
    Block.box(0d, 6d, 0d, 8d, 8d, 16d),
    BooleanOp.OR
  );
  private static final VoxelShape BOX_EAST = Shapes.join(
    Block.box(0d, 14d, 0d, 8d, 16d, 16),
    Block.box(8d, 6d, 0d, 16d, 8d, 16d),
    BooleanOp.OR
  );

  private static final int placementHelperId = PlacementHelpers.register(new PlacementHelper());

  public CatwalkStairBlock (Properties props) {
    super(props);
    this.registerDefaultState(this.defaultBlockState()
      .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
      .setValue(BlockStateProperties.WATERLOGGED, false)
    );
  }

  @Override
  public InteractionResult use (
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
  }

  @Override
  public boolean canPlaceLiquid (BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
    return !state.getValue(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
  }

  @Override
  public VoxelShape getShape (BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
    return switch(state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
      case SOUTH -> BOX_SOUTH;
      case EAST  -> BOX_EAST;
      case WEST  -> BOX_WEST;
      default    -> BOX_NORTH;
    };
  }

  public static boolean isCatwalkStair (ItemStack test) {
    return (test.getItem() instanceof BlockItem)
      && isCatwalkStair(((BlockItem)test.getItem()).getBlock());
  }

  public static boolean isCatwalkStair (Block test) {
    return test instanceof CatwalkStairBlock;
  }


  @MethodsReturnNonnullByDefault
  private static class PlacementHelper implements IPlacementHelper {

    @Override
    public Predicate<ItemStack> getItemPredicate () {
      return (Predicate<ItemStack>) CatwalkStairBlock::isCatwalkStair;
    }

    @Override
    public Predicate<BlockState> getStatePredicate () {
      return state -> CatwalkStairBlock.isCatwalkStair(state.getBlock());
    }

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos,
                                     BlockHitResult ray) {
      Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
      PlacementOffset offset = PlacementOffset.success(pos.relative(facing).offset(0, 1, 0));
      return offset.withTransform(s->s.setValue(BlockStateProperties.HORIZONTAL_FACING,
        state.getValue(BlockStateProperties.HORIZONTAL_FACING)
      ));
    }
  }
}

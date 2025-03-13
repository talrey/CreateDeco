package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.blocks.CatwalkBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.function.Predicate;

public class CatwalkBlockItem extends BlockItem {
  private final int extensionPlacementHelperID;
  private final int catwalkPlacementHelperID;

  public CatwalkBlockItem (CatwalkBlock block, Properties props) {
    super(block, props);
    extensionPlacementHelperID = PlacementHelpers.register(new CatwalkExtensionHelper());
    catwalkPlacementHelperID = PlacementHelpers.register(new CatwalkPlacementHelper());
  }

  @Override
  public InteractionResult useOn (UseOnContext ctx) {
    BlockPos pos   = ctx.getClickedPos();
    Direction face = ctx.getClickedFace();
    Level world    = ctx.getLevel();
    Player player  = ctx.getPlayer();

    BlockState state        = world.getBlockState(pos);
    IPlacementHelper extension_helper = PlacementHelpers.get(extensionPlacementHelperID);
    IPlacementHelper placement_helper = PlacementHelpers.get(catwalkPlacementHelperID);
    BlockHitResult ray = new BlockHitResult(ctx.getClickLocation(), face, pos, true);
    if (extension_helper.matchesState(state) && player != null) {
      return extension_helper.getOffset(player, world, state, pos, ray).placeInWorld(world, this, player, ctx.getHand(), ray);
    } else if (placement_helper.matchesState(state) && player != null) {
      // This offset is only used to produce the ghost state, we don't use
      // it to actually place the block.
      PlacementOffset offset = placement_helper.getOffset(player, world, state, pos, ray);

      // Update the block state and consume a catwalk item
      state = state.setValue(CatwalkBlock.CATWALK_BOTTOM, true);
      world.setBlock(pos, state, 3);
      ItemStack stack = ctx.getItemInHand();
      if (!player.getAbilities().instabuild) {
	stack.shrink(1);
      }

      return InteractionResult.SUCCESS;
    }
    return super.useOn(ctx);
  }

  /*
    There are two placement helpers below. The first one is used to extend
    catwalks horizontally. It applies to any catwalk blocks that have a catwalk
    (top or bottom). When extending the catwalk, we copy the catwalk position,
    so top catwalks will extend to create more top catwalks, and likewise for
    bottom catwalks.

    The second placement helper is used to place a bottom catwalk in a catwalk
    block that only has railing items.
   */

  @MethodsReturnNonnullByDefault
  public static class CatwalkExtensionHelper implements IPlacementHelper {
    @Override
    public Predicate<ItemStack> getItemPredicate () {
      return CatwalkBlock::isCatwalk;
    }

    // We only want to use this helper if the catwalk block has
    // a catwalk (top or bottom). This way, this helper is mutually
    // exclusive with the catwalk placement helper below.
    @Override
    public Predicate<BlockState> getStatePredicate () {
      return state -> CatwalkBlock.isCatwalk(state.getBlock()) &&
	CatwalkBlock.hasAnyCatwalks(state);
    }

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
      // The new block should have a catwalk in the same position as
      // this block (so we copy that property), and no railings, because
      // those need to be placed separately
      BlockState newState = state
	.setValue(CatwalkBlock.RAILING_NORTH, false)
	.setValue(CatwalkBlock.RAILING_SOUTH, false)
	.setValue(CatwalkBlock.RAILING_EAST, false)
	.setValue(CatwalkBlock.RAILING_WEST, false);

      Direction face = ray.getDirection();
      if (face.getAxis() != Direction.Axis.Y) {
        return PlacementOffset.success(pos.offset(face.getNormal()), offsetState -> newState);
      }
      List<Direction> dirs = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), Direction.Axis.Y);
      for (Direction dir : dirs) {
        BlockPos newPos = pos.relative(dir);
        if (!CatwalkBlock.canPlaceCatwalk(world, newPos)) continue;
        return PlacementOffset.success(newPos, offsetState -> newState);
      }
      return PlacementOffset.fail();
    }
  }

  /**
     This helper is used to place catwalks into catwalk blocks that only
     contain railing items.
   **/
  @MethodsReturnNonnullByDefault
  public static class CatwalkPlacementHelper implements IPlacementHelper {
    @Override
    public Predicate<ItemStack> getItemPredicate () {
      return CatwalkBlock::isCatwalk;
    }

    // We only want to apply this helper if the block doesn't have any
    // catwalks (top or bottom) in it. This way, the helper is mutually
    // exclusive with the catwalk extension helper above.
    @Override
    public Predicate<BlockState> getStatePredicate () {
      return state ->
	CatwalkBlock.isCatwalk(state.getBlock()) &&
	!CatwalkBlock.hasAnyCatwalks(state);
    }

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
      // The ghost state should only contain the bottom catwalk
      BlockState ghost_state = state
	.setValue(CatwalkBlock.CATWALK_TOP, false)
	.setValue(CatwalkBlock.CATWALK_BOTTOM, true)
	.setValue(CatwalkBlock.RAILING_NORTH, false)
	.setValue(CatwalkBlock.RAILING_SOUTH, false)
	.setValue(CatwalkBlock.RAILING_EAST, false)
	.setValue(CatwalkBlock.RAILING_WEST, false);

      return PlacementOffset.success(pos, offsetState -> ghost_state);
    }
  }
}

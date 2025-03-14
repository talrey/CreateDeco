package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.blocks.CatwalkBlock;
import com.github.talrey.createdeco.blocks.CatwalkRailingBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
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
  private final int catwalkReplacementHelperID;

  public CatwalkBlockItem (CatwalkBlock block, Properties props) {
    super(block, props);
    extensionPlacementHelperID = PlacementHelpers.register(new CatwalkExtensionHelper());
    catwalkPlacementHelperID = PlacementHelpers.register(new CatwalkPlacementHelper());
    catwalkReplacementHelperID = PlacementHelpers.register(new CatwalkReplacementHelper());
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
    IPlacementHelper replacement_helper = PlacementHelpers.get(catwalkReplacementHelperID);
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
    } else if(replacement_helper.matchesState(state) && player != null) {
      PlacementOffset offset = replacement_helper.getOffset(player, world, state, pos, ray);
      if (offset.isSuccessful()) {
        // The placeInWorld function has code to first check whether the current
        // block can be replaced. That's not the case for catwalk railings, so
        // we briefly replace the block with air, and then replace it with the
        // catwalk block.
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
        return offset.placeInWorld(world, this, player, ctx.getHand(), ray);
      }
    }
    return super.useOn(ctx);
  }

  /*
    There are three placement helpers below. The first one is used to extend
    catwalks horizontally. It applies to any catwalk blocks that have a catwalk
    (top or bottom). When extending the catwalk, we copy the catwalk position,
    so top catwalks will extend to create more top catwalks, and likewise for
    bottom catwalks.

    The second placement helper is used to place a bottom catwalk in a catwalk
    block that only has railing items.

    The third one is used to replace CatwalkRailingBlocks with CatwalkBlocks
    when interacting with a catwalk item on a railing block.
   */

  @MethodsReturnNonnullByDefault
  public class CatwalkExtensionHelper implements IPlacementHelper {
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
        state.getBlock() instanceof CatwalkBlock catblock &&
        CatwalkBlock.hasAnyCatwalks(state) &&
        // We also check that the metal of the item matches the metal of the
        // catwalk block.
        CatwalkBlockItem.this.getBlock().equals(
          BlockRegistry.CATWALKS.get(catblock.metal).get()
        );
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
  public class CatwalkPlacementHelper implements IPlacementHelper {
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
        // Check that the metal type of the block is the same as the
        // block that corresponds to this item
        (
          state.getBlock() instanceof CatwalkBlock catblock &&
          CatwalkBlockItem.this.getBlock() instanceof CatwalkBlock thiscatblock &&
          catblock.metal == thiscatblock.metal
        ) &&
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

  /**
     This helper is used to replace railing blocks with catwalk blocks when the player interacts with a legacy railing block.
     This allows seamlessly replacing legacy blocks with catwalk blocks, without needing to breaking and replacing the block.
   **/
  @MethodsReturnNonnullByDefault
  public class CatwalkReplacementHelper implements IPlacementHelper {
    @Override
    public Predicate<ItemStack> getItemPredicate () {
      return CatwalkBlock::isCatwalk;
    }

    @Override
    public Predicate<BlockState> getStatePredicate () {
      return state -> CatwalkRailingBlock.isRailing(state.getBlock()) &&
        // Check that the metal type of the block is the same as the
        // block that corresponds to this item
        (
          state.getBlock() instanceof CatwalkRailingBlock railblock &&
          CatwalkBlockItem.this.getBlock() instanceof CatwalkBlock thiscatblock &&
          railblock.metal == thiscatblock.metal
        );
    }

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
      // The ghost state should only contain the bottom catwalk

      if (state.getBlock() instanceof CatwalkRailingBlock catrail) {
	String metal = catrail.metal;
	// The ghost state only shows the added catwal
	BlockState ghost_state = BlockRegistry.CATWALKS.get(metal).get()
	  .defaultBlockState()
	  .setValue(CatwalkBlock.CATWALK_BOTTOM, true);

	// But the updated state contains the new catwalk as well as any existing railings
	BlockState new_state = BlockRegistry.CATWALKS.get(metal).get()
	  .defaultBlockState()
	  .setValue(CatwalkBlock.CATWALK_TOP, false)
	  .setValue(CatwalkBlock.CATWALK_BOTTOM, true)
	  .setValue(CatwalkBlock.RAILING_NORTH, state.getValue(CatwalkRailingBlock.NORTH_FENCE))
	  .setValue(CatwalkBlock.RAILING_SOUTH, state.getValue(CatwalkRailingBlock.SOUTH_FENCE))
	  .setValue(CatwalkBlock.RAILING_EAST, state.getValue(CatwalkRailingBlock.EAST_FENCE))
	  .setValue(CatwalkBlock.RAILING_WEST, state.getValue(CatwalkRailingBlock.WEST_FENCE));

        return PlacementOffset.success(pos, offsetState -> new_state).withGhostState(ghost_state);
      }
      return PlacementOffset.fail();
    }
  }
}

package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.blocks.CatwalkRailingBlock;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.function.Predicate;

public class RailingBlockItem extends BlockItem {
  private final int placementHelperID;

  public RailingBlockItem (CatwalkRailingBlock block, Properties props) {
    super(block, props);
    placementHelperID = PlacementHelpers.register(new RailingHelper());
  }

  @Override
  public InteractionResult useOn (UseOnContext ctx) {
    BlockPos pos    = ctx.getClickedPos();
    Direction face  = ctx.getClickedFace();
    Level level     = ctx.getLevel();
    Player player   = ctx.getPlayer();
    ItemStack stack = ctx.getItemInHand();

    BlockState state        = level.getBlockState(pos);
    IPlacementHelper helper = PlacementHelpers.get(placementHelperID);
    BlockHitResult ray = new BlockHitResult(ctx.getClickLocation(), face, pos, true);
    boolean railMatchTest = stack.getItem() == state.getBlock().asItem();

    if (player == null) return InteractionResult.PASS;

    PlacementOffset offset = null;
    if (helper.matchesState(state)) {
      offset = helper.getOffset(player, level, state, pos, ray);
      //return offset.placeInWorld(world, this, player, ctx.getHand(), ray);
    }

    if (offset != null && offset.isSuccessful() && !player.isShiftKeyDown() //&& railMatchTest
    ) {
      state = offset.getGhostState(); //level.getBlockState(offset.getBlockPos());
      var offsetPos = offset.getBlockPos();
      var soundType = state.getSoundType();

      level.setBlock(offsetPos, offset.getTransform().apply(state), 3);
      level.playSound(player, offsetPos, this.getPlaceSound(state), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
      level.gameEvent(GameEvent.BLOCK_PLACE, offsetPos, GameEvent.Context.of(player, state));
      if (!player.getAbilities().instabuild) {
        stack.shrink(1);
      }
      return InteractionResult.SUCCESS;
    }

    return super.useOn(ctx);
  }

  @MethodsReturnNonnullByDefault
  public static class RailingHelper implements IPlacementHelper {
    @Override
    public Predicate<ItemStack> getItemPredicate () {
      return CatwalkRailingBlock::isRailing;
    }

    @Override
    public Predicate<BlockState> getStatePredicate () {
      return state -> true; //CatwalkRailingBlock.isRailing(state.getBlock());
    }

    @Override
    public PlacementOffset getOffset (
      Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray
    ) {
      Direction face = ray.getDirection();
      BlockState adjacent = world.getBlockState(pos.relative(face));
      if (CatwalkRailingBlock.isRailing(adjacent.getBlock())) {
        pos = pos.relative(face);
        state = adjacent;
      }

      boolean railMatchTest = player.isHolding(state.getBlock().asItem());

      if (!CatwalkRailingBlock.isRailing(state.getBlock()) ||
              (state.getValue(CatwalkRailingBlock.NORTH_FENCE)
              && state.getValue(CatwalkRailingBlock.SOUTH_FENCE)
              && state.getValue(CatwalkRailingBlock.EAST_FENCE)
              && state.getValue(CatwalkRailingBlock.WEST_FENCE)) || !railMatchTest) {
        return PlacementOffset.fail();
      }

      List<Direction> dirs = IPlacementHelper.orderedByDistanceExceptAxis(
         pos, ray.getLocation(), Direction.Axis.Y
      );
      for (Direction offset : dirs) {
        if (!state.getValue(CatwalkRailingBlock.fromDirection(offset))) {
          state = state.setValue(CatwalkRailingBlock.fromDirection(offset), true);
          break;
        }
      }

      BlockState finalState = state;
      return PlacementOffset.success(pos, newState -> finalState).withGhostState(finalState);
    }
  }
}

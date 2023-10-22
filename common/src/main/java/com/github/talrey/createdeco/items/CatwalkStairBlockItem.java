package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.blocks.CatwalkBlock;
import com.github.talrey.createdeco.blocks.CatwalkStairBlock;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.function.Predicate;

public class CatwalkStairBlockItem extends BlockItem {
  private final int placementHelperID;

  public CatwalkStairBlockItem (CatwalkStairBlock block, Properties props) {
    super(block, props);
    placementHelperID = PlacementHelpers.register(new CatwalkStairBlockItem.CatwalkHelper());
  }

  @Override
  public InteractionResult useOn (UseOnContext ctx) {
    BlockPos pos   = ctx.getClickedPos();
    Direction face = ctx.getClickedFace();
    Level world    = ctx.getLevel();
    Player player  = ctx.getPlayer();

    BlockState state        = world.getBlockState(pos);
    IPlacementHelper helper = PlacementHelpers.get(placementHelperID);
    BlockHitResult ray = new BlockHitResult(ctx.getClickLocation(), face, pos, true);
    if (helper.matchesState(state) && player != null && !player.isShiftKeyDown()) {
      return helper.getOffset(player, world, state, pos, ray)
        .placeInWorld(world, this, player, ctx.getHand(), ray);
    }
    return super.useOn(ctx);
  }

  @MethodsReturnNonnullByDefault
  public static class CatwalkHelper implements IPlacementHelper {
    @Override
    public Predicate<ItemStack> getItemPredicate () {
      return CatwalkStairBlock::isCatwalkStair;
    }

    @Override
    public Predicate<BlockState> getStatePredicate () {
      return state -> CatwalkStairBlock.isCatwalkStair(state.getBlock());
    }

    @Override
    public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
//      if (face.getAxis() != Direction.Axis.Y) {
//        return PlacementOffset.success(pos.offset(face.getNormal()), offsetState -> offsetState);
//      }
      List<Direction> dirs = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(), Direction.Axis.Y);
      for (Direction dir : dirs) {
        Direction facing;
        if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
          facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
          if (dir.getOpposite() != facing) continue;
        }
        else facing = dir.getOpposite();

        BlockPos newPos = pos.relative(dir).offset(0, 1, 0);
        if (!CatwalkBlock.canPlaceCatwalk(world, newPos)) continue;

        return PlacementOffset.success(newPos,
          offsetState -> {
          // not entirely sure why this is necessary tbh, but if it prevents crashes aight then.
            if (offsetState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
              offsetState = offsetState.setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
            }
            return offsetState;
          }
        );
      }
      return PlacementOffset.fail();
    }


  }
}

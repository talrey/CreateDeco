package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.blocks.CatwalkBlock;
import com.simibubi.create.foundation.utility.placement.IPlacementHelper;
import com.simibubi.create.foundation.utility.placement.PlacementHelpers;
import com.simibubi.create.foundation.utility.placement.PlacementOffset;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class CatwalkBlockItem extends BlockItem {
  private final int placementHelperID;

  public CatwalkBlockItem (CatwalkBlock block, Properties props) {
    super(block, props);
    placementHelperID = PlacementHelpers.register(new CatwalkHelper());
  }

  @Override
  public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext ctx) {
    BlockPos pos   = ctx.getPos();
    Direction face = ctx.getFace();
    World world    = ctx.getWorld();

    PlayerEntity player     = ctx.getPlayer();
    BlockState state        = world.getBlockState(pos);
    IPlacementHelper helper = PlacementHelpers.get(placementHelperID);
    BlockRayTraceResult ray = new BlockRayTraceResult(ctx.getHitVec(), face, pos, true);
    if (helper.matchesState(state) && player != null) {
      return helper.getOffset(player, world, state, pos, ray).placeInWorld(world, this, player, ctx.getHand(), ray);
    }
    return super.onItemUseFirst(stack, ctx);
  }

  @MethodsReturnNonnullByDefault
  public static class CatwalkHelper implements IPlacementHelper {
    @Override
    public Predicate<ItemStack> getItemPredicate () {
      return (Predicate<ItemStack>) CatwalkBlock::isCatwalk;
    }

    @Override
    public Predicate<BlockState> getStatePredicate () {
      return state -> CatwalkBlock.isCatwalk(state.getBlock());
    }

    @Override
    public PlacementOffset getOffset(PlayerEntity player, World world, BlockState state, BlockPos pos, BlockRayTraceResult ray) {
      Direction face = ray.getFace();
      if (face.getAxis() != Direction.Axis.Y) {
        return PlacementOffset.success(pos.offset(face), offsetState -> offsetState
          .with(CatwalkBlock.LIFTED, state.get(CatwalkBlock.LIFTED))
          .with(CatwalkBlock.getPropertyFromDirection(face), !CatwalkBlock.isCatwalk(world.getBlockState(pos.offset(face).offset(face)).getBlock()))
          .with(CatwalkBlock.getPropertyFromDirection(face.getOpposite()), false)
        );
      }
      List<Direction> dirs = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getHitVec(), Direction.Axis.Y);
      for (Direction dir : dirs) {
        BlockPos newPos = pos.offset(dir);
        if (!CatwalkBlock.canPlaceCatwalk(world, newPos)) continue;
        return PlacementOffset.success(newPos, offsetState -> offsetState
          .with(CatwalkBlock.LIFTED, state.get(CatwalkBlock.LIFTED))
          .with(CatwalkBlock.getPropertyFromDirection(dir), !CatwalkBlock.isCatwalk(world.getBlockState(newPos.offset(dir)).getBlock()))
          .with(CatwalkBlock.getPropertyFromDirection(dir.getOpposite()), false)
        );
      }
      return PlacementOffset.fail();
    }
  }
}

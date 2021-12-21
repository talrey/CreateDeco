package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.Registration;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CoinStackItem extends Item {
  public CoinStackItem (Properties props) {
    super(props);
  }

  protected boolean placeBlock(UseOnContext ctx) {
    BlockPlaceContext bictx = new BlockPlaceContext (ctx);
    BlockState target = ctx.getLevel().getBlockState(ctx.getClickedPos());
    String targetName = target.getBlock().getDescriptionId();
    String thisName   = this.getDescriptionId().substring(this.getDescriptionId().lastIndexOf(".")+1).replace("_coinstack","");
    int found = targetName.indexOf("_coinstack_block");
    if (found > 0) {
      int start = targetName.lastIndexOf(".");
      targetName = targetName.substring(start+1,found);
    }
    if (found > 0 && targetName.equals(thisName) && target.getValue(BlockStateProperties.LAYERS) < 8) {
      int height = target.getValue(BlockStateProperties.LAYERS);
      if (!ctx.getLevel().isClientSide()) ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), Registration.COIN_BLOCKS.get(thisName).getDefaultState()
        .setValue(BlockStateProperties.LAYERS, height+1)
      );
      return true;
    }
    else if (target.canBeReplaced(bictx)) {
      if (!ctx.getLevel().isClientSide()) ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), Registration.COIN_BLOCKS.get(thisName).getDefaultState());
      return true;
    }
    else {
      target = ctx.getLevel().getBlockState(ctx.getClickedPos().offset(ctx.getClickedFace().getNormal()));
      if (target.canBeReplaced(bictx)) {
        if (!ctx.getLevel().isClientSide()) ctx.getLevel().setBlockAndUpdate(
          ctx.getClickedPos().offset(ctx.getClickedFace().getNormal()), Registration.COIN_BLOCKS.get(thisName).getDefaultState()
        );
        return true;
      }
    }
    return false;
  }


  @Override
  public InteractionResult useOn (UseOnContext ctx) {
    if (placeBlock (ctx)) {
      ctx.getItemInHand().shrink(1);
      return InteractionResult.SUCCESS;
    }
    return super.useOn(ctx);
  }
}

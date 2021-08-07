package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;

public class CoinStackItem extends Item {
  public CoinStackItem (Properties props) {
    super(props);
  }

  protected boolean placeBlock(ItemUseContext ctx) {
    BlockItemUseContext bictx = new BlockItemUseContext (ctx);
    BlockState target = ctx.getWorld().getBlockState(ctx.getPos());
    String targetName = target.getBlock().getTranslationKey();
    String thisName   = this.getTranslationKey().substring(this.getTranslationKey().lastIndexOf(".")+1).replace("_coinstack","");
    int found = targetName.indexOf("_coinstack_block");
    if (found > 0) {
      int start = targetName.lastIndexOf(".");
      targetName = targetName.substring(start+1,found);
    }
    if (found > 0 && targetName.equals(thisName) && target.get(BlockStateProperties.LAYERS_1_8) < 8) {
      int height = target.get(BlockStateProperties.LAYERS_1_8);
      if (!ctx.getWorld().isRemote()) ctx.getWorld().setBlockState(ctx.getPos(), Registration.COIN_BLOCKS.get(thisName).getDefaultState()
        .with(BlockStateProperties.LAYERS_1_8, height+1)
      );
      return true;
    }
    else if (target.isReplaceable(bictx)) {
      if (!ctx.getWorld().isRemote()) ctx.getWorld().setBlockState(ctx.getPos(), Registration.COIN_BLOCKS.get(thisName).getDefaultState());
      return true;
    }
    else {
      target = ctx.getWorld().getBlockState(ctx.getPos().add(ctx.getFace().getDirectionVec()));
      if (target.isReplaceable(bictx)) {
        if (!ctx.getWorld().isRemote()) ctx.getWorld().setBlockState(
          ctx.getPos().add(ctx.getFace().getDirectionVec()), Registration.COIN_BLOCKS.get(thisName).getDefaultState()
        );
        return true;
      }
    }
    return false;
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext ctx) {
    if (placeBlock (ctx)) {
      ctx.getItem().shrink(1);
      return ActionResultType.SUCCESS;
    }
    return super.onItemUse(ctx);
  }
}

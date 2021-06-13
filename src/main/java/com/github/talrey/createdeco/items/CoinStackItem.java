package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import org.apache.logging.log4j.LogManager;

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
    //LogManager.getLogger(CreateDecoMod.MODID).debug(target.getBlock().getTranslationKey());
    //LogManager.getLogger(CreateDecoMod.MODID).debug(this.getTranslationKey());
    if (found > 0 && targetName.equals(thisName)) {
      //LogManager.getLogger(CreateDecoMod.MODID).debug("found stack of " + thisName);
      int height = ctx.getWorld().getBlockState(ctx.getPos()).get(BlockStateProperties.LAYERS_1_8);
      if (height < 8) {
        if (!ctx.getWorld().isRemote()) ctx.getWorld().setBlockState(ctx.getPos(), Registration.COIN_BLOCKS.get(thisName).getDefaultState()
          .with(BlockStateProperties.LAYERS_1_8, height+1)
        );
        return true;
      }
    }
    else if (target.isReplaceable(bictx)) {
      //LogManager.getLogger(CreateDecoMod.MODID).debug("target is replaceable");
      if (!ctx.getWorld().isRemote()) ctx.getWorld().setBlockState(ctx.getPos(), Registration.COIN_BLOCKS.get(thisName).getDefaultState());
      return true;
    }
    else {
      target = ctx.getWorld().getBlockState(ctx.getPos().subtract(ctx.getPos().add(ctx.getFace().getDirectionVec())));
      if (target.isReplaceable(bictx)) {
        //LogManager.getLogger(CreateDecoMod.MODID).debug("target-adjacent is replaceable");
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
      //LogManager.getLogger(CreateDecoMod.MODID).debug("success, decrementing stack of " + ctx.getItem().getCount());
      ctx.getItem().shrink(1);
      //LogManager.getLogger(CreateDecoMod.MODID).debug("now at " + ctx.getItem().getCount());
      return ActionResultType.SUCCESS;
    }
    return super.onItemUse(ctx);
  }
}

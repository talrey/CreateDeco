package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.CreateDecoMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import org.apache.logging.log4j.LogManager;

public class CoinStackItem extends Item {
  public CoinStackItem (Properties props) {
    super(props);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext ctx) {
    LogManager.getLogger(CreateDecoMod.MODID).debug(ctx.getWorld().getBlockState(ctx.getPos()).getBlock().getName().toString());
    return super.onItemUse(ctx);
  }
}

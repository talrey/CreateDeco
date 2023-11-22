package com.github.talrey.createdeco.fabric;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LoaderUtilImpl {
  public static int getSignal (IBE<?> be, BlockState pState, Level pLevel, BlockPos pPos) {
    return be.getBlockEntityOptional(pLevel, pPos)
      .filter(vte->!Transaction.isOpen()) // fabric: hack fix for comparators updating when they shouldn't
      .map(vte->{
        if (vte instanceof SidedStorageBlockEntity sided) return sided.getItemStorage(null);
        return null;
      })
      .map(ItemHelper::calcRedstoneFromInventory)
      .orElse(0);
  }

  public static boolean checkPlacingNbt (BlockPlaceContext ctx) {
    ItemStack item = ctx.getItemInHand();
    return BlockItem.getBlockEntityData(item) != null;
  }
}

package com.github.talrey.createdeco.forge;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class LoaderUtilImpl {
  public static int getSignal (IBE<?> be, BlockState pState, Level pLevel, BlockPos pPos) {
    return be.getBlockEntityOptional(pLevel, pPos).map((vte) ->
      vte.getCapability(ForgeCapabilities.ITEM_HANDLER)
    ).map((lo) ->
      lo.map(ItemHelper::calcRedstoneFromInventory).orElse(0)
    ).orElse(0);
  }
}

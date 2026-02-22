package com.github.talrey.createdeco.mixin;

import com.github.talrey.createdeco.blocks.ShippingContainerBlock;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemVaultBlockEntity.class)
public class ItemVaultBlockEntityMixin {
  @ModifyArg(
    method = "initCapability()V",
    at = @At(
      value = "INVOKE",
      target = "Lcom/simibubi/create/api/connectivity/ConnectivityHandler;partAt(Lnet/minecraft/world/level/block/entity/BlockEntityType;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"
    ),
    index = 0
  )
  public BlockEntityType<?> initCapability (BlockEntityType<?> type) {

    if ((Object)this instanceof ShippingContainerBlock.Entity container) {
//      CreateDecoMod.LOGGER.info("Injected: " +
//        ((ShippingContainerBlock)container.getBlockState().getBlock()).COLOR
//      );
      return ((ShippingContainerBlock)container.getBlockState().getBlock()).getBlockEntityType();
    }
    return type;
  }
}

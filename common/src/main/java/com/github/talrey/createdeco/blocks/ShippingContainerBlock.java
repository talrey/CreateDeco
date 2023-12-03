package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.BlockRegistry;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class ShippingContainerBlock extends ItemVaultBlock {
  public final DyeColor COLOR;

  public ShippingContainerBlock (Properties properties, DyeColor color) {
    super(properties);
    COLOR = color;
  }

  public static DyeColor getColor (BlockState state) {
    if (state.getBlock() instanceof ShippingContainerBlock scb) {
      return scb.COLOR;
    }
    return DyeColor.BLUE;
  }

  public boolean isSameType (BlockState other) {
    return (other.getBlock() instanceof ShippingContainerBlock container)
      && (container.COLOR == this.COLOR);
  }

  public static boolean isVault (BlockState state) {
    return (state.getBlock() instanceof ShippingContainerBlock);
  }

  public static boolean isLarge(BlockState state) {
    if (!isVault(state))
      return false;
    return state.getValue(LARGE);
  }

  @Nullable
  public static Direction.Axis getVaultBlockAxis (BlockState state) {
    if (!isVault(state))
      return null;
    return state.getValue(HORIZONTAL_AXIS);
  }

  // Vaults are less noisy when placed in batch
  public static final SoundType SILENCED_METAL =
    new SoundType(0.1F, 1.5F,
      SoundEvents.NETHERITE_BLOCK_BREAK, SoundEvents.NETHERITE_BLOCK_STEP,
      SoundEvents.NETHERITE_BLOCK_PLACE, SoundEvents.NETHERITE_BLOCK_HIT,
      SoundEvents.NETHERITE_BLOCK_FALL
    );

//    @Override
//    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, net.minecraft.world.entity.Entity entity) {
//        SoundType soundType = getSoundType(state);
//        if (entity != null)
//            return SILENCED_METAL;
//        return soundType;
//    }

  @Override
  public BlockEntityType<? extends ItemVaultBlockEntity> getBlockEntityType() {
    return BlockRegistry.CONTAINER_ENTITIES.get(COLOR).get();
  }

  public static class Entity extends ItemVaultBlockEntity {
    public Entity (BlockEntityType<?> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
    }

    @Override
    public Entity getControllerBE () {
      BlockEntity be = super.getControllerBE();
      if (be instanceof Entity container) return container;
      return null;
    }

    @Override
    protected void updateConnectivity() {
      updateConnectivity = false;
      if (level.isClientSide())
        return;
      if (!isController())
        return;
      ConnectivityHandler.formMulti(this);
    }

    @Override
    public void notifyMultiUpdated() {
      BlockState state = this.getBlockState();
      if (isVault(state)) { // safety
        level.setBlock(getBlockPos(), state.setValue(ItemVaultBlock.LARGE, radius > 2), 6);
      }
      super.notifyMultiUpdated();
    }

    @Override
    public void removeController (boolean keepContents) {
      super.removeController(keepContents);
      BlockState state = getBlockState();
      state = state.setValue(ItemVaultBlock.LARGE, false);
      getLevel().setBlock(worldPosition, state, 22);
    }
  }
}


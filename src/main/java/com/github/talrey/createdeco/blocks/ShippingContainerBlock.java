package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.BlockRegistry;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllMountedStorageTypes;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.api.packager.InventoryIdentifier;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import com.simibubi.create.content.logistics.vault.ItemVaultMountedStorage;
import com.simibubi.create.foundation.ICapabilityProvider;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryWrapper;
import com.simibubi.create.foundation.codec.CreateCodecs;
import com.simibubi.create.foundation.utility.SameSizeCombinedInvWrapper;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class ShippingContainerBlock extends ItemVaultBlock {
  public final DyeColor COLOR;

  public ShippingContainerBlock (Properties properties, DyeColor color) {
    super(properties);
    registerDefaultState(defaultBlockState().setValue(LARGE, false));
    COLOR = color;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
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

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {

      for (BlockEntityEntry<Entity> vault : BlockRegistry.CONTAINER_ENTITIES.values()) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                vault.get(),
                (be, context) -> {
                  be.initCapability();
                  if (be.itemCapability == null)
                    return null;
                  return be.itemCapability.getCapability();
                }
        );
      }

    }

    @Override
    public InventoryIdentifier getInvId() {
      // ensure capability is up to date first, which sets the ID
      this.initCapability();
      return this.invId;
    }

    @Override
    public Entity getControllerBE () {
      if (isController())
        return this;
      BlockEntity blockEntity = level.getBlockEntity(controller);
      if (blockEntity instanceof Entity entity)
        return entity;
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
      BlockState state = getBlockState();
      if (ShippingContainerBlock.isVault(state)) {
        state = state.setValue(ItemVaultBlock.LARGE, false);
        getLevel().setBlock(worldPosition, state, 22);
      }
      super.removeController(keepContents);
    }

    private void initCapability() {
      if (itemCapability != null && itemCapability.getCapability() != null)
        return;
      if (!isController()) {
        Entity controllerBE = getControllerBE();
        if (controllerBE == null)
          return;
        controllerBE.initCapability();
        itemCapability = ICapabilityProvider.of(() -> {
          if (controllerBE.isRemoved())
            return null;
          if (controllerBE.itemCapability == null)
            return null;
          return controllerBE.itemCapability.getCapability();
        });
        invId = controllerBE.invId;
        return;
      }

      boolean alongZ = ItemVaultBlock.getVaultBlockAxis(getBlockState()) == Direction.Axis.Z;
      IItemHandlerModifiable[] invs = new IItemHandlerModifiable[length * radius * radius];
      for (int yOffset = 0; yOffset < length; yOffset++) {
        for (int xOffset = 0; xOffset < radius; xOffset++) {
          for (int zOffset = 0; zOffset < radius; zOffset++) {
            BlockPos vaultPos = alongZ ? worldPosition.offset(xOffset, zOffset, yOffset)
                    : worldPosition.offset(yOffset, xOffset, zOffset);
            Entity vaultAt =
                    ConnectivityHandler.partAt(getType(), getLevel(), vaultPos);
            invs[yOffset * radius * radius + xOffset * radius + zOffset] =
                    vaultAt != null ? vaultAt.inventory : new ItemStackHandler();
          }
        }
      }

      itemCapability = ICapabilityProvider.of(new VersionedInventoryWrapper(SameSizeCombinedInvWrapper.create(invs)));

      // build an identifier encompassing all component vaults
      BlockPos farCorner = alongZ
              ? worldPosition.offset(radius, radius, length)
              : worldPosition.offset(length, radius, radius);
      BoundingBox bounds = BoundingBox.fromCorners(this.worldPosition, farCorner);
      this.invId = new InventoryIdentifier.Bounds(bounds);
    }
  }

  public static class MountedStorage extends ItemVaultMountedStorage {

	  public static final MapCodec<MountedStorage> CODEC = CreateCodecs.ITEM_STACK_HANDLER.xmap(
			  MountedStorage::new, storage -> storage.wrapped
	  ).fieldOf("value");

    protected MountedStorage(MountedItemStorageType<?> type, ItemStackHandler handler) {
      super(type, handler);
    }

    protected MountedStorage(ItemStackHandler handler) {
      this(BlockRegistry.CONTAINER_MOUNTED_STORAGE.get(), handler);
    }

    @Override
    public void unmount(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity be) {
      if (be instanceof Entity vault) {
        vault.applyInventoryToBlock(this.wrapped);
      }
    }

	  public static MountedStorage fromVault(ItemVaultBlockEntity vault) {
		  // Vault inventories have a world-affecting onContentsChanged, copy to a safe one
		  return new MountedStorage(copyToItemStackHandler(vault.getInventoryOfBlock()));
	  }
  }

  public static class MountedStorageType extends MountedItemStorageType<MountedStorage> {

	  public MountedStorageType() {
		  super(MountedStorage.CODEC);
	  }

	  @Override
	  @Nullable
	  public MountedStorage mount(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity be) {
		  return be instanceof Entity vault ? MountedStorage.fromVault(vault) : null;
	  }
  }
}


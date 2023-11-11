package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.BlockRegistry;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.placard.PlacardBlockEntity;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.content.logistics.vault.ItemVaultBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nullable;

public class ShippingContainerBlock extends ItemVaultBlock {
    public static final Property<Direction.Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public ShippingContainerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends ItemVaultBlockEntity> getBlockEntityType() {
        return BlockRegistry.SHIPPING_CONTAINER_ENTITIES.get();
    }

    public static boolean isVault(BlockState state) {
        return state.getBlock() instanceof ItemVaultBlock;
    }

    @Nullable
    public static Direction.Axis getVaultBlockAxis(BlockState state) {
        if (!isVault(state))
            return null;
        return state.getValue(HORIZONTAL_AXIS);
    }

    public static boolean isLarge(BlockState state) {
        if (!isVault(state))
            return false;
        return state.getValue(LARGE);
    }


    // the BlockEntity for the Dyed Placard
    public static class Entity extends ItemVaultBlockEntity {
        public Entity (BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }
    }
}

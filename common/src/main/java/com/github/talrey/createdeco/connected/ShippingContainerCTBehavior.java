package com.github.talrey.createdeco.connected;

import com.github.talrey.createdeco.blocks.ShippingContainerBlock;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.content.logistics.vault.ItemVaultCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ShippingContainerCTBehavior extends ItemVaultCTBehaviour {

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        Direction.Axis vaultBlockAxis = ShippingContainerBlock.getVaultBlockAxis(state);
        boolean small = !ShippingContainerBlock.isLarge(state);
        if (vaultBlockAxis == null)
            return null;

        if (direction.getAxis() == vaultBlockAxis)
            return SpriteShifts.VAULT_FRONT.get(small);
        if (direction == Direction.UP)
            return SpriteShifts.VAULT_TOP.get(small);
        if (direction == Direction.DOWN)
            return SpriteShifts.VAULT_BOTTOM.get(small);

        return SpriteShifts.VAULT_SIDE.get(small);
    }
}

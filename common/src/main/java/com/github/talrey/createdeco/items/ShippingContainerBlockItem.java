package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.blocks.ShippingContainerBlock;
import com.github.talrey.createdeco.blocks.block_entities.ShippingContainerBlockEntity;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.fluids.tank.FluidTankItem;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;

public class ShippingContainerBlockItem extends BlockItem {
    // fabric: see comment in FluidTankItem
    @ApiStatus.Internal
    public static boolean IS_PLACING_NBT = false;

    public ShippingContainerBlockItem(Block p_i48527_1_, Properties p_i48527_2_) {
        super(p_i48527_1_, p_i48527_2_);
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        IS_PLACING_NBT = FluidTankItem.checkPlacingNbt(ctx);
        InteractionResult initialResult = super.place(ctx);
        IS_PLACING_NBT = false;
        if (!initialResult.consumesAction())
            return initialResult;
        tryMultiPlace(ctx);
        return initialResult;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos p_195943_1_, Level p_195943_2_, Player p_195943_3_,
                                                 ItemStack p_195943_4_, BlockState p_195943_5_) {
        MinecraftServer minecraftserver = p_195943_2_.getServer();
        if (minecraftserver == null)
            return false;
        CompoundTag nbt = p_195943_4_.getTagElement("BlockEntityTag");
        if (nbt != null) {
            nbt.remove("Length");
            nbt.remove("Size");
            nbt.remove("Controller");
            nbt.remove("LastKnownPos");
        }
        return super.updateCustomBlockEntityTag(p_195943_1_, p_195943_2_, p_195943_3_, p_195943_4_, p_195943_5_);
    }

    private void tryMultiPlace(BlockPlaceContext ctx) {
        Player player = ctx.getPlayer();
        if (player == null)
            return;
        if (player.isSteppingCarefully())
            return;
        Direction face = ctx.getClickedFace();
        ItemStack stack = ctx.getItemInHand();
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockPos placedOnPos = pos.relative(face.getOpposite());
        BlockState placedOnState = world.getBlockState(placedOnPos);

        if (!ShippingContainerBlock.isVault(placedOnState))
            return;
        ShippingContainerBlockEntity tankAt = ConnectivityHandler.partAt(BlockRegistry.SHIPPING_CONTAINER_ENTITIES.get(), world, placedOnPos);
        if (tankAt == null)
            return;
        ShippingContainerBlockEntity controllerBE = tankAt.getControllerBE();
        if (controllerBE == null)
            return;

        int width = controllerBE.getWidth();
        if (width == 1)
            return;

        int tanksToPlace = 0;
        Direction.Axis vaultBlockAxis = ShippingContainerBlock.getVaultBlockAxis(placedOnState);
        if (vaultBlockAxis == null)
            return;
        if (face.getAxis() != vaultBlockAxis)
            return;

        Direction vaultFacing = Direction.fromAxisAndDirection(vaultBlockAxis, Direction.AxisDirection.POSITIVE);
        BlockPos startPos = face == vaultFacing.getOpposite() ? controllerBE.getBlockPos()
                .relative(vaultFacing.getOpposite())
                : controllerBE.getBlockPos()
                .relative(vaultFacing, controllerBE.getHeight());

        if (VecHelper.getCoordinate(startPos, vaultBlockAxis) != VecHelper.getCoordinate(pos, vaultBlockAxis))
            return;

        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                BlockPos offsetPos = vaultBlockAxis == Direction.Axis.X ? startPos.offset(0, xOffset, zOffset)
                        : startPos.offset(xOffset, zOffset, 0);
                BlockState blockState = world.getBlockState(offsetPos);
                if (ShippingContainerBlock.isVault(blockState))
                    continue;
                if (!blockState.canBeReplaced())
                    return;
                tanksToPlace++;
            }
        }

        if (!player.isCreative() && stack.getCount() < tanksToPlace)
            return;

        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                BlockPos offsetPos = vaultBlockAxis == Direction.Axis.X ? startPos.offset(0, xOffset, zOffset)
                        : startPos.offset(xOffset, zOffset, 0);
                BlockState blockState = world.getBlockState(offsetPos);
                if (ShippingContainerBlock.isVault(blockState))
                    continue;
                BlockPlaceContext context = BlockPlaceContext.at(ctx, offsetPos, face);
                //player.getCustomData().method_10556("SilenceVaultSound", true);
                IS_PLACING_NBT = FluidTankItem.checkPlacingNbt(context);
                super.place(context);
                IS_PLACING_NBT = false;
                //player.getCustomData().method_10551("SilenceVaultSound");
            }
        }
    }

}

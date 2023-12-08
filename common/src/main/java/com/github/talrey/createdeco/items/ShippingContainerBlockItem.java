package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.BlockRegistry;
import com.github.talrey.createdeco.LoaderUtil;
import com.github.talrey.createdeco.blocks.ShippingContainerBlock;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.content.logistics.vault.ItemVaultItem;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;

public class ShippingContainerBlockItem extends ItemVaultItem {
  // fabric: see comment in FluidTankItem
  @ApiStatus.Internal
  public static boolean IS_PLACING_NBT = false;

  public ShippingContainerBlockItem (Block block, Properties props) {
    super(block, props);
  }

  @Override
  public InteractionResult place(BlockPlaceContext ctx) {
    IS_PLACING_NBT = LoaderUtil.checkPlacingNbt(ctx);
    InteractionResult initialResult = super.place(ctx);
    IS_PLACING_NBT = false;
    if (!initialResult.consumesAction())
      return initialResult;
    tryMultiPlace(ctx);
    return initialResult;
  }

  private void tryMultiPlace (BlockPlaceContext ctx) {
    Player player = ctx.getPlayer();
    if (player == null || player.isSteppingCarefully()) return;

    ShippingContainerBlock myBlock = (ShippingContainerBlock) this.getBlock();
    Direction face = ctx.getClickedFace();
    ItemStack stack = ctx.getItemInHand();
    Level level = ctx.getLevel();
    BlockPos pos = ctx.getClickedPos();
    BlockPos placedOn = pos.relative(face.getOpposite());
    BlockState placedOnState = level.getBlockState(placedOn);

    if (!myBlock.isSameType(placedOnState)) return;
    ShippingContainerBlock.Entity beAt = ConnectivityHandler.partAt(
      BlockRegistry.CONTAINER_ENTITIES.get(myBlock.COLOR).get(), level, placedOn
    );
    if (beAt == null) return;
    ShippingContainerBlock.Entity controller = beAt.getControllerBE();
    if (controller == null) return;

    int width = controller.getWidth();
    if (width == 1) return;

    int blocksToPlace = 0;
    Direction.Axis axis = ShippingContainerBlock.getVaultBlockAxis(placedOnState);
    if (axis == null || axis != face.getAxis()) return;

    Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
    BlockPos start = (face == facing.getOpposite())
      ? controller.getBlockPos().relative(facing.getOpposite())
      : controller.getBlockPos().relative(facing, controller.getHeight());

    if (VecHelper.getCoordinate(start, axis) != VecHelper.getCoordinate(pos, axis)) return;

    for (int xOffset = 0; xOffset < width; xOffset++) {
      for (int zOffset = 0; zOffset < width; zOffset++) {
        BlockPos offset = (axis == Direction.Axis.X)
          ? start.offset(0, xOffset, zOffset)
          : start.offset(xOffset, zOffset, 0);
        BlockState other = level.getBlockState(offset);
        if (ShippingContainerBlock.isVault(other)) continue;
        if (!other.canBeReplaced()) return;
        blocksToPlace++;
      }
    }
    if (!player.isCreative() && stack.getCount() < blocksToPlace) return;
    for (int xOffset = 0; xOffset < width; xOffset++) {
      for (int zOffset = 0; zOffset < width; zOffset++) {
        BlockPos offset = (axis == Direction.Axis.X)
          ? start.offset(0, xOffset, zOffset)
          : start.offset(xOffset, zOffset, 0);
        BlockState other = level.getBlockState(offset);
        if (myBlock.isSameType(other)) continue;
        BlockPlaceContext context = BlockPlaceContext.at(ctx, offset, face);
        IS_PLACING_NBT = LoaderUtil.checkPlacingNbt(context);
        super.place(context);
        IS_PLACING_NBT = false;
      }
    }
  }
}

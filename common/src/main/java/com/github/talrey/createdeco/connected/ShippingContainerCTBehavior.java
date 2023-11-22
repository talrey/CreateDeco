package com.github.talrey.createdeco.connected;

import com.github.talrey.createdeco.blocks.ShippingContainerBlock;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ShippingContainerCTBehavior extends ConnectedTextureBehaviour.Base {

  @Override
  public CTSpriteShiftEntry getShift (
    BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite
  ) {
    Direction.Axis vaultBlockAxis = ShippingContainerBlock.getVaultBlockAxis(state);
    boolean small = !ShippingContainerBlock.isLarge(state);
    DyeColor color = ShippingContainerBlock.getColor(state);
    if (vaultBlockAxis == null) return null;

    if (direction.getAxis() == vaultBlockAxis)
      return SpriteShifts.VAULT_FRONT.get(color).get(small);
    if (direction == Direction.UP)
      return SpriteShifts.VAULT_TOP.get(color).get(small);
    if (direction == Direction.DOWN)
      return SpriteShifts.VAULT_BOTTOM.get(color).get(small);

    return SpriteShifts.VAULT_SIDE.get(color).get(small);
  }

  @Override
  protected Direction getUpDirection (
    BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face
  ) {
    Direction.Axis vaultBlockAxis = ShippingContainerBlock.getVaultBlockAxis(state);
    boolean alongX = vaultBlockAxis == Direction.Axis.X;
    if (face.getAxis().isVertical() && alongX)
      return super.getUpDirection(reader, pos, state, face).getClockWise();
    if (face.getAxis() == vaultBlockAxis || face.getAxis().isVertical())
      return super.getUpDirection(reader, pos, state, face);
    return Direction.fromAxisAndDirection(vaultBlockAxis, alongX
      ? Direction.AxisDirection.POSITIVE
      : Direction.AxisDirection.NEGATIVE
    );
  }

  @Override
  protected Direction getRightDirection (
    BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face
  ) {
    Direction.Axis vaultBlockAxis = ShippingContainerBlock.getVaultBlockAxis(state);
    if (face.getAxis().isVertical() && vaultBlockAxis == Direction.Axis.X)
      return super.getRightDirection(reader, pos, state, face).getClockWise();
    if (face.getAxis() == vaultBlockAxis || face.getAxis().isVertical())
      return super.getRightDirection(reader, pos, state, face);
    return Direction.fromAxisAndDirection(Direction.Axis.Y, face.getAxisDirection());
  }

  public boolean buildContextForOccludedDirections () {
    return super.buildContextForOccludedDirections();
  }

  @Override
  public boolean connectsTo (
    BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos,
    BlockPos otherPos, Direction face
  ) {
    return state == other && ConnectivityHandler.isConnected(reader, pos, otherPos);
  }
}

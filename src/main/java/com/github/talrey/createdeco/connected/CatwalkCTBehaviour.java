package com.github.talrey.createdeco.connected;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;

import com.simibubi.create.foundation.block.connected.SimpleCTBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CatwalkCTBehaviour extends SimpleCTBehaviour {
  public CatwalkCTBehaviour (CTSpriteShiftEntry layerShift) { super (layerShift); }

  @Override
  public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
    return (face.getAxis().isVertical() && (state.getBlock() == other.getBlock()));
  }

  public Supplier<ConnectedTextureBehaviour> getSupplier () {
    return () -> this;
  }
}

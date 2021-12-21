package com.github.talrey.createdeco.connected;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.StandardCTBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class CatwalkCTBehaviour extends StandardCTBehaviour {
  public CatwalkCTBehaviour (CTSpriteShiftEntry layerShift) { super (layerShift); }

  @Override
  public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
    return (face.getAxis().isVertical() && (state.getBlock() == other.getBlock()));
  }

}

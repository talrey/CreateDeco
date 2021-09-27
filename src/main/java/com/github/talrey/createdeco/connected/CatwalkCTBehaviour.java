package com.github.talrey.createdeco.connected;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.StandardCTBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public class CatwalkCTBehaviour extends StandardCTBehaviour {
  public CatwalkCTBehaviour (CTSpriteShiftEntry layerShift) { super (layerShift); }

  @Override
  public boolean connectsTo(BlockState state, BlockState other, IBlockDisplayReader reader, BlockPos pos, BlockPos otherPos, Direction face) {
    return (face.getAxis().isVertical() && (state.getBlock() == other.getBlock()));
  }

}

package com.github.talrey.createdeco.connected;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.math.BlockPos;

public class SheetMetalVertCTBehaviour extends SheetMetalSlabCTBehaviour {
  public SheetMetalVertCTBehaviour (CTSpriteShiftEntry layerShift) { super (layerShift); }
  public SheetMetalVertCTBehaviour (CTSpriteShiftEntry layerShift, CTSpriteShiftEntry topShift) { super(layerShift, topShift); }

  @Override
  protected boolean slabTouching (BlockState state, BlockPos pos, BlockState other, BlockPos otherPos) {
    if (!other.contains(BlockStateProperties.SLAB_TYPE)) return false;
    switch (state.get(BlockStateProperties.SLAB_TYPE)) {
      case TOP:
        return other.get(BlockStateProperties.SLAB_TYPE).equals(SlabType.TOP);
      case BOTTOM:
        return other.get(BlockStateProperties.SLAB_TYPE).equals(SlabType.BOTTOM);
      case DOUBLE:
        return true;
    }
    return false;
  }
}

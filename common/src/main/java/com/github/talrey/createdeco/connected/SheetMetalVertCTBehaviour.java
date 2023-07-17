package com.github.talrey.createdeco.connected;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;

public class SheetMetalVertCTBehaviour extends SheetMetalSlabCTBehaviour {
  public SheetMetalVertCTBehaviour (CTSpriteShiftEntry layerShift) { super (layerShift); }
  public SheetMetalVertCTBehaviour (CTSpriteShiftEntry layerShift, CTSpriteShiftEntry topShift) { super(layerShift, topShift); }

  @Override
  protected boolean slabTouching (BlockState state, BlockPos pos, BlockState other, BlockPos otherPos) {
    if (!other.hasProperty(BlockStateProperties.SLAB_TYPE)) return false;
    switch (state.getValue(BlockStateProperties.SLAB_TYPE)) {
      case TOP:
        return other.getValue(BlockStateProperties.SLAB_TYPE).equals(SlabType.TOP);
      case BOTTOM:
        return other.getValue(BlockStateProperties.SLAB_TYPE).equals(SlabType.BOTTOM);
      case DOUBLE:
        return true;
    }
    return false;
  }
}

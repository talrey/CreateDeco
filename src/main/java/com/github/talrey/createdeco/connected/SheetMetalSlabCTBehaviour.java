package com.github.talrey.createdeco.connected;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;

public class SheetMetalSlabCTBehaviour extends SheetMetalCTBehaviour {
  public SheetMetalSlabCTBehaviour (CTSpriteShiftEntry layerShift) { super (layerShift); }
  public SheetMetalSlabCTBehaviour (CTSpriteShiftEntry layerShift, CTSpriteShiftEntry topShift) { super(layerShift, topShift); }

  @Override
  public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
    if (slabTouching(state, pos, other, otherPos) && super.connectsTo(state, other, reader, pos, otherPos, face)) {
      return true;
    }
    else return isSameMaterial(state, other);
  }

  protected boolean slabTouching (BlockState state, BlockPos pos, BlockState other, BlockPos otherPos) {
    if (!other.hasProperty(BlockStateProperties.SLAB_TYPE)) return false;
    switch (state.getValue(BlockStateProperties.SLAB_TYPE)) {
      case TOP:
        return !other.getValue(BlockStateProperties.SLAB_TYPE).equals(SlabType.TOP);
      case BOTTOM:
        return !other.getValue(BlockStateProperties.SLAB_TYPE).equals(SlabType.BOTTOM);
      case DOUBLE:
        if (pos.getY() - otherPos.getY() > 0) { // we're above them
          return !other.getValue(BlockStateProperties.SLAB_TYPE).equals(SlabType.BOTTOM);
        }
        else return !other.getValue(BlockStateProperties.SLAB_TYPE).equals(SlabType.TOP);
    }
    return false;
  }
}

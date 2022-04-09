package com.github.talrey.createdeco.connected;

import com.github.talrey.createdeco.Registration;
import com.github.talrey.createdeco.registry.SheetMetal;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class SheetMetalCTBehaviour extends HorizontalCTBehaviour {
  public SheetMetalCTBehaviour (CTSpriteShiftEntry layerShift) { super (layerShift); }
  public SheetMetalCTBehaviour (CTSpriteShiftEntry layerShift, CTSpriteShiftEntry topShift) { super(layerShift, topShift); }

  @Override
  public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {

    if (!face.getAxis().isVertical() && super.connectsTo(state, other, reader, pos, otherPos, face)) {
      return true;
    }
    else return isSameMaterial(state, other) && SheetMetal.SHEET_METAL_BLOCKS.containsValue(other.getBlock());
  }

  protected boolean isSameMaterial (BlockState state, BlockState other) {
    Block me = state.getBlock();
    Block it = other.getBlock();
    String material = me.getRegistryName().toString().replace("createdeco:","");
    return it.getRegistryName().toString().replace("createdeco:","").startsWith(material.substring(0, material.indexOf("_")));
  }

  public Supplier<ConnectedTextureBehaviour> getSupplier () {
    return () -> this;
  }
}

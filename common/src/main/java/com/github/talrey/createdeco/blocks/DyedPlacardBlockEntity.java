package com.github.talrey.createdeco.blocks;

import com.simibubi.create.content.decoration.placard.PlacardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DyedPlacardBlockEntity extends PlacardBlockEntity {
  public DyedPlacardBlockEntity (BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }
}

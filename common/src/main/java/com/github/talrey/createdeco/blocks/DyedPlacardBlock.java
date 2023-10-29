package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.BlockRegistry;
import com.simibubi.create.content.decoration.placard.PlacardBlock;
import com.simibubi.create.content.decoration.placard.PlacardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DyedPlacardBlock extends PlacardBlock {
  public DyedPlacardBlock (Properties props) {
    super(props);
  }

  @Override
  public BlockEntityType<? extends PlacardBlockEntity> getBlockEntityType() {
    return BlockRegistry.PLACARD_ENTITY.get();
  }

  // the BlockEntity for the Dyed Placard
  public static class Entity extends PlacardBlockEntity {
    public Entity (BlockEntityType<?> type, BlockPos pos, BlockState state) {
      super(type, pos, state);
    }
  }
}

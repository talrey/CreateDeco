package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.registry.Props;
import com.simibubi.create.content.decoration.placard.PlacardBlock;
import com.simibubi.create.content.decoration.placard.PlacardBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class DyedPlacardBlock extends PlacardBlock {
  public DyedPlacardBlock (Properties props) {
    super(props);
  }

  @Override
  public BlockEntityType<? extends PlacardBlockEntity> getBlockEntityType() {
    return Props.PLACARD_ENTITY.get();
  }
}

package com.github.talrey.createdeco;

import com.simibubi.create.foundation.block.IBE;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LoaderUtil {
  @ExpectPlatform
  public static int getSignal (IBE<?> be, BlockState pState, Level pLevel, BlockPos pPos) {
    throw new AssertionError();
  }

  // this check is only done in Fabric's implementation, forge doesn't seem to need it
  @ExpectPlatform
  public static boolean checkPlacingNbt (BlockPlaceContext ctx) {
    throw new AssertionError();
  }
}

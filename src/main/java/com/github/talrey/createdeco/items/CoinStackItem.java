package com.github.talrey.createdeco.items;

import com.github.talrey.createdeco.blocks.CoinStackBlock;
import com.github.talrey.createdeco.registry.Props;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CoinStackItem extends Item {
  public final String material;

  public CoinStackItem (Properties props) {
    this(props, "iron");
  }

  public CoinStackItem (Properties props, String material) {
    super(props);
    this.material = material;
  }

  protected boolean placeBlock(UseOnContext ctx) {
    BlockPlaceContext bictx = new BlockPlaceContext (ctx);
    BlockState target = ctx.getLevel().getBlockState(ctx.getClickedPos());
    if (target.getBlock() instanceof CoinStackBlock && this.material.equals(((CoinStackBlock)target.getBlock()).material) && target.getValue(BlockStateProperties.LAYERS) < 8) {
      int height = target.getValue(BlockStateProperties.LAYERS);
      if (!ctx.getLevel().isClientSide()) ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), Props.COIN_BLOCKS.get(this.material).getDefaultState()
        .setValue(BlockStateProperties.LAYERS, height+1)
      );
      return true;
    }
    else if (target.canBeReplaced(bictx)) {
      if (!ctx.getLevel().isClientSide()) ctx.getLevel().setBlockAndUpdate(ctx.getClickedPos(), Props.COIN_BLOCKS.get(this.material).getDefaultState());
      return true;
    }
    else {
      target = ctx.getLevel().getBlockState(ctx.getClickedPos().offset(ctx.getClickedFace().getNormal()));
      if (target.canBeReplaced(bictx)) {
        if (!ctx.getLevel().isClientSide()) ctx.getLevel().setBlockAndUpdate(
          ctx.getClickedPos().offset(ctx.getClickedFace().getNormal()), Props.COIN_BLOCKS.get(this.material).getDefaultState()
        );
        return true;
      }
    }
    return false;
  }


  @Override
  public InteractionResult useOn (UseOnContext ctx) {
    if (placeBlock (ctx)) {
      ctx.getItemInHand().shrink(1);
      ctx.getLevel().playSound(
        null, ctx.getClickedPos(), SoundEvents.CHAIN_PLACE, SoundSource.BLOCKS, 1f, 1f
      );
      return InteractionResult.SUCCESS;
    }
    return super.useOn(ctx);
  }
}

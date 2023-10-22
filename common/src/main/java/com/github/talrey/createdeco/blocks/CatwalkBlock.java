package com.github.talrey.createdeco.blocks;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CatwalkBlock extends Block implements IWrenchable, SimpleWaterloggedBlock {
  private static final VoxelShape VOXEL_TOP = Block.box(
    0d, 14d, 0d,
    16d, 16d, 16d
  );

  public CatwalkBlock (Properties props) {
    super(props);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
    return getInteractionShape(state, reader, pos);
  }

  @Override
  public VoxelShape getInteractionShape (BlockState state, BlockGetter world, BlockPos pos) {
    return VOXEL_TOP;
  }

  public static boolean isCatwalk (ItemStack test) {
    return (test.getItem() instanceof BlockItem be)
      && be.getBlock() instanceof CatwalkBlock;
    //isCatwalk(((BlockItem)test.getItem()).getBlock());
  }

  public static boolean isCatwalk (Block test) {
    return test instanceof CatwalkBlock || test instanceof CatwalkStairBlock;
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());

    BlockState state = defaultBlockState()
      .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
    Player player = ctx.getPlayer();

    Level world = ctx.getLevel();
    if (canPlaceCatwalk(world, ctx.getClickedPos())) {
      world.setBlock(ctx.getClickedPos(), state, 3);
      if (!player.getAbilities().instabuild)
        player.getItemInHand(ctx.getHand()).shrink(1);
      world.playSound(player, ctx.getClickedPos(),
        SoundEvents.NETHERITE_BLOCK_PLACE, SoundSource.BLOCKS, 1f, 1f
      );
      //return world.getBlockState(ctx.getClickedPos());
    }
    return state;
  }

  public static boolean canPlaceCatwalk (Level world, BlockPos pos) {
    return world.getBlockState(pos).canBeReplaced();
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.WATERLOGGED);
  }

  @Override
  public boolean canPlaceLiquid (BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
    return !state.getValue(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
  }

//  @Override
//  public FluidState getFluidState(BlockState state) {
//    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
//  }
}

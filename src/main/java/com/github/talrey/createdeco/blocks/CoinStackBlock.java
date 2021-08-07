package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.CreateDecoMod;
import com.github.talrey.createdeco.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;

public class CoinStackBlock extends Block {
  private static final VoxelShape[] SHAPE = {
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 2d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 4d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 6d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 8d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 10d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 12d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 14d, 16d
    ),
    Block.makeCuboidShape(
  0d, 0d, 0d,
  16, 16d, 16d
    )
  };

  public CoinStackBlock (Properties properties) {
    super(properties);
    this.setDefaultState(this.getStateContainer().getBaseState().with(BlockStateProperties.LAYERS_1_8, 1));
  }

  @Override
  public VoxelShape getShape (BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
    return SHAPE[state.get(BlockStateProperties.LAYERS_1_8)-1];
  }

  @Override
  public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
    if (facing == Direction.DOWN && !hasEnoughSolidSide(worldIn, facingPos, Direction.UP)) return Blocks.AIR.getDefaultState();
    return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockItemUseContext ctx) {
    return getDefaultState();
  }

  @Override
  protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) { builder.add(BlockStateProperties.LAYERS_1_8); }

  @Override
  public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
    String material = state.getBlock().getTranslationKey().replace("_coinstack_block", "");
    material = material.substring(material.lastIndexOf('.')+1); // remove createdeco.block.
    material = material.substring(0,1).toUpperCase() + material.substring(1); // capitalize
  //  LogManager.getLogger(CreateDecoMod.MODID).debug("Checking for " + material);
    return Registration.COINSTACK_ITEM.containsKey(material) ? Registration.COINSTACK_ITEM.get(material).asStack() : new ItemStack(Items.AIR);
  }
}

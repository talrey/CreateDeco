package com.github.talrey.createdeco.blocks;

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

import javax.annotation.Nullable;

public class CoinStackBlock extends Block {
  private static final VoxelShape[] SHAPE = {
    Block.box(
  0d, 0d, 0d,
  16, 2d, 16d
    ),
    Block.box(
  0d, 0d, 0d,
  16, 4d, 16d
    ),
    Block.box(
  0d, 0d, 0d,
  16, 6d, 16d
    ),
    Block.box(
  0d, 0d, 0d,
  16, 8d, 16d
    ),
    Block.box(
  0d, 0d, 0d,
  16, 10d, 16d
    ),
    Block.box(
  0d, 0d, 0d,
  16, 12d, 16d
    ),
    Block.box(
  0d, 0d, 0d,
  16, 14d, 16d
    ),
    Block.box(
  0d, 0d, 0d,
  16, 16d, 16d
    )
  };

  public CoinStackBlock (Properties properties) {
    super(properties);
    this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.LAYERS, 1));
  }

  @Override
  public VoxelShape getShape (BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
    return SHAPE[state.getValue(BlockStateProperties.LAYERS)-1];
  }

  @Override
  public BlockState updateShape (BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
    if (facing == Direction.DOWN && !canSupportCenter(worldIn, facingPos, Direction.UP)) return Blocks.AIR.defaultBlockState();
    return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockItemUseContext ctx) {
    return defaultBlockState();
  }

  @Override
  protected void createBlockStateDefinition (StateContainer.Builder<Block, BlockState> builder) { builder.add(BlockStateProperties.LAYERS); }

  @Override
  public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
    String material = state.getBlock().getDescriptionId().replace("_coinstack_block", "");
    material = material.substring(material.lastIndexOf('.')+1); // remove createdeco.block.
    material = material.substring(0,1).toUpperCase() + material.substring(1); // capitalize
    return Registration.COINSTACK_ITEM.containsKey(material) ? Registration.COINSTACK_ITEM.get(material).asStack() : new ItemStack(Items.AIR);
  }
}

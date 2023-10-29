package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.ItemRegistry;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CoinStackBlock extends Block implements ProperWaterloggedBlock {
  public final String material;
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
    this(properties, "iron");
  }

  public CoinStackBlock (Properties properties, String material) {
    super(properties);
    this.material = material;
    this.registerDefaultState(
      this.defaultBlockState()
        .setValue(BlockStateProperties.LAYERS, 1)
        .setValue(WATERLOGGED, false)
    );
  }

  @Override
  public VoxelShape getShape (BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
    return SHAPE[state.getValue(BlockStateProperties.LAYERS)-1];
  }

  @Override
  public FluidState getFluidState(BlockState pState) {
    return fluidState(pState);
  }

  @Override
  public BlockState updateShape (BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
    updateWater(worldIn, stateIn, currentPos);
    if (facing == Direction.DOWN && !canSupportCenter(worldIn, facingPos, Direction.UP)) return Blocks.AIR.defaultBlockState();
    return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    return withWater(defaultBlockState(), ctx);
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.LAYERS, WATERLOGGED);
  }

  @Override
  public ItemStack getCloneItemStack (BlockGetter level, BlockPos pos, BlockState state) {
    return ItemRegistry.COINSTACKS.containsKey(material)
      ? ItemRegistry.COINSTACKS.get(material).asStack()
      : new ItemStack(Items.AIR);
  }
}
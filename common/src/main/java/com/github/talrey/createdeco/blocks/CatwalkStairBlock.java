package com.github.talrey.createdeco.blocks;

import com.github.talrey.createdeco.BlockRegistry;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CatwalkStairBlock extends Block implements IWrenchable, SimpleWaterloggedBlock {
  public static final BooleanProperty RAILING_LEFT = BooleanProperty.create("railing_left");
  public static final BooleanProperty RAILING_RIGHT = BooleanProperty.create("railing_right");

  public final String metal;

  private static final VoxelShape BOX_NORTH = Shapes.join(
    Block.box(0d, 14d, 8d, 16d, 16d, 16d),
    Block.box(0d, 6d, 0d, 16d, 8d, 8d),
    BooleanOp.OR
  );
  private static final VoxelShape BOX_SOUTH = Shapes.join(
    Block.box(0d, 14d, 0d, 16d, 16d, 8d),
    Block.box(0d, 6d, 8d, 16d, 8d, 16d),
    BooleanOp.OR
  );
  private static final VoxelShape BOX_WEST = Shapes.join(
    Block.box(8d, 14d, 0d, 16d, 16d, 16d),
    Block.box(0d, 6d, 0d, 8d, 8d, 16d),
    BooleanOp.OR
  );
  private static final VoxelShape BOX_EAST = Shapes.join(
    Block.box(0d, 14d, 0d, 8d, 16d, 16),
    Block.box(8d, 6d, 0d, 16d, 8d, 16d),
    BooleanOp.OR
  );

  public CatwalkStairBlock (Properties props, String metal) {
    super(props);
    this.metal = metal;
    this.registerDefaultState(this.defaultBlockState()
      .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
      .setValue(BlockStateProperties.WATERLOGGED, false)
      .setValue(RAILING_LEFT, false)
      .setValue(RAILING_RIGHT, false)
    );
  }

  public static boolean isCatwalkStair (ItemStack test) {
    return (test.getItem() instanceof BlockItem be)
      && be.getBlock() instanceof CatwalkStairBlock;
    //isCatwalk(((BlockItem)test.getItem()).getBlock());
  }

  public static boolean isCatwalkStair (Block test) {
    return test instanceof CatwalkBlock || test instanceof CatwalkStairBlock;
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement (BlockPlaceContext ctx) {
    Direction facing = ctx.getHorizontalDirection();
    FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());

    return defaultBlockState()
      .setValue(BlockStateProperties.HORIZONTAL_FACING, facing.getOpposite())
      .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
  }

  @Override
  protected void createBlockStateDefinition (StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
    builder.add(BlockStateProperties.WATERLOGGED);
    builder.add(RAILING_LEFT);
    builder.add(RAILING_RIGHT);
  }

  @Override
  public boolean canPlaceLiquid (BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
    return !state.getValue(BlockStateProperties.WATERLOGGED) && fluid == Fluids.WATER;
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
  }

  @Override
  public VoxelShape getShape (BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
    return switch(state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
      case SOUTH -> BOX_SOUTH;
      case EAST  -> BOX_EAST;
      case WEST  -> BOX_WEST;
      default    -> BOX_NORTH;
    };
  }

  @Override
  public InteractionResult onSneakWrenched (BlockState state, UseOnContext context) {
    BlockPos pos   = context.getClickedPos();
    Vec3 subbox    = context.getClickLocation().subtract(pos.getCenter());
    Level level    = context.getLevel();
    Player player  = context.getPlayer();

    if (state.getValue(RAILING_RIGHT) || state.getValue(RAILING_LEFT)) {
      var xPos = subbox.x;
      var zPos = subbox.z;

      var dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
      boolean left = false;

      if (dir == Direction.NORTH) left = xPos > 0;
      if (dir == Direction.SOUTH) left = xPos < 0;
      if (dir == Direction.EAST) left = zPos > 0;
      if (dir == Direction.WEST) left = zPos < 0;

      if (level instanceof ClientLevel || !state.getValue(left ? CatwalkStairBlock.RAILING_LEFT : CatwalkStairBlock.RAILING_RIGHT)) return InteractionResult.PASS;

      level.setBlock(pos, state.setValue(left ? CatwalkStairBlock.RAILING_LEFT : CatwalkStairBlock.RAILING_RIGHT, false), 3);

      if (!player.getAbilities().instabuild) player.addItem(new ItemStack(
        BlockRegistry.CATWALK_RAILINGS.get(metal)
      ));
      playRemoveSound(level, pos);
      return InteractionResult.SUCCESS;
    }

    level.removeBlock(pos, false);
    if (!player.getAbilities().instabuild) player.addItem(new ItemStack(state.getBlock().asItem()));
    playRemoveSound(level, pos);
    return InteractionResult.SUCCESS;

  }
}

package com.github.talrey.createdeco.blocks;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class MeshFenceBlock extends FenceBlock implements IWrenchable {
    public static final BooleanProperty UP = BlockStateProperties.UP;

    public MeshFenceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(UP, false).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(WATERLOGGED, false));
    }

    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();

        level.setBlockAndUpdate(context.getClickedPos(), state.setValue(UP, !state.getValue(UP)));
        IWrenchable.playRotateSound(level, context.getClickedPos());

        return InteractionResult.SUCCESS;
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }
}

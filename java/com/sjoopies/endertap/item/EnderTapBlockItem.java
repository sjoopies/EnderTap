package com.sjoopies.endertap.item;

import com.sjoopies.endertap.block.EnderTapBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class EnderTapBlockItem extends BlockItem {

    public EnderTapBlockItem(Block enderTapBlock, Properties properties) {
        super(enderTapBlock, properties);
    }

    protected boolean updateCustomBlockEntityTag(BlockPos blockPos, Level level, @Nullable Player player, ItemStack itemStack, BlockState blockState) {
        boolean flag = super.updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState);
        if (player != null) {
            ((EnderTapBlock) getBlock()).setPlayerID(level, blockPos, player);
        }
        return flag;
    }

}

package com.sjoopies.endertap.block;

import com.sjoopies.endertap.blockentity.EnderTapBlockEntity;
import com.sjoopies.endertap.blockentity.ModBlockEntities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class EnderTapBlock extends ModBlock {

    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    public EnderTapBlock(Properties properties, String registryName) {
        super(properties, registryName);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if (player.isCrouching()) {
            setPlayerID(level, pos, player);
        } else {
            Player pe = ((EnderTapBlockEntity) level.getBlockEntity(pos)).getPlayer();
            if (level.isClientSide && pe != null)

                player.sendMessage(new TextComponent("Linked to " + pe.getGameProfile().getName()), Util.NIL_UUID);

        }
        return InteractionResult.SUCCESS;
    }

    public boolean setPlayerID(Level level, BlockPos pos, Player player) {
        ((EnderTapBlockEntity) level.getBlockEntity(pos)).setPlayerID(player);

        level.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.END_PORTAL_FRAME_FILL,
                SoundSource.BLOCKS, 1.0F, 1.0F);
        return true;
    }


    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        return calcRedstoneFromInventory((EnderTapBlockEntity) level.getBlockEntity(pos));
    }

    public static int calcRedstoneFromInventory(@Nullable EnderTapBlockEntity enderTapTileEntity) {
        if (enderTapTileEntity == null) {
            return 0;
        } else {
            int i = 0;
            float f = 0.0F;

            for (int j = 0; j < enderTapTileEntity.getItemHandler().getSlots(); ++j) {
                ItemStack itemstack = enderTapTileEntity.getItemHandler().getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    f += (float) itemstack.getCount() / (float) Math.min(64, itemstack.getMaxStackSize());
                    ++i;
                }
            }

            f = f / (float) enderTapTileEntity.getItemHandler().getSlots();
            return Mth.floor(f * 14.0F) + (i > 0 ? 1 : 0);
        }
    }

    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState p_48735_, BlockGetter p_48736_, BlockPos p_48737_, CollisionContext p_48738_) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnderTapBlockEntity(pos, state);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, Random rand) {
        for (int i = 0; i < 3; ++i) {
            int j = rand.nextInt(2) * 2 - 1;
            int k = rand.nextInt(2) * 2 - 1;
            double d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
            double d1 = (float) pos.getY() + rand.nextFloat();
            double d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) k;
            double d3 = rand.nextFloat() * (float) j;
            double d4 = ((double) rand.nextFloat() - 0.5D) * 0.125D;
            double d5 = rand.nextFloat() * (float) k;

            level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }

    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.ENDER_TAP, EnderTapBlockEntity::enterTapTick);
    }

}

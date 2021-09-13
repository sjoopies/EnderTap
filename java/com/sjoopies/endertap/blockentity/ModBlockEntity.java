package com.sjoopies.endertap.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class ModBlockEntity extends BlockEntity {
    public ModBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    @Nonnull
    @Override
    public CompoundTag save(CompoundTag tag) {

        CompoundTag ret = super.save(tag);
        savePacketTag(ret);
        return ret;
    }

    @Nonnull
    @Override
    public final CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadPacketTag(tag);
    }

    public void savePacketTag(CompoundTag cmp) {
    }

    public void loadPacketTag(CompoundTag cmp) {
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag tag = new CompoundTag();
        savePacketTag(tag);
        return new ClientboundBlockEntityDataPacket(getBlockPos(), -999, tag);
    }
    // TODO idk what replaced this
//	@Override
//	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
//		super.onDataPacket(net, packet);
//		readPacketNBT(packet.getNbtCompound());
//	}

}

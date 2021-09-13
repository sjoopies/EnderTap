package com.sjoopies.endertap.network;

import com.sjoopies.endertap.blockentity.EnderTapBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class EnderTapPacket {
    private final String str;
    private final BlockPos pos;

    public EnderTapPacket(String str, BlockPos pos) {
        this.str = str;
        this.pos = pos;
    }

    public static EnderTapPacket decode(FriendlyByteBuf buf) {
        return new EnderTapPacket(buf.readUtf(), buf.readBlockPos());
    }

    public static void encode(EnderTapPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.str);
        buf.writeBlockPos(msg.pos);
    }


    public static void handle(final EnderTapPacket message, final Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide().isServer()) {
            ctx.get().setPacketHandled(true);
            return;
        }
        // server -> client
        ctx.get().enqueueWork(() -> {
            BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(message.pos);

            if (blockEntity != null && blockEntity instanceof EnderTapBlockEntity) {
                EnderTapBlockEntity t = (EnderTapBlockEntity) blockEntity;
                t.setPlayerName(message.str);
            }

        });

        ctx.get().setPacketHandled(true);
    }

}

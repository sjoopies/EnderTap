package com.sjoopies.endertap.network;

import java.util.function.Supplier;

import com.sjoopies.endertap.tileentity.EnderTapTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class EnderTapPacket {
	private final String str;
	private final BlockPos pos;

	public EnderTapPacket(String str, BlockPos pos) {
		this.str = str;
		this.pos = pos;
	}

	public static EnderTapPacket decode(PacketBuffer buf) {
		return new EnderTapPacket(buf.readString(), buf.readBlockPos());
	}

	public static void encode(EnderTapPacket msg, PacketBuffer buf) {
		buf.writeString(msg.str);
		buf.writeBlockPos(msg.pos);
	}

	public static void handle(final EnderTapPacket message, final Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isServer()) {
			ctx.get().setPacketHandled(true);
			return;
		}

		ctx.get().enqueueWork(() -> {

			ClientWorld world = Minecraft.getInstance().world;
			TileEntity te = world.getTileEntity(message.pos);
			if (te != null && te instanceof EnderTapTileEntity) {
				EnderTapTileEntity t = (EnderTapTileEntity) te;
				t.setPlayerName(message.str);

			}

		});

		ctx.get().setPacketHandled(true);
	}

}

package com.sjoopies.endertap.network;

import com.sjoopies.endertap.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModPacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Reference.MODID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	public static void registerPackets() {
		int id = 0;
		INSTANCE.registerMessage(id++, EnderTapPacket.class, EnderTapPacket::encode, EnderTapPacket::decode, EnderTapPacket::handle);
	}

	public static void sendToNearby(World world, BlockPos pos, Object toSend) {
		if (world instanceof ServerWorld) {
			ServerWorld ws = (ServerWorld) world;

			ws.getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(pos), false).filter(p -> p.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64).forEach(p -> INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), toSend));
		}
	}

	public static void sendToNearby(IWorld world, ChunkPos pos, Object toSend) {
		if (world instanceof ServerWorld) {
			ServerWorld ws = (ServerWorld) world;
			ws.getChunkProvider().chunkManager.getTrackingPlayers(pos, false).forEach(p -> INSTANCE.send(PacketDistributor.PLAYER.with(() -> p), toSend));
		}
	}

	public static void sendToNearby(World world, Entity e, Object toSend) {
		sendToNearby(world, new BlockPos(e), toSend);
	}

	public static void sendTo(ServerPlayerEntity playerMP, Object toSend) {
		INSTANCE.sendTo(toSend, playerMP.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void sendNonLocal(ServerPlayerEntity playerMP, Object toSend) {
		if (playerMP.server.isDedicatedServer() || !playerMP.getGameProfile().getName().equals(playerMP.server.getServerOwner())) {
			sendTo(playerMP, toSend);
		}
	}

	public static void sendToServer(Object msg) {
		INSTANCE.sendToServer(msg);
	}
}

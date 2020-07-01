package com.sjoopies.endertap.client;

import com.sjoopies.endertap.Reference;
import com.sjoopies.endertap.tileentity.ModTileEntities;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Reference.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModClientRegistry {

	@SubscribeEvent
	public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
		
		ClientRegistry.bindTileEntityRenderer(ModTileEntities.ENDER_TAP, EnderTapTileEntityRenderer::new);

	}
}

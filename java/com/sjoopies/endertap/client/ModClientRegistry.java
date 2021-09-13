package com.sjoopies.endertap.client;

import com.sjoopies.endertap.Reference;
import com.sjoopies.endertap.blockentity.ModBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Reference.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModClientRegistry {

    @SubscribeEvent
    public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
        BlockEntityRenderers.register(ModBlockEntities.ENDER_TAP, EnderTapTileEntityRenderer::new);


    }
}

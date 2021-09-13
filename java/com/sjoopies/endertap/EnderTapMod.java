package com.sjoopies.endertap;


import com.sjoopies.endertap.network.ModPacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MODID)
public class EnderTapMod {

    public EnderTapMod() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        //FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);


    }

    private void setup(final FMLCommonSetupEvent event) {
        ModPacketHandler.registerPackets();
    }

}

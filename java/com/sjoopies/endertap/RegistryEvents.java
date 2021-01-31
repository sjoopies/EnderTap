package com.sjoopies.endertap;


import com.sjoopies.endertap.block.ModBlocks;
import com.sjoopies.endertap.tileentity.ModTileEntities;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {
	
	@SubscribeEvent
	public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(ModBlocks.blockRegisters());
	}

	@SubscribeEvent
	public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ModBlocks.itemBlockRegisters());
	}

	@SubscribeEvent
	public static void registerTE(RegistryEvent.Register<TileEntityType<?>> evt) {
		evt.getRegistry().registerAll(ModTileEntities.TERegisters());
	}
}

package com.sjoopies.endertap.block;

import com.sjoopies.endertap.Reference;
import com.sjoopies.endertap.item.EnderTapBlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MODID)
public class ModBlocks {
    // eTODO fix this
    private static final Block.Properties ENDER_TAP_MAT = Block.Properties.of(Material.STONE).strength(20f, 500f);

    public static Block ender_tap = new EnderTapBlock(ENDER_TAP_MAT, "ender_tap");

    public static Block[] blockRegisters() {
        return new Block[]{ender_tap};
    }

    public static Item[] itemBlockRegisters() {
        Item[] items = {new EnderTapBlockItem(ender_tap, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)).setRegistryName(ender_tap.getRegistryName())};
//				new Item[blockRegisters().length];
//		for (int i = 0; i < items.length; i++) {
//
//			items[i] = new BlockItem(blockRegisters()[i], new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS))
//					.setRegistryName(blockRegisters()[i].getRegistryName());
//		}
        return items;

    }
}

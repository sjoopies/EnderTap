package com.sjoopies.endertap.block;

import com.sjoopies.endertap.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Reference.MODID)
public class ModBlocks {

	private static Block.Properties ENDER_TAP_MAT = Block.Properties.create(Material.ROCK).hardnessAndResistance(20f, 500f);

	public static Block ender_tap = new EnderTapBlock(ENDER_TAP_MAT, "ender_tap");

	public static Block[] blockRegisters() {
		return new Block[] { ender_tap };
	}

	public static Item[] itemBlockRegisters() {
		Item[] items = new Item[blockRegisters().length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new BlockItem(blockRegisters()[i], new Item.Properties().group(ItemGroup.DECORATIONS))
					.setRegistryName(blockRegisters()[i].getRegistryName());
		}
		return items;

	}
}

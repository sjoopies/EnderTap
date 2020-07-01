package com.sjoopies.endertap.block;

import com.sjoopies.endertap.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class ModBlock extends Block {
	public ModBlock(Properties properties, String registryName) {
		super(properties);
		init(registryName);
	}

	public ModBlock(String registryName, Material material) {
		super(Block.Properties.create(material));
		init(registryName);
	}

	private void init(String registryName) {
		setRegistryName(new ResourceLocation(Reference.MODID, registryName));

	}

}

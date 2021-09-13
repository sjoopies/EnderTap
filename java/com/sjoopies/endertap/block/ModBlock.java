package com.sjoopies.endertap.block;

import com.sjoopies.endertap.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;


public abstract class ModBlock extends BaseEntityBlock {

    public ModBlock(Properties properties, String registryName) {
        super(properties);
        init(registryName);
    }

    public ModBlock(String registryName, Material material) {
        super(Block.Properties.of(material));
        init(registryName);
    }

    private void init(String registryName) {
        setRegistryName(new ResourceLocation(Reference.MODID, registryName));

    }

}

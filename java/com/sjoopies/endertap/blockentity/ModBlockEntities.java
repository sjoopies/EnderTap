package com.sjoopies.endertap.blockentity;

import com.sjoopies.endertap.Reference;
import com.sjoopies.endertap.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class ModBlockEntities {

    public static final BlockEntityType<EnderTapBlockEntity> ENDER_TAP = BlockEntityType.Builder.of(EnderTapBlockEntity::new, ModBlocks.ender_tap).build(null);

    public static BlockEntityType[] blockEntityRegisters() {
        ENDER_TAP.setRegistryName(Reference.MODID, "ender_tap");
        return new BlockEntityType[]{ENDER_TAP};
    }

}

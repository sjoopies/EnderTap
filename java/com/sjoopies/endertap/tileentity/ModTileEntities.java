package com.sjoopies.endertap.tileentity;

import com.sjoopies.endertap.Reference;
import com.sjoopies.endertap.block.ModBlocks;

import net.minecraft.tileentity.TileEntityType;

public class ModTileEntities {
	
	public static final TileEntityType<EnderTapTileEntity> ENDER_TAP = TileEntityType.Builder
			.create(EnderTapTileEntity::new, ModBlocks.ender_tap).build(null);

	public static TileEntityType[] TERegisters() {
		ENDER_TAP.setRegistryName(Reference.MODID, "ender_tap");
		return new TileEntityType[] { ENDER_TAP };
	}
	
}

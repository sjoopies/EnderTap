package com.sjoopies.endertap.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.sjoopies.endertap.tileentity.EnderTapTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderTapBlock extends ModBlock {
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

	public EnderTapBlock(Properties properties, String registryName) {
		super(properties, registryName);

	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {

		if (player.isSneaking()) {

			((EnderTapTileEntity) worldIn.getTileEntity(pos)).setPlayerID(player);

			worldIn.playSound(player, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_END_PORTAL_FRAME_FILL,
					SoundCategory.BLOCKS, 1.0F, 1.0F);

		} else {
			PlayerEntity pe = ((EnderTapTileEntity) worldIn.getTileEntity(pos)).getPlayer();
			if (worldIn.isRemote && pe != null)
				player.sendStatusMessage(new StringTextComponent("Linked to " + pe.getGameProfile().getName()), true);

		}
		return ActionResultType.SUCCESS;
	}

	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return calcRedstoneFromInventory((EnderTapTileEntity) worldIn.getTileEntity(pos));
	}

	public static int calcRedstoneFromInventory(@Nullable EnderTapTileEntity enderTapTileEntity) {
		if (enderTapTileEntity == null) {
			return 0;
		} else {
			int i = 0;
			float f = 0.0F;

			for (int j = 0; j < enderTapTileEntity.getItemHandler().getSlots(); ++j) {
				ItemStack itemstack = enderTapTileEntity.getItemHandler().getStackInSlot(j);
				if (!itemstack.isEmpty()) {
					f += (float) itemstack.getCount() / (float) Math.min(64, itemstack.getMaxStackSize());
					++i;
				}
			}

			f = f / (float) enderTapTileEntity.getItemHandler().getSlots();
			return MathHelper.floor(f * 14.0F) + (i > 0 ? 1 : 0);
		}
	}

	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new EnderTapTileEntity();
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		for (int i = 0; i < 3; ++i) {
			int j = rand.nextInt(2) * 2 - 1;
			int k = rand.nextInt(2) * 2 - 1;
			double d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
			double d1 = (double) ((float) pos.getY() + rand.nextFloat());
			double d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) k;
			double d3 = (double) (rand.nextFloat() * (float) j);
			double d4 = ((double) rand.nextFloat() - 0.5D) * 0.125D;
			double d5 = (double) (rand.nextFloat() * (float) k);
			worldIn.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
		}

	}
}

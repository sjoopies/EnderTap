package com.sjoopies.endertap.tileentity;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public abstract class ModInventoryTileEntity extends ModTileEntity {

	protected SimpleItemStackHandler itemHandler = createItemHandler();
	private final LazyOptional<IItemHandler> automationItemHandler = LazyOptional.of(() -> itemHandler);

	public ModInventoryTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public void readPacketNBT(CompoundNBT tag) {
		itemHandler = createItemHandler();
		itemHandler.deserializeNBT(tag);
	}

	@Override
	public void writePacketNBT(CompoundNBT tag) {
		tag.merge(itemHandler.serializeNBT());
	}

	public abstract int getSizeInventory();

	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true);
	}

	public final IItemHandlerModifiable getItemHandler() {
		return itemHandler;
	}

	public final RecipeWrapper getRecipeWrapper() {
		return new RecipeWrapper(getItemHandler());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, automationItemHandler);
	}

	/*
	 * Extension of ItemStackHandler that uses our own slot array, allows for
	 * control of writing, allows control over stack limits, and allows for
	 * itemstack-slot validation
	 */
	protected static class SimpleItemStackHandler extends ItemStackHandler {

		private final boolean allowWrite;
		private final ModInventoryTileEntity tile;

		public SimpleItemStackHandler(ModInventoryTileEntity inv, boolean allowWrite) {
			super(inv.getSizeInventory());
			this.allowWrite = allowWrite;
			tile = inv;
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			if (allowWrite) {
				return super.insertItem(slot, stack, simulate);
			} else {
				return stack;
			}
		}

		@Nonnull
		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (allowWrite) {
				return super.extractItem(slot, amount, simulate);
			} else {
				return ItemStack.EMPTY;
			}
		}

		@Override
		public void onContentsChanged(int slot) {
			tile.markDirty();
		}
	}
}

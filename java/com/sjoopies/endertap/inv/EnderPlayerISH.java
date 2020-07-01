package com.sjoopies.endertap.inv;

import javax.annotation.Nonnull;

import com.sjoopies.endertap.tileentity.EnderTapTileEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class EnderPlayerISH extends ItemStackHandler {

	private final PlayerEntity player;
	private final EnderTapTileEntity tile;

	public EnderPlayerISH(PlayerEntity player, EnderTapTileEntity tile) {

		super(player.getInventoryEnderChest().getSizeInventory());
		this.player = player;
		this.tile = tile;

	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		if (player != null) {
			validateSlotIndex(slot);
			player.getInventoryEnderChest().setInventorySlotContents(slot, stack);
			onContentsChanged(slot);
		}
	}

	@Override
	public int getSlots() {
		if (player != null) {
			return player.getInventoryEnderChest().getSizeInventory();
		}
		return 0;
	}

	@Override
	@Nonnull
	public ItemStack getStackInSlot(int slot) {
		if (player != null) {
			validateSlotIndex(slot);
			return player.getInventoryEnderChest().getStackInSlot(slot);
		}
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (player != null) {
			if (stack.isEmpty())
				return ItemStack.EMPTY;

			if (!isItemValid(slot, stack))
				return stack;

			validateSlotIndex(slot);

			ItemStack existing = player.getInventoryEnderChest().getStackInSlot(slot);

			int limit = getStackLimit(slot, stack);

			if (!existing.isEmpty()) {
				if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
					return stack;

				limit -= existing.getCount();
			}

			if (limit <= 0)
				return stack;

			boolean reachedLimit = stack.getCount() > limit;

			if (!simulate) {
				if (existing.isEmpty()) {
					player.getInventoryEnderChest().setInventorySlotContents(slot,
							reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
				} else {
					existing.grow(reachedLimit ? limit : stack.getCount());
				}
				onContentsChanged(slot);
			}

			return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit)
					: ItemStack.EMPTY;
		}
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (player != null) {
			if (amount == 0)
				return ItemStack.EMPTY;

			validateSlotIndex(slot);

			ItemStack existing = player.getInventoryEnderChest().getStackInSlot(slot);

			if (existing.isEmpty())
				return ItemStack.EMPTY;

			int toExtract = Math.min(amount, existing.getMaxStackSize());

			if (existing.getCount() <= toExtract) {
				if (!simulate) {
					player.getInventoryEnderChest().setInventorySlotContents(slot, ItemStack.EMPTY);
					onContentsChanged(slot);
					return existing;
				} else {
					return existing.copy();
				}
			} else {
				if (!simulate) {
					player.getInventoryEnderChest().setInventorySlotContents(slot,
							ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
					onContentsChanged(slot);
				}

				return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
			}
		}
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
		return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return true;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = new CompoundNBT();
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
	}

	protected void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= player.getInventoryEnderChest().getSizeInventory())
			throw new RuntimeException("Slot " + slot + " not in valid range - [0,"
					+ player.getInventoryEnderChest().getSizeInventory() + ")");
	}

	@Override
	public void onContentsChanged(int slot) {
		if (player != null)
			player.getInventoryEnderChest().markDirty();
		tile.markDirty();
	}
}
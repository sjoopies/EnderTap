package com.sjoopies.endertap.inv;

import com.sjoopies.endertap.blockentity.EnderTapBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class EnderPlayerISH extends ItemStackHandler {

    private final Player player;
    private final EnderTapBlockEntity blockEntity;

    public EnderPlayerISH(Player player, EnderTapBlockEntity blockEntity) {
        super(player.getEnderChestInventory().getContainerSize());
        this.player = player;
        this.blockEntity = blockEntity;

    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (player != null) {
            validateSlotIndex(slot);
            player.getEnderChestInventory().setItem(slot, stack);
            onContentsChanged(slot);
        }
    }

    @Override
    public int getSlots() {
        if (player != null) {
            return player.getEnderChestInventory().getContainerSize();
        }
        return 0;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        if (player != null) {
            validateSlotIndex(slot);
            return player.getEnderChestInventory().getItem(slot);
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

            ItemStack existing = player.getEnderChestInventory().getItem(slot);

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
                    player.getEnderChestInventory().setItem(slot,
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

            ItemStack existing = player.getEnderChestInventory().getItem(slot);

            if (existing.isEmpty())
                return ItemStack.EMPTY;

            int toExtract = Math.min(amount, existing.getMaxStackSize());

            if (existing.getCount() <= toExtract) {
                if (!simulate) {
                    player.getEnderChestInventory().setItem(slot, ItemStack.EMPTY);
                    onContentsChanged(slot);
                    return existing;
                } else {
                    return existing.copy();
                }
            } else {
                if (!simulate) {
                    player.getEnderChestInventory().setItem(slot,
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

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
    }

    @Override
    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= player.getEnderChestInventory().getContainerSize())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0,"
                    + player.getEnderChestInventory().getContainerSize() + ")");
    }

    @Override
    public void onContentsChanged(int slot) {
        if (player != null) {
            player.getEnderChestInventory().setChanged();
        }
        blockEntity.setChanged();
    }
}
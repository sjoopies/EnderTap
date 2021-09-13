package com.sjoopies.endertap.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ModInventoryBlockEntity extends ModBlockEntity {

    protected SimpleItemStackHandler itemHandler = createItemHandler();
    private final LazyOptional<IItemHandler> automationItemHandler = LazyOptional.of(() -> itemHandler);

    public ModInventoryBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    @Override
    public void loadPacketTag(CompoundTag tag) {
        itemHandler = createItemHandler();
        itemHandler.deserializeNBT(tag);
    }

    @Override
    public void savePacketTag(CompoundTag tag) {
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
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, automationItemHandler);
    }

    /*
     * Extension of ItemStackHandler that uses our own slot array, allows for
     * control of writing, allows control over stack limits, and allows for
     * itemstack-slot validation
     */
    protected static class SimpleItemStackHandler extends ItemStackHandler {

        private final boolean allowWrite;
        private final ModInventoryBlockEntity blockEntity;

        public SimpleItemStackHandler(ModInventoryBlockEntity inv, boolean allowWrite) {
            super(inv.getSizeInventory());
            this.allowWrite = allowWrite;
            blockEntity = inv;
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
            blockEntity.setChanged();
        }
    }
}

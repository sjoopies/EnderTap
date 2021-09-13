package com.sjoopies.endertap.blockentity;

import com.sjoopies.endertap.inv.EnderPlayerISH;
import com.sjoopies.endertap.network.EnderTapPacket;
import com.sjoopies.endertap.network.ModPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
import java.util.UUID;

public class EnderTapBlockEntity extends ModBlockEntity implements Container {

    protected ItemStackHandler itemHandler = createBlank();
    private LazyOptional<IItemHandler> automationItemHandler = LazyOptional.of(() -> itemHandler);

    private UUID playerID = null;
    private String playerUsername = "";

    private int lastSize = 0;

    public EnderTapBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENDER_TAP, pos, state);
    }

    public void setItemHandler(boolean zero) {
        if (zero) {
            itemHandler = createBlank();
        } else {
            itemHandler = playerID != null ? createPlayerISH() : createBlank();
        }
        automationItemHandler = LazyOptional.of(() -> itemHandler);
    }

    public void setPlayerID(Player player) {
        playerID = player.getGameProfile().getId();
        playerUsername = player.getGameProfile().getName();
        setItemHandler(false);
        setChanged();
    }

    @Override
    public int getContainerSize() {
        return itemHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        return itemHandler.getStackInSlot(0) == null || itemHandler.getStackInSlot(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack stack = itemHandler.getStackInSlot(slot);
        return stack.isEmpty() ? ItemStack.EMPTY : stack.split(count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack s = getItem(index);
        if (s.isEmpty()) return ItemStack.EMPTY;
        setItem(index, ItemStack.EMPTY);
        return s;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        ModPacketHandler.sendToNearby(level, getBlockPos(), new EnderTapPacket(getPlayerUsername(), getBlockPos()));
    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return false;
    }

    public Player getPlayer() {
        if (playerID != null) {

            if (level != null && level.players() != null && level.players().size() > 0
                    && level.getPlayerByUUID(playerID) != null) {
                return level.getPlayerByUUID(playerID);
            }

        }
        return null;
    }

    @Override
    public void savePacketTag(CompoundTag cmp) {

        if (playerID != null)
            cmp.putUUID("playerID", playerID);
        cmp.putString("playerUsername", playerUsername);
    }

    @Override
    public void loadPacketTag(CompoundTag cmp) {
        if (cmp == null) {
            return;
        }
        if (cmp.get("playerID") != null && cmp.getUUID("playerID") != null)
            playerID = cmp.getUUID("playerID");
        playerUsername = cmp.getString("playerUsername");
        setItemHandler(false);
    }

    public boolean hasPlayer() {
        return playerID != null;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    protected BlankISH createBlank() {
        return new BlankISH();
    }

    protected ItemStackHandler createPlayerISH() {
        return playerID != null && getPlayer() != null ? new EnderPlayerISH(getPlayer(), this) : createBlank();
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


    public static void enterTapTick(Level level, BlockPos blockPos, BlockState state, EnderTapBlockEntity blockEntity) {
        if (blockEntity.getPlayer() != null) {
            if (blockEntity.getAmount() != blockEntity.lastSize) {
                blockEntity.setChanged();
                blockEntity.lastSize = blockEntity.getAmount();
            }
        }
        if (blockEntity.getPlayer() == null) {
            blockEntity.setItemHandler(true);
        } else {
            if (blockEntity.isZeroInventory()) {
                blockEntity.setItemHandler(false);
            }
        }
    }

    private boolean isZeroInventory() {
        return itemHandler instanceof BlankISH;
    }

    @Override
    public void clearContent() {

    }

    protected static class BlankISH extends ItemStackHandler {

        private final boolean allowWrite;

        public BlankISH() {
            super(1);
            this.allowWrite = false;
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

        @Override
        public int getSlotLimit(int slot) {
            return 0;
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

    }

    private int getAmount() {
        int stack = 0;

        for (int i = 0; i < getPlayer().getEnderChestInventory().getContainerSize(); i++) {
            stack += getPlayer().getEnderChestInventory().getItem(i).getCount();
        }
        return stack;
    }

    public void setPlayerName(String str) {
        playerUsername = str;
    }
}

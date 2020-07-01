package com.sjoopies.endertap.tileentity;

import java.util.UUID;

import javax.annotation.Nonnull;

import com.sjoopies.endertap.inv.EnderPlayerISH;
import com.sjoopies.endertap.network.EnderTapPacket;
import com.sjoopies.endertap.network.ModPacketHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class EnderTapTileEntity extends ModTileEntity implements ITickableTileEntity {

	protected ItemStackHandler itemHandler = createBlank();
	private LazyOptional<IItemHandler> automationItemHandler = LazyOptional.of(() -> itemHandler);

	private UUID playerID = null;
	private String playerUsername = "";

	private int lastSize = 0;

	public EnderTapTileEntity() {
		super(ModTileEntities.ENDER_TAP);
	}

	public void setItemHandler(boolean zero) {
		if (zero) {
			itemHandler = createBlank();
		} else {
			itemHandler = playerID != null ? createPlayerISH() : createBlank();
		}
		automationItemHandler = LazyOptional.of(() -> itemHandler);
	}

	public void setPlayerID(PlayerEntity player) {
		playerID = player.getGameProfile().getId();
		playerUsername = player.getGameProfile().getName();
		setItemHandler(false);
		markDirty();
	}

	public void markDirty() {
		super.markDirty();
		ModPacketHandler.sendToNearby(world, pos, new EnderTapPacket(getPlayerUsername(), getPos()));
	}

	public PlayerEntity getPlayer() {
		if (playerID != null) {
			if (world != null && world.getPlayers() != null && world.getPlayers().size() > 0
					&& world.getPlayerByUuid(playerID) != null) {
				return world.getPlayerByUuid(playerID);
			}

		}
		return null;
	}

	public void writePacketNBT(CompoundNBT cmp) {
		if (playerID != null)
			cmp.putUniqueId("playerID", playerID);
		cmp.putString("playerUsername", playerUsername);
	}

	public void readPacketNBT(CompoundNBT cmp) {
		if (cmp.getUniqueId("playerID") != null)
			playerID = cmp.getUniqueId("playerID");
		playerUsername = cmp.getString("playerUsername");
		setItemHandler(false);
		if (getPlayer() != null) {

		}
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
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, automationItemHandler);
	}

	@Override
	public void tick() {
		if (getPlayer() != null) {
			if (getAmount() != lastSize) {
				markDirty();
				lastSize = getAmount();
			}
		}
		if (getPlayer() == null) {
			setItemHandler(true);
		} else {
			if (isZeroInventory()) {
				setItemHandler(false);
			}
		}
	}

	private boolean isZeroInventory() {
		return itemHandler instanceof BlankISH;
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
		for (int i = 0; i < getPlayer().getInventoryEnderChest().getSizeInventory(); i++) {
			stack += getPlayer().getInventoryEnderChest().getStackInSlot(i).getCount();
		}
		return stack;
	}

	public void setPlayerName(String str) {
		playerUsername = str;
	}
}

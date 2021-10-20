package ru.restudios.industrialise.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.other.SidedInventory;
import ru.restudios.industrialise.other.Tags;
import ru.restudios.industrialise.other.UpgradableTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThunderChargerTile extends UpgradableTile implements ITickableTileEntity {

    private final SidedInventory inventory = createHandler();

    public ThunderChargerTile(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public ThunderChargerTile(){
        this(Industrialise.DeferredEvents.THUNDER_CHARGER_TILE.get());
    }

    private SidedInventory createHandler(){
        return new SidedInventory(4,true,true,
                SidedInventory.Settings.createGUI((integer, stack) -> {
                    switch (integer){
                        case 0: return stack.getItem() == Industrialise.DeferredEvents.ENERGY_POWDER.get();
                        case 1: return stack.getItem() == Industrialise.DeferredEvents.GLASS_LENSE.get();
                        case 2: return stack.getItem().is(Tags.ItemTags.UPGRADES);
                        default:
                        case 3: return false;
                    }
                })

                );
    }

    @Override
    public void applyFirstLevel() { }

    @Override
    public void applySecondLevel() { }

    @Override
    public void applyThirdLevel() { }

    @Override
    public ItemStack itemInUpgrade() {
        return inventory.getItem(2);
    }

    @Override
    public void tick() {
        upgradeTick();
    }

    private boolean valid = true;

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return inventory.asHandler(side).cast();
        }
        final CapabilityDispatcher disp = getCapabilities();
        return !valid || disp == null ? LazyOptional.empty() : disp.getCapability(cap, side);
    }

    @Override
    protected void reviveCaps() {
        valid = true;
        super.reviveCaps();
    }

    @Override
    protected void invalidateCaps() {
        this.valid = false;
        final CapabilityDispatcher disp = getCapabilities();
        if (disp != null)
            disp.invalidate();
    }
}

package ru.restudios.industrialise.tileentities;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.items.other.BatteryItem;
import ru.restudios.industrialise.other.ICraftMachine;
import ru.restudios.industrialise.other.REUtils;
import ru.restudios.industrialise.other.SidedInventory;
import ru.restudios.industrialise.other.Tags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DisenergizerTile extends TileEntity implements ITickableTileEntity, ICraftMachine {

    public ComputerTileEntity computer;
    public BatteryTileEntity batteryServer;

    public SidedInventory inventory = createSided();

    private SidedInventory createSided() {
        return new SidedInventory(3,true,false,
                SidedInventory.Settings.defaultInputSide(0,Industrialise.DeferredEvents.ENERGY_DUST.get(),Industrialise.DeferredEvents.ENERGY_BLOCK_ITEM.get()),
                SidedInventory.Settings.defaultOutputSide(1),
                SidedInventory.Settings.createGUI((integer, stack) -> {
                    switch (integer){
                        case 0: return stack.getItem() == Industrialise.DeferredEvents.ENERGY_DUST.get() || stack.getItem() == Industrialise.DeferredEvents.ENERGY_BLOCK_ITEM.get();
                        case 1: return false;
                        case 2: return stack.getItem().is(Tags.ItemTags.BATTERIES);
                    }
                    return false;
                })){

            @Override
            public void setChanged() {
                DisenergizerTile.this.setChanged();
            }
        }.setSlotLimit(2,1);
    }

    public DisenergizerTile(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public DisenergizerTile(){
        this(Industrialise.DeferredEvents.DISENERGIZER_TILE.get());
    }

    private boolean structure;

    public void structureCompleted(){
        structure = true;
    }

    public void structureDestroyed(){
        structure = false;
    }

    private int timeout = 0;



    @Override
    public void tick() {
        assert level != null;
        if (!level.isClientSide){
            timeout = REUtils.keepInRange(timeout-1,0,20);
            if (canCraft() && timeoutLeft() == 0){
                ItemStack energyDust = inventory.getItem(0);
                ItemStack output = inventory.getItem(1);
                ItemStack battery = inventory.getItem(2);


                if (output.getItem() == Items.AIR || output == ItemStack.EMPTY){
                    output = new ItemStack(Industrialise.DeferredEvents.ENERGY_POWDER.get(),1);
                    inventory.setItem(1,output);
                } else { output.grow(1); }
                if (battery.getItem() != Items.AIR && battery.getCount() >= 1 && !structure){
                    int count = 1000;
                    if (energyDust.getItem() == Industrialise.DeferredEvents.ENERGY_BLOCK_ITEM.get()){
                        count = 4000;
                    }
                    BatteryItem item = REUtils.castOrNull(BatteryItem.class,battery.getItem());
                    item.addEnergy(battery,count);
                    inventory.setItem(2,battery);
                }
                if (this.batteryServer != null && structure){
                    int energy = 1000;
                    if (energyDust.getItem() == Industrialise.DeferredEvents.ENERGY_BLOCK_ITEM.get()){
                        energy = 4000;
                    }
                    this.batteryServer.receiveEnergy(energy,false);
                }
                energyDust.shrink(1);
                timeout = 15;
            }
        }

    }



    @Override
    public boolean canCraft() {
        ItemStack energyDust = inventory.getItem(0);
        ItemStack output = inventory.getItem(1);
        if (energyDust == null || energyDust == ItemStack.EMPTY){ return false; }
        if (output.getCount()>=64){ return false; }
        return energyDust.getCount() >= 1;
    }


    @Override
    public void startCraft() {

    }

    @Override
    public int timeoutLeft() {
        return timeout;
    }




    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return inventory.asHandler(side).cast();
        }
        return super.getCapability(cap, side);
    }
}

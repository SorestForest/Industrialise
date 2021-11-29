package ru.restudios.industrialise.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.items.other.BatteryItem;
import ru.restudios.industrialise.other.REUtils;
import ru.restudios.industrialise.other.SidedInventory;
import ru.restudios.industrialise.other.Tags;
import ru.restudios.industrialise.other.capabilities.ForestEnergyCapability;
import ru.restudios.industrialise.other.capabilities.ForestEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BatteryTileEntity extends TileEntity implements ITickableTileEntity {

    public final SidedInventory inventory = createSided();
    private ForestEnergyStorage storage = createStorage(0);

    public static final int START_CAPACITY = 300000;
    public static final int SLOTS = 1;

    private SidedInventory createSided() {
        return new SidedInventory(SLOTS,false,false,
                SidedInventory.Settings.createGUI((integer, stack) -> stack.getItem().is(Tags.ItemTags.BATTERIES))){

            @Override
            public void setChanged() {
                BatteryTileEntity.this.setChanged();
            }
        };
    }

    private ForestEnergyStorage createStorage(int energy) {
        return new ForestEnergyStorage(START_CAPACITY,4000,4000,energy);
    }

    public BatteryTileEntity(TileEntityType<?> type) {
        super(type);
    }

    public BatteryTileEntity(){
        this(Industrialise.TileEntities.BATTERY_TILE.get());
    }

    private final LazyOptional<ForestEnergyStorage> energyStorage = LazyOptional.of(()->storage);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (side == null){
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
                return inventory.asHandler(null).cast();
            }
        }
        if (cap == ForestEnergyCapability.FOREST_ENERGY){
            return energyStorage.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        ItemStack in = inventory.getItem(0);
        if (in == null || in == ItemStack.EMPTY){ return; }
        BatteryItem battery = REUtils.castOrNull(BatteryItem.class,in.getItem());
        if (battery != null){
            int inBattery = battery.getEnergy(in);
            int canAddToBattery = battery.getCapacity()-inBattery;
            if (canAddToBattery > 500){ canAddToBattery = 500;}
            int extracted = this.storage.extractEnergy(canAddToBattery,false);
            if (extracted > 0)
                battery.addEnergy(in,extracted);

        }
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        storage = createStorage(p_230337_2_.getInt("Energy"));
        super.load(p_230337_1_, p_230337_2_);
    }

    @Override
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        p_189515_1_.putInt("Energy",storage.getEnergyStored());
        return super.save(p_189515_1_);
    }

    public int extractEnergy(int toExtract,boolean simulate){
        return storage.extractEnergy(toExtract, simulate);
    }

    public int getEnergy(){
        return storage.getEnergyStored();
    }

    public int receiveEnergy(int toReceive,boolean simulate){
        assert level != null;
        if (level.isClientSide)
                System.out.println("Energy receiving!");
        return storage.receiveEnergy(toReceive, simulate);
    }
}

package ru.restudios.industrialise.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.other.SidedInventory;
import ru.restudios.industrialise.other.multiblock.IMultiBlockPart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnergyCollectorTileEntity extends TileEntity implements IMultiBlockPart {

    public EnergyCollectorTileEntity(TileEntityType<?> type) {
        super(type);
    }

    public EnergyCollectorTileEntity(){
        this(Industrialise.TileEntities.ENERGY_COLLECTOR_TILE.get());
    }

    public SidedInventory inventory = createSided();

    private SidedInventory createSided() {
        return new SidedInventory(5,false,false,
                SidedInventory.Settings.createGUI(SidedInventory.Settings.validator(Industrialise.Items.COIL.get()))){

            @Override
            public void setChanged() {
                EnergyCollectorTileEntity.this.setChanged();
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return inventory.asHandler(side).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public boolean isReady() {
        return !inventory.isEmpty();
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        inventory.deserializeNBT(p_230337_2_.getCompound("inv"));
        super.load(p_230337_1_, p_230337_2_);
    }

    @Override
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        p_189515_1_.put("inv",inventory.serializeNBT());
        return super.save(p_189515_1_);
    }
}

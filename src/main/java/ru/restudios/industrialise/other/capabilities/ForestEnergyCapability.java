package ru.restudios.industrialise.other.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * (C) Copyright REStudios
 *
 * @author SorestForest
 */
public class ForestEnergyCapability {

    @CapabilityInject(ForestEnergyStorage.class)
    public static Capability<ForestEnergyStorage> FOREST_ENERGY = null;

    public static void register(){

        CapabilityManager.INSTANCE.register(ForestEnergyStorage.class, new Capability.IStorage<ForestEnergyStorage>()
                {
                    @Override
                    public INBT writeNBT(Capability<ForestEnergyStorage> capability, ForestEnergyStorage instance, Direction side)
                    {
                        return IntNBT.valueOf(instance.getEnergyStored());
                    }

                    @Override
                    public void readNBT(Capability<ForestEnergyStorage> capability, ForestEnergyStorage instance, Direction side, INBT nbt)
                    {
                        if (instance == null)
                            throw new IllegalArgumentException("Can not deserialize null object");
                        instance.energy = ((IntNBT) nbt).getAsInt();
                    }
                },
                () -> new ForestEnergyStorage(1000));
    }
}

package ru.restudios.industrialise.other.capabilities;

/**
 * (C) Copyright REStudios
 *
 * @author SorestForest
 */
public class ForestEnergyStorage {

    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public ForestEnergyStorage(int capacity)
    {
        this(capacity, capacity, capacity, 0);
    }

    public ForestEnergyStorage(int capacity, int maxTransfer)
    {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public ForestEnergyStorage(int capacity, int maxReceive, int maxExtract)
    {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public ForestEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
    {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0 , Math.min(capacity, energy));
    }


    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }


    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    public int getEnergyStored()
    {
        return energy;
    }

    public int getMaxEnergyStored()
    {
        return capacity;
    }

    public boolean canExtract()
    {
        return this.maxExtract > 0;
    }

    public boolean canReceive()
    {
        return this.maxReceive > 0;
    }
}

package ru.restudios.industrialise.items.other;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import ru.restudios.industrialise.other.REUtils;
import ru.restudios.industrialise.other.RegistryHelper;

public class BatteryItem extends Item {


    private final int capcacity;


    public BatteryItem(int capacity) {
        super(RegistryHelper.getUncommonItemProperties().durability(capacity));
        this.capcacity = capacity;

    }


    @Override
    public void inventoryTick(ItemStack stackIn, World worldIn, Entity playerIn, int p_77663_4_, boolean p_77663_5_) {
        if (!worldIn.isClientSide()){
            setDamage(stackIn,capcacity-stackIn.getOrCreateTag().getInt("Energy"));
        }
    }



    public void setEnergy(ItemStack in, int energy){
        CompoundNBT nbt = in.getOrCreateTag();
        nbt.putInt("Energy",Math.min(capcacity,energy));
        in.setTag(nbt);
        in.setDamageValue(getCapacity()-energy);
    }


    public int getEnergy(ItemStack in){
        return in.getOrCreateTag().getInt("Energy");
    }

    public void addEnergy(ItemStack in, int energy){
        setEnergy(in,getEnergy(in)+energy);
    }


    public int extractEnergy(ItemStack in,int energy){
        int maximalExtract = 500;
        energy = REUtils.keepInRange(energy,0, maximalExtract);
        if (getEnergy(in)-energy >=0){
            addEnergy(in,-energy);
            return energy;
        }
        return 0;
    }

    public int getCapacity() {
        return capcacity;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return capcacity;
    }
}

package ru.restudios.industrialise.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.other.Tags;

import java.util.Arrays;
import java.util.List;

public class Slots {


    public static class BatterySlot extends Slot {

        public BatterySlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return p_75214_1_.getItem().is(Tags.ItemTags.BATTERIES);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    public static class UpgradeSlot extends Slot {


        public UpgradeSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        }

        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return p_75214_1_.getItem().is(Tags.ItemTags.UPGRADES);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    public static class ItemSlot extends Slot {

        private final int stack;
        private final List<Item> items;

        public ItemSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, int maxStackSize, Item... items) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
            this.stack = maxStackSize;
            this.items = Arrays.asList(items);
        }

        @Override
        public int getMaxStackSize() {
            return stack;
        }

        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return items.contains(p_75214_1_.getItem());
        }
    }


    public static class OutputSlot extends Slot {

        private final int stackSize;

        public OutputSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_,int stackSize) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
            this.stackSize = stackSize;
        }

        @Override
        public boolean mayPlace(ItemStack p_75214_1_) {
            return false;
        }

        @Override
        public int getMaxStackSize() {
            return stackSize;
        }
    }

    public static class ThunderChargerSlot extends Slot {

        private final int stackSize;
        private final int index;

        public ThunderChargerSlot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_,int stackSize) {
            super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
            this.stackSize = stackSize;
            this.index = p_i1824_2_;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public int getMaxStackSize() {
            return stackSize;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            switch (getIndex()){
                case 1:  return stack.getItem() == Industrialise.Items.ENERGY_POWDER.get();
                case 0:  return stack.getItem() == Industrialise.Items.GLASS_LENSE.get();
                default: return false;
            }
        }
    }



}

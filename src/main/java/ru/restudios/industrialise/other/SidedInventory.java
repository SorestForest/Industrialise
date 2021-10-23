package ru.restudios.industrialise.other;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiPredicate;

public class SidedInventory implements ISidedInventory, INBTSerializable<CompoundNBT> {

    private NonNullList<ItemStack> stacks;
    private int size;
    private final Settings[] settings;
    private final int[] allSlots;
    private final boolean insert;
    private final boolean extract;
    public Direction[] allDirections;

    private HashMap<Integer,Integer> slotLimits;

    public SidedInventory(int size, boolean defaultInsert, boolean defaultExtract, Settings... settings) {
        this.stacks = NonNullList.withSize(size,ItemStack.EMPTY);
        this.size = size;
        this.settings = settings;
        allSlots = REUtils.getFrom(0,size);
        insert = defaultInsert;
        extract = defaultExtract;
        ArrayList<Direction> directions = new ArrayList<>();
        for (Settings setting : settings) {
            directions.addAll(Arrays.asList(setting.getSides()));
        }
        allDirections = directions.toArray(new Direction[]{});
    }

    


    @Nullable
    public Settings fromDirection(Direction direction){
        for (Settings setting : settings) {
            for (Direction d : setting.getSides()){
                if (d == direction){
                    return setting;
                }
            }
        }
        return null;
    }

    @Nullable
    public Settings getGUISettings(){
        for (Settings setting : settings){
            if (setting.isGUIParameters()){
                return setting;
            }
        }
        return null;
    }

    @Override
    public int[] getSlotsForFace(Direction p_180463_1_) {
        Settings loaded = fromDirection(p_180463_1_);
        if (loaded == null){
            return allSlots;
        }
        if (loaded.isGUIParameters()){
            return allSlots;
        }
        return loaded.getSlots();
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_180462_1_, ItemStack p_180462_2_, @Nullable Direction p_180462_3_) {
        if (p_180462_3_ == null){
            Settings setting = getGUISettings();
            if (setting == null) {
                return insert; }
            if (setting.canInsert(p_180462_1_,p_180462_2_)){
                int inStack = p_180462_2_.getCount();
                int inSlot = getItem(p_180462_1_).getCount();
                return slotLimits.getOrDefault(p_180462_1_, p_180462_2_.getMaxStackSize()) >= (inSlot + inStack);
            }

            return false;
        }
        Settings settings = fromDirection(p_180462_3_);
        if (settings == null) {
            return insert;
        }
        if (settings.canInsert(p_180462_1_,p_180462_2_)){
            int inStack = p_180462_2_.getCount();
            int inSlot = getItem(p_180462_1_).getCount();
            return slotLimits.getOrDefault(p_180462_1_, p_180462_2_.getMaxStackSize()) >= (inSlot + inStack);
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_180461_1_, ItemStack p_180461_2_, Direction p_180461_3_) {
        Settings settings = fromDirection(p_180461_3_);
        if (settings == null) { return extract; }
        return settings.canExtract();
    }

    @Override
    public int getContainerSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.stacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int p_70301_1_) {
        return stacks.get(p_70301_1_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_70304_1_) {
        return ItemStackHelper.takeItem(this.stacks, p_70304_1_);
    }

    @Override
    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        ItemStack itemstack = ItemStackHelper.removeItem(this.stacks, p_70298_1_, p_70298_2_);
        if (!itemstack.isEmpty()) {
            setChanged();
        }

        return itemstack;
    }

    @Override
    public void setItem(int p_70299_1_, ItemStack p_70299_2_) {
        this.stacks.set(p_70299_1_, p_70299_2_);
        setChanged();
    }

    @Override
    public void setChanged() {}

    @Override
    public boolean stillValid(PlayerEntity p_70300_1_) { return true; }

    @Override
    public void clearContent() { stacks = NonNullList.withSize(size,ItemStack.EMPTY); setChanged(); }


    public SidedInventory setSlotLimit(int slot,int stackLimit){
        if (slotLimits == null) {
            slotLimits = new HashMap<>();
        }
        slotLimits.put(slot,stackLimit);
        return this;
    }


    @Override
    public CompoundNBT serializeNBT()
    {
        ListNBT nbtTagList = new ListNBT();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (!stacks.get(i).isEmpty())
            {
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putInt("Slot", i);
                stacks.get(i).save(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        this.size = (nbt.contains("Size", Constants.NBT.TAG_INT) ? nbt.getInt("Size") : stacks.size());
        ListNBT tagList = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundNBT itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < stacks.size())
            {
                stacks.set(slot, ItemStack.of(itemTags));
            }
        }

    }

    public static class Settings {

        private final Direction[] sides;
        private final int[] slots;
        private final boolean canExtract;
        private final boolean canInsert;
        private final BiPredicate<Integer,ItemStack> validator;

        private Settings(@Nullable Direction[] sides, boolean canExtract, boolean canInsert, @Nullable BiPredicate<Integer, ItemStack> validator, int... slots) {
            this.sides = sides;
            if (slots == null || slots.length == 0){
                this.slots = new int[]{-1};
            }
            else { this.slots = slots; }
            this.canExtract = canExtract;
            this.canInsert = canInsert;
            this.validator = validator;
        }


        public static final Direction[] GUI_ONLY = new Direction[]{null};
        public static final Direction[] HORIZONTAL = new Direction[]{Direction.EAST,Direction.NORTH,Direction.SOUTH,Direction.WEST};
        public static final Direction[] VERTICAL = new Direction[]{Direction.UP,Direction.DOWN};
        public static final Direction[] MAIN_INPUT = new Direction[]{Direction.UP};
        public static final Direction[] MAIN_OUTPUT = new Direction[]{Direction.DOWN};
        public static final Direction[] ALL = REUtils.collapse(REUtils.collapse(HORIZONTAL,VERTICAL),GUI_ONLY);

        private boolean canExtract(){
            return canExtract;
        }

        private boolean isGUIParameters(){
            return sides == GUI_ONLY && slots[0] == -1;
        }

        private boolean canInsert(int slot, ItemStack stack){
            if (!canInsert){ return false; }
            if (validator == null) { return true; }
            return validator.test(slot,stack);
        }

        public int[] getSlots() {
            return slots;
        }

        public Direction[] getSides() {
            return sides;
        }

        public static Settings createGUI(BiPredicate<Integer,ItemStack> validator){
            return new Settings(GUI_ONLY,true,true,validator,-1);
        }

        public static Settings createSide(Direction[] sides, boolean extract, boolean insert, BiPredicate<Integer,ItemStack> validator, int... slots){
            return new Settings(sides,extract,insert,validator,slots);
        }

        public static Settings defaultInputSide(int slot,Item... items){
            return createSide(MAIN_INPUT,true,true,validator(items),slot);
        }

        public static Settings defaultOutputSide(int slot){
            return createSide(MAIN_OUTPUT,true,false,validator((Item) null),slot);
        }

        public static BiPredicate<Integer,ItemStack> validator(Item... items){
            return (integer, stack) -> Arrays.asList(items).contains(stack.getItem());
        }

    }
}

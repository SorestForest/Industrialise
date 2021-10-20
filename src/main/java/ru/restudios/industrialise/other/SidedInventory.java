package ru.restudios.industrialise.other;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiPredicate;

public class SidedInventory implements ISidedInventory {

    private NonNullList<ItemStack> stacks;
    private final int size;
    private final Settings[] settings;

    private final int[] allSlots;

    private final boolean insert;
    private final boolean extract;

    private LazyOptional<IItemHandlerModifiable>[] capability;
    private ArrayList<Direction> directions;

    public SidedInventory(int size, boolean defaultInsert, boolean defaultExtract, Settings... settings) {
        this.stacks = NonNullList.withSize(size,ItemStack.EMPTY);
        this.size = size;
        this.settings = settings;

        allSlots = REUtils.getFrom(0,size);
        insert = defaultInsert;
        extract = defaultExtract;
        directions = new ArrayList<>();
        for (Settings setting : settings) {
            directions.addAll(Arrays.asList(setting.getSides()));
        }
        capability = SidedInvWrapper.create(this,directions.toArray(new Direction[]{}));
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
            if (setting == null) { return insert; }
            return setting.canInsert(p_180462_1_,p_180462_2_);
        }
        Settings settings = fromDirection(p_180462_3_);
        if (settings == null) {
            return insert;
        }
        return settings.canInsert(p_180462_1_,p_180462_2_);
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
    }

    @Override
    public void setChanged() {}

    @Override
    public boolean stillValid(PlayerEntity p_70300_1_) { return true; }

    @Override
    public void clearContent() { stacks = NonNullList.withSize(size,ItemStack.EMPTY); }

    public LazyOptional<IItemHandlerModifiable> asHandler(Direction side){
        int j = 0;
        for (int i = 0; i < directions.size(); i++) {
            if (directions.get(i) == side){
                j = i;
            }
        }
        return capability[j];
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


        public static final Direction[] GUI_ONLY = null;
        public static final Direction[] HORIZONTAL = new Direction[]{Direction.EAST,Direction.NORTH,Direction.SOUTH,Direction.WEST};
        public static final Direction[] VERTICAL = new Direction[]{Direction.UP,Direction.DOWN};
        public static final Direction[] MAIN_INPUT = new Direction[]{Direction.UP};
        public static final Direction[] MAIN_OUTPUT = new Direction[]{Direction.DOWN};

        private boolean canExtract(){
            return canExtract;
        }

        private boolean isGUIParameters(){
            return sides == null && slots[0] == -1;
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
    }
}

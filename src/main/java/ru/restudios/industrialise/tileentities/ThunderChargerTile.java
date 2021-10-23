package ru.restudios.industrialise.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.other.REUtils;
import ru.restudios.industrialise.other.SidedInventory;
import ru.restudios.industrialise.other.Tags;
import ru.restudios.industrialise.other.UpgradableTile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ThunderChargerTile extends UpgradableTile implements ITickableTileEntity {

    public static final int SLOTS = 4;

    private final SidedInventory inventory = createHandler();

    private int needToCraft;
    private boolean autoCraft;


    public ThunderChargerTile(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
        needToCraft = 16;
        autoCraft = false;
    }

    public ThunderChargerTile(){
        this(Industrialise.DeferredEvents.THUNDER_CHARGER_TILE.get());
    }

    private SidedInventory createHandler(){
        return new SidedInventory(SLOTS,true,true,
                SidedInventory.Settings.createGUI((integer, stack) -> {
                    switch (integer){
                        case 1: return stack.getItem() == Industrialise.DeferredEvents.ENERGY_POWDER.get();
                        case 0: return stack.getItem() == Industrialise.DeferredEvents.GLASS_LENSE.get();
                        case 2: return stack.getItem().is(Tags.ItemTags.UPGRADES);
                        default:
                        case 3: return false;
                    }
                }),SidedInventory.Settings.defaultInputSide(0,Industrialise.DeferredEvents.GLASS_LENSE.get()),
                SidedInventory.Settings.defaultOutputSide(3),
                SidedInventory.Settings.createSide(SidedInventory.Settings.HORIZONTAL,true,true,
                        SidedInventory.Settings.validator(Industrialise.DeferredEvents.ENERGY_POWDER.get()),1)){

            @Override
            public void setChanged() {
                ThunderChargerTile.this.setChanged();
            }
        }.setSlotLimit(2,1);
    }

    @Override
    public void applyFirstLevel() {  }

    @Override
    public void applySecondLevel() { needToCraft = 10;}

    @Override
    public void applyThirdLevel() { needToCraft = 8; upgrade = true; }


    @Override
    public ItemStack itemInUpgrade() {
        return inventory.getItem(2);
    }

    @Override
    public void revertUpdates() {
        needToCraft = 16;
        autoCraft = false;
    }

    private int timeout = 0;

    @Override
    public void tick() {
        assert level != null;
        if (!level.isClientSide()){
            upgradeTick();
            autoCraft = upgrade && structure;
            timeout = REUtils.keepInRange(timeout-1,0,20);
            if (canCraft() && autoCraft){
                EntityType.LIGHTNING_BOLT.spawn(((ServerWorld) level), null, null,
                        worldPosition.above(), SpawnReason.TRIGGERED, true, true);
                startCraft();
                timeout = 20;
            }
        }

    }


    public void startCraft(){
        damageLense();
        removePowder();
        addPowder();
    }


    private boolean structure;
    private boolean upgrade;

    public void structureBuild(){
        structure = true;
    }

    public void structureDestroy(){
        structure = false;
    }


    public boolean canCraft() {
        assert level != null;
        boolean weatherCondition = level.isThundering();
        if (autoCraft){
            weatherCondition = true;
        }
        if (!weatherCondition) { return false; }
        if (timeout > 0) { return false;}
        ItemStack lenseStack = inventory.getItem(0);
        ItemStack powder = inventory.getItem(1);
        ItemStack outputStack = inventory.getItem(3);
        if (lenseStack.getCount() <= 0) { return false;}
        if (outputStack.getCount() >= 64) { return false; }
        return powder.getCount() >= needToCraft;
    }

    private void damageLense(){
        inventory.setItem(0,ItemStack.EMPTY);
    }

    private void removePowder(){ inventory.getItem(1).shrink(needToCraft); }

    private void addPowder(){
        ItemStack powderStack = inventory.getItem(3);
        if (powderStack.getItem() == Items.AIR) {
            inventory.setItem(3,new ItemStack(Industrialise.DeferredEvents.ENERGY_DUST.get(),1));
        }
        else {
            powderStack.grow(1);
        }
    }

    private boolean valid = true;

    private final LazyOptional<IItemHandlerModifiable>[] caps = SidedInvWrapper.create(inventory, SidedInventory.Settings.ALL);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            if (side == null){
                return caps[6].cast();
            }
            switch (side){
                case UP:
                    return caps[4].cast();
                case DOWN:
                    return caps[5].cast();
                case NORTH:
                    return caps[1].cast();
                case SOUTH:
                    return caps[2].cast();
                case EAST:
                    return caps[0].cast();
                case WEST:
                    return caps[3].cast();
            }

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

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT p_230337_2_) {
        super.load(p_230337_1_, p_230337_2_);
        inventory.deserializeNBT(p_230337_2_.getCompound("inv"));
    }

    @Override
    public CompoundNBT save(CompoundNBT p_189515_1_) {
        p_189515_1_.put("inv",inventory.serializeNBT());
        return super.save(p_189515_1_);
    }
}

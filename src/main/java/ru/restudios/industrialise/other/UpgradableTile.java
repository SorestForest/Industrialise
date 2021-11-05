package ru.restudios.industrialise.other;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import ru.restudios.industrialise.Industrialise;

/**
 * Makes tile entity upgradable using upgrades
 */
public abstract class UpgradableTile extends TileEntity {

    private final boolean include;

    private boolean firstTick = true;

    public UpgradableTile(TileEntityType<?> type){
        this(type,true);
    }

    /**
     * Creates instance
     * @param p_i48289_1_ tile entity type
     * @param includeLowerLevels does it need to execute code of lower methods?
     */

    public UpgradableTile(TileEntityType<?> p_i48289_1_, boolean includeLowerLevels) {
        super(p_i48289_1_);
        include = includeLowerLevels;
    }

    /**
     * Apply changes, when first level inserted into mechanism
     */
    public abstract void applyFirstLevel();

    /**
     * Like applyFirstLevel, but for second
     */
    public abstract void applySecondLevel();

    /**
     * Similar
     */
    public abstract void applyThirdLevel();

    /**
     * @return ItemStack from slot in upgrade slot
     */
    public abstract ItemStack itemInUpgrade();

    /**
     * Remove any upgrades here
     */
    public abstract void revertUpdates();


    private ItemStack previousStack = null;

    /**
     * Use this method in your tile entity#tick
     */
    public final void upgradeTick(){
        ItemStack in = itemInUpgrade();
        if (previousStack == null) { previousStack = in; }
        if (in.getItem() == Items.AIR){
            revertUpdates();
            return;
        }
        if (REUtils.consideredTheSameItem(in,previousStack) && !firstTick) {
            return;
        }
        firstTick = false;
        previousStack = in;
        revertUpdates();
        if (!in.getItem().is(Tags.ItemTags.UPGRADES)) { return; }
        if (in.getItem() == Industrialise.Items.UPGRADE_FIRST.get()){
            applyFirstLevel();
        }
        else if (in.getItem() == Industrialise.Items.UPGRADE_SECOND.get()){
            if (include){
                applyFirstLevel();
            }
            applySecondLevel();
        }
        else if (in.getItem() == Industrialise.Items.UPGRADE_THIRD.get()){
            if (include){
                applyFirstLevel();
                applySecondLevel();
            }
            applyThirdLevel();
        }
    }

}

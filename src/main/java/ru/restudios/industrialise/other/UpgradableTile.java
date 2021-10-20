package ru.restudios.industrialise.other;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import ru.restudios.industrialise.Industrialise;

public abstract class UpgradableTile extends TileEntity {

    public UpgradableTile(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public abstract void applyFirstLevel();
    public abstract void applySecondLevel();
    public abstract void applyThirdLevel();
    public abstract ItemStack itemInUpgrade();

    public final void upgradeTick(){
        ItemStack in = itemInUpgrade();
        if (!in.getItem().is(Tags.ItemTags.UPGRADES)) { return; }
        if (in.getItem() == Industrialise.DeferredEvents.UPGRADE_FIRST.get()){
            applyFirstLevel();
        }
        else if (in.getItem() == Industrialise.DeferredEvents.UPGRADE_SECOND.get()){
            applySecondLevel();
        }
        else if (in.getItem() == Industrialise.DeferredEvents.UPGRADE_THIRD.get()){
            applyThirdLevel();
        }
    }

}

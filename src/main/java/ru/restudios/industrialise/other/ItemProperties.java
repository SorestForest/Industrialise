package ru.restudios.industrialise.other;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import ru.restudios.industrialise.Industrialise;

public class ItemProperties {


    public static Item.Properties getDefault(){
        return new Item.Properties().rarity(Rarity.COMMON).stacksTo(64).tab(Industrialise.GROUP);
    }

    public static Item.Properties getUncommon(){
        return getDefault().rarity(Rarity.UNCOMMON);
    }

    public static Item.Properties getRare(){
        return getDefault().rarity(Rarity.RARE);
    }

    public static Item.Properties getEpic(){
        return getDefault().rarity(Rarity.EPIC);
    }

    public static Item getDefaultItem(){
        return new Item(getDefault());
    }

    public static Item getUncommonItem(){
        return new Item(getUncommon());
    }

    public static Item getRareItem(){
        return new Item(getRare());
    }

    public static Item getEpicItem(){
        return new Item(getEpic());
    }
}

package ru.restudios.industrialise.other;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import ru.restudios.industrialise.Industrialise;

public class RegistryHelper {


    public static Item.Properties getDefaultItemProperties(){
        return new Item.Properties().rarity(Rarity.COMMON).stacksTo(64).tab(Industrialise.GROUP);
    }

    public static Item.Properties getUncommonItemProperties(){
        return getDefaultItemProperties().rarity(Rarity.UNCOMMON);
    }

    public static Item.Properties getRareItemProperties(){
        return getDefaultItemProperties().rarity(Rarity.RARE);
    }

    public static Item.Properties getEpicItemProperties(){
        return getDefaultItemProperties().rarity(Rarity.EPIC);
    }

    public static Item getDefaultItem(){
        return new Item(getDefaultItemProperties());
    }

    public static Item getUncommonItem(){
        return new Item(getUncommonItemProperties());
    }

    public static Item getRareItem(){
        return new Item(getRareItemProperties());
    }

    public static Item getEpicItem(){
        return new Item(getEpicItemProperties());
    }

    public static AbstractBlock.Properties getMetalBlockProperties(){
        return AbstractBlock.Properties.of(Material.METAL).strength(4).sound(SoundType.METAL);
    }

    public static Block getMetalBlock(){
        return new Block(getMetalBlockProperties());
    }
}

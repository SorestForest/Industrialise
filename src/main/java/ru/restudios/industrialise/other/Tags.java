package ru.restudios.industrialise.other;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import ru.restudios.industrialise.Industrialise;

public class Tags {

    public static class ItemTags {

        public static final net.minecraftforge.common.Tags.IOptionalNamedTag<Item> UPGRADES = createModTag("upgrades");

        private static net.minecraftforge.common.Tags.IOptionalNamedTag<Item> createModTag(String name) {
            return net.minecraft.tags.ItemTags.createOptional(new ResourceLocation(Industrialise.MODID,name));
        }


    }


    public static class BlockTags {

    }
}

package ru.restudios.industrialise.other;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import ru.restudios.industrialise.Industrialise;

public class Tags {

    public static class ItemTags {

        public static final net.minecraftforge.common.Tags.IOptionalNamedTag<Item> UPGRADES = createModTag("upgrades");
        public static final net.minecraftforge.common.Tags.IOptionalNamedTag<Item> BATTERIES = createModTag("batteries");

        private static net.minecraftforge.common.Tags.IOptionalNamedTag<Item> createModTag(String name) {
            return net.minecraft.tags.ItemTags.createOptional(new ResourceLocation(Industrialise.MODID,name));
        }


    }


    public static class BlockTags {

        public static final net.minecraftforge.common.Tags.IOptionalNamedTag<Block> COMPUTER_ADDITION = createModTag("computer_addition");

        private static net.minecraftforge.common.Tags.IOptionalNamedTag<Block> createModTag(String computer_addition) {
            return net.minecraft.tags.BlockTags.createOptional(new ResourceLocation(Industrialise.MODID,computer_addition));
        }


    }
}

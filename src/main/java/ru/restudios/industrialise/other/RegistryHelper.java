package ru.restudios.industrialise.other;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.IBlockReader;
import ru.restudios.industrialise.Industrialise;

import javax.annotation.Nullable;

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
        return AbstractBlock.Properties.of(Material.METAL).strength(5).sound(SoundType.METAL);
    }

    public static Block getMetalBlock(){
        return new Block(getMetalBlockProperties());
    }


    public static AbstractBlock.Properties getRockBlockProperties(){
        return AbstractBlock.Properties.of(Material.STONE).strength(6).sound(SoundType.STONE);
    }

    public static Block getRockBlock() { return new Block(getRockBlockProperties()); }

    public static Item defaultBlockItem(Block block){
        return new BlockItem(block,getDefaultItemProperties());
    }

    public static Item uncommonBlockItem(Block block){
        return new BlockItem(block,getUncommonItemProperties());
    }
    public static Item rareBlockItem(Block block){
        return new BlockItem(block,getRareItemProperties());
    }
    public static Item epicBlockItem(Block block){
        return new BlockItem(block,getEpicItemProperties());
    }

    public static Block metalEntityBlock(TileEntityType<?> type){
        return new Block(getMetalBlockProperties()){

            @Override
            public boolean hasTileEntity(BlockState state) {
                return true;
            }

            @Nullable
            @Override
            public TileEntity createTileEntity(BlockState state, IBlockReader world) {
                return type.create();
            }
        };

    }
}

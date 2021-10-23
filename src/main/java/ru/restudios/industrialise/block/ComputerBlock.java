package ru.restudios.industrialise.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import ru.restudios.industrialise.Industrialise;

import javax.annotation.Nullable;

public class ComputerBlock extends Block {

    public ComputerBlock() {
        super(AbstractBlock.Properties.of(Material.METAL).strength(8).sound(SoundType.METAL));
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Industrialise.DeferredEvents.COMPUTER_TILE.get().create();
    }
}

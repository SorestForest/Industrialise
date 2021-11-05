package ru.restudios.industrialise.other.multiblock.custom;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.other.multiblock.AbstractMultiBlock;
import ru.restudios.industrialise.tileentities.ThunderChargerTile;

import java.util.ArrayList;

public class ThunderChargerMultiBlock extends AbstractMultiBlock {

    private final ThunderChargerTile tile;

    public ThunderChargerMultiBlock(ThunderChargerTile tile, World world, BlockState state) {
        super(state,world);
        this.tile = tile;
    }

    @Override
    public ArrayList<Block> parts() {
        return Lists.newArrayList(Industrialise.Blocks.ENERGY_COLLECTOR.get(),Industrialise.Blocks.ENERGY_COLLECTOR.get());
    }

    @Override
    public void tick() {

    }

    @Override
    public void onStructureBuilt() {
        tile.structureBuild();
    }

    @Override
    public void onStructureDestroyed() {
        tile.structureDestroy();
    }

    @Override
    public void onPartConnected(BlockState part, TileEntity server) {

    }


    @Override
    public TileEntity getTileEntity() {
        return tile;
    }
}

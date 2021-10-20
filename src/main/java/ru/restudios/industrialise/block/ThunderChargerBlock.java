package ru.restudios.industrialise.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import ru.restudios.industrialise.Industrialise;

import javax.annotation.Nullable;

public class ThunderChargerBlock extends Block {
    public ThunderChargerBlock() {
        super(Block.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).strength(4,4));
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Industrialise.DeferredEvents.THUNDER_CHARGER_TILE.get().create();
    }
}

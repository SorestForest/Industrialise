package ru.restudios.industrialise.other.multiblock;

import net.minecraft.world.World;

public interface IMultiBlockFactoryProvider<M extends IMultiBlock> {

    IMultiBlockFactory<M> provide(World world);
}

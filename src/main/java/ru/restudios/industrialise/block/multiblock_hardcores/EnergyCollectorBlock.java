package ru.restudios.industrialise.block.multiblock_hardcores;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import ru.restudios.industrialise.Industrialise;
import ru.restudios.industrialise.containers.EnergyCollectorContainer;
import ru.restudios.industrialise.other.RegistryHelper;
import ru.restudios.industrialise.tileentities.EnergyCollectorTileEntity;

import javax.annotation.Nullable;

public class EnergyCollectorBlock extends Block {

    public EnergyCollectorBlock() {
        super(RegistryHelper.getMetalBlockProperties());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Industrialise.TileEntities.ENERGY_COLLECTOR_TILE.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos,
                                PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isClientSide) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if(!player.isCrouching()) {
                if(tileEntity instanceof EnergyCollectorTileEntity) {
                    INamedContainerProvider containerProvider = new INamedContainerProvider() {
                        @Override
                        public ITextComponent getDisplayName() {
                            return Industrialise.localise(Industrialise.ResourceType.CONTAINER,"energy_collector");
                        }

                        @Nullable
                        @Override
                        public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                            return new EnergyCollectorContainer(p_createMenu_1_, ((EnergyCollectorTileEntity) tileEntity),p_createMenu_2_);
                        }
                    };

                    NetworkHooks.openGui(((ServerPlayerEntity)player), containerProvider, tileEntity.getBlockPos());
                } else {
                    throw new IllegalStateException("Our Container provider is missing!");
                }
            }
        }
        return ActionResultType.SUCCESS;
    }
}

package ru.restudios.industrialise.block;

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
import ru.restudios.industrialise.containers.BatteryContainer;
import ru.restudios.industrialise.other.REUtils;
import ru.restudios.industrialise.other.RegistryHelper;
import ru.restudios.industrialise.tileentities.BatteryTileEntity;

import javax.annotation.Nullable;

public class BatteryBlock extends Block {
    public BatteryBlock() {
        super(RegistryHelper.getMetalBlockProperties());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return Industrialise.DeferredEvents.BATTERY_TILE.get().create();
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos,
                                PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isClientSide) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if(!player.isCrouching()) {
                if(tileEntity instanceof BatteryTileEntity) {
                    BatteryTileEntity tile = REUtils.castOrNull(BatteryTileEntity.class,worldIn.getBlockEntity(pos));
                    INamedContainerProvider containerProvider =  new INamedContainerProvider() {

                        @Override
                        public ITextComponent getDisplayName() {
                            return Industrialise.localise(Industrialise.ResourceType.CONTAINER,"battery_block");
                        }

                        @Nullable
                        @Override
                        public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                            assert tile != null;
                            return new BatteryContainer(p_createMenu_1_,p_createMenu_3_,tile,tile.getEnergy());
                        }
                    };

                    NetworkHooks.openGui(((ServerPlayerEntity)player), containerProvider, buffer -> {
                        buffer.writeBlockPos(pos);
                        assert tile != null;
                        buffer.writeInt(tile.getEnergy());
                    });
                } else {
                    throw new IllegalStateException("Our Container provider is missing!");
                }
            }
        }
        return ActionResultType.SUCCESS;
    }


}

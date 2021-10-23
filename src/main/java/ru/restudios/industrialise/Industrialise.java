package ru.restudios.industrialise;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.restudios.industrialise.block.ComputerBlock;
import ru.restudios.industrialise.block.ThunderChargerBlock;
import ru.restudios.industrialise.containers.ThunderChargerContainer;
import ru.restudios.industrialise.items.other.BatteryItem;
import ru.restudios.industrialise.other.DebugTool;
import ru.restudios.industrialise.other.RegistryHelper;
import ru.restudios.industrialise.screen.ThunderChargerScreen;
import ru.restudios.industrialise.tileentities.ComputerTileEntity;
import ru.restudios.industrialise.tileentities.ThunderChargerTile;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("industrialise")
public class Industrialise {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "industrialise";


    public static final ItemGroup GROUP = new ItemGroup("Industrialise") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(DeferredEvents.INFINITY_CRYSTAL.get());
        }
    };

    public Industrialise() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        DeferredEvents.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        DeferredEvents.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        DeferredEvents.TILE_ENTITY.register(FMLJavaModLoadingContext.get().getModEventBus());
        DeferredEvents.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // Registering screens
        event.enqueueWork(() -> {
            ScreenManager.register(DeferredEvents.THUNDER_CHARGER_CONTAINER.get(),ThunderChargerScreen::new);
        });
    }



    @SuppressWarnings("unused")
    public static class DeferredEvents {

        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,MODID);
        public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,MODID);
        public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,MODID);
        public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS,MODID);

        // Ingredients
        public static final RegistryObject<Item> BATTERY_FIRST = ITEMS.register("battery_first",()-> new BatteryItem(10000));
        public static final RegistryObject<Item> BATTERY_SECOND = ITEMS.register("battery_second",()-> new BatteryItem(50000));
        public static final RegistryObject<Item> BATTERY_THIRD = ITEMS.register("battery_third",()-> new BatteryItem(100000));

        public static final RegistryObject<Item> BLUE_GEM = ITEMS.register("blue_gem", RegistryHelper::getRareItem);
        public static final RegistryObject<Item> RED_GEM = ITEMS.register("red_gem", RegistryHelper::getRareItem);
        public static final RegistryObject<Item> YELLOW_GEM = ITEMS.register("yellow_gem", RegistryHelper::getRareItem);
        public static final RegistryObject<Item> GREEN_GEM = ITEMS.register("green_gem", RegistryHelper::getRareItem);

        public static final RegistryObject<Item> CLUE = ITEMS.register("clue", RegistryHelper::getDefaultItem);
        public static final RegistryObject<Item> DARK_CLUE = ITEMS.register("dark_clue", RegistryHelper::getUncommonItem);

        public static final RegistryObject<Item> BROKEN_INFINITY_CRYSTAL = ITEMS.register("broken_infinity_crystal", RegistryHelper::getRareItem);
        public static final RegistryObject<Item> FIXED_INFINITY_CRYSTAL = ITEMS.register("fixed_infinity_crystal", RegistryHelper::getRareItem);
        public static final RegistryObject<Item> INFINITY_CRYSTAL = ITEMS.register("infinity_crystal",() -> glowing(RegistryHelper.getEpicItemProperties()));

        public static final RegistryObject<Item> GLASS_LENSE = ITEMS.register("glass_lense",() -> new Item(RegistryHelper.getDefaultItemProperties().stacksTo(1)){
            @Override
            public int getMaxDamage(ItemStack stack) {
                return 8;
            }

            @Override
            public boolean isDamageable(ItemStack stack) {
                return true;
            }

            @Override
            public boolean canBeDepleted() {
                return true;
            }
        });

        public static final RegistryObject<Item> DIAMOND_DUST = ITEMS.register("diamond_dust", RegistryHelper::getDefaultItem);
        public static final RegistryObject<Item> OBSIDIAN_DUST = ITEMS.register("obsidian_dust", RegistryHelper::getDefaultItem);
        public static final RegistryObject<Item> ENERGY_POWDER = ITEMS.register("uncharged_energy_dust", RegistryHelper::getDefaultItem);
        public static final RegistryObject<Item> ENERGY_DUST = ITEMS.register("energy_dust", RegistryHelper::getRareItem);
        public static final RegistryObject<Item> INFERIUM_DUST = ITEMS.register("inferium_dust", RegistryHelper::getUncommonItem);
        public static final RegistryObject<Item> DARK_POWDER = ITEMS.register("dark_powder", RegistryHelper::getUncommonItem);

        public static final RegistryObject<Item> OBSIDIAN_STICK = ITEMS.register("obsidian_stick", RegistryHelper::getDefaultItem);
        public static final RegistryObject<Item> DARKNESS_STICK = ITEMS.register("darkness_stick", RegistryHelper::getRareItem);

        public static final RegistryObject<Item> UPGRADE_FIRST = ITEMS.register("upgrade_level_one", ()-> new Item(RegistryHelper.getRareItemProperties().stacksTo(1)));
        public static final RegistryObject<Item> UPGRADE_SECOND = ITEMS.register("upgrade_level_two", ()-> new Item(RegistryHelper.getRareItemProperties().stacksTo(1)));
        public static final RegistryObject<Item> UPGRADE_THIRD = ITEMS.register("upgrade_level_three",
                ()-> new Item(RegistryHelper.getRareItemProperties().stacksTo(1)));

        // Tools + weapons
        public static final RegistryObject<Item> DEBUG_TOOL = ITEMS.register("debug_tool", DebugTool::new);

        // Blocks
        public static final RegistryObject<Block> THUNDER_CHARGER = BLOCKS.register("thunder_charger", ThunderChargerBlock::new);
        public static final RegistryObject<Item> THUNDER_CHARGER_ITEM = ITEMS.register("thunder_charger",
                () -> new BlockItem(THUNDER_CHARGER.get(), RegistryHelper.getUncommonItemProperties()));
        public static final RegistryObject<Block> COMPUTER_BLOCK = BLOCKS.register("computer", ComputerBlock::new);
        public static final RegistryObject<Item> COMPUTER_ITEM = ITEMS.register("computer",
                ()-> new BlockItem(COMPUTER_BLOCK.get(), RegistryHelper.getUncommonItemProperties()));
        public static final RegistryObject<Block> ENERGY_BLOCK = BLOCKS.register("energy_block",
                () -> new Block(AbstractBlock.Properties.of(Material.STONE).sound(SoundType.STONE).strength(4)));
        public static final RegistryObject<Item> ENERGY_BLOCK_ITEM = ITEMS.register("energy_block",
                ()-> new BlockItem(ENERGY_BLOCK.get(),RegistryHelper.getUncommonItemProperties()));

        // Tile entities
        public static final RegistryObject<TileEntityType<ThunderChargerTile>> THUNDER_CHARGER_TILE = TILE_ENTITY.register("thunder_charger_tile",
                ()->TileEntityType.Builder.of(ThunderChargerTile::new,THUNDER_CHARGER.get()).build(null));
        public static final RegistryObject<TileEntityType<ComputerTileEntity>> COMPUTER_TILE = TILE_ENTITY.register("computer_tile",
                ()->TileEntityType.Builder.of(ComputerTileEntity::new,COMPUTER_BLOCK.get()).build(null));

        // Containers
        public static final RegistryObject<ContainerType<ThunderChargerContainer>> THUNDER_CHARGER_CONTAINER = CONTAINERS.register("thunder_charger_container",
                ()-> IForgeContainerType.create((windowId, inv, data) -> {
                    BlockPos pos = data.readBlockPos();
                    PlayerEntity entity = inv.player;
                    TileEntity tileEntity = entity.level.getBlockEntity(pos);

                    return new ThunderChargerContainer(windowId,tileEntity,entity);
                }));

        public static Item glowing(Item.Properties properties){
            return new Item(properties){

                @Override
                @OnlyIn(Dist.CLIENT)
                public boolean isFoil(ItemStack p_77636_1_) {
                    return true;
                }
            };
        }
    }
}

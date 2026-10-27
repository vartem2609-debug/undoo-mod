package com.example.undoo;

import com.example.undoo.client.TntScreen;
import com.example.undoo.network.OpenTntMenuPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(UndooMod.MODID)
public class UndooMod {
    public static final String MODID = "undoo";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);
    public static final DeferredHolder<Item, Item> BIG_TNT = ITEMS.register("big_tnt", () -> new BigTntItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> END_PORTAL = ITEMS.register("end_portal", () -> new EndPortalItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> NETHER_PORTAL = ITEMS.register("nether_portal", () -> new NetherPortalItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> MEGA_ENCHANTER = ITEMS.register("mega_enchanter", () -> new MegaEnchanterItem(new Item.Properties()));

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MODID);
    public static final DeferredHolder<MenuType<?>, MenuType<TntMenu>> TNT_MENU = MENUS.register("tnt_menu", () -> IMenuTypeExtension.create(TntMenu::new));

    public UndooMod(IEventBus modBus) {
        ITEMS.register(modBus);
        MENUS.register(modBus);
        ModDataComponents.register(modBus);
        NeoForge.EVENT_BUS.register(CommandBlocker.class);
        modBus.addListener(this::addCreative);
        modBus.addListener(this::registerScreens);
        modBus.addListener(this::registerPackets);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SEARCH) {
            event.accept(BIG_TNT.get());
            event.accept(END_PORTAL.get());
            event.accept(NETHER_PORTAL.get());
            event.accept(MEGA_ENCHANTER.get());
        }
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            event.register(TNT_MENU.get(), TntScreen::new);
        }
    }

    private void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID);
        registrar.playToServer(OpenTntMenuPacket.TYPE, OpenTntMenuPacket.STREAM_CODEC, OpenTntMenuPacket::handle);
    }
  }

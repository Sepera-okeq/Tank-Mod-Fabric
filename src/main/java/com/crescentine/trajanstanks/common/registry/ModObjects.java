package com.crescentine.trajanstanks.common.registry;

import com.crescentine.trajanstanks.common.TankMod;
import com.crescentine.trajanstanks.common.item.ShellItem;
import com.crescentine.trajanstanks.common.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

//Contains the objects of the tank mod
public class ModObjects {

    //Done linked maps to maintain insertion order.
    //In other words, the order you put the objects here.
    //is the same they will appear in the creative tab
    private static final Map<String, Item> ITEMS = new LinkedHashMap<>();
    private static final Map<String, Block> BLOCKS = new LinkedHashMap<>();

    public static final Item TANK_ITEM = add("tank_item", new SpawnEggItem(ModEntityTypes.TANK_ENTITY_TYPE, 12895428, 11382189, defaultSettings()));
    public static final Item SHELL_ITEM = add("shell_item", new ShellItem(defaultSettings().maxCount(16)));
    public static final Item TANK_CONTROLLER = add("tank_controller", new Item(defaultSettings()));
    public static final Item NETHERITE_WHEEL = add("netherite_wheel", new Item(defaultSettings()));

    private static <T extends Item>  T add(final String name, final T item) {
        ITEMS.put(name, item);
        return item;
    }

    private static <T extends Block> T add(final String name, final T block) {
        BLOCKS.put(name, block);
        ITEMS.put(name, new BlockItem(block, defaultSettings()));
        return block;
    }

    private static Item.Settings defaultSettings() {
        return new Item.Settings().group(TankMod.TANKS_GROUP);
    }

    public static void init() {
        ITEMS.keySet().forEach(id -> Registry.register(Registry.ITEM, RegistryUtil.setRegistryName(id), ITEMS.get(id)));
        BLOCKS.keySet().forEach(id -> Registry.register(Registry.BLOCK, RegistryUtil.setRegistryName(id), BLOCKS.get(id)));
    }
}

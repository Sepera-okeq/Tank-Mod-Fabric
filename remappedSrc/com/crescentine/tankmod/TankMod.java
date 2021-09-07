package com.crescentine.tankmod;

import com.crescentine.tankmod.shell.ShellEntity;
import com.crescentine.tankmod.shell.ShellItem;
import com.crescentine.tankmod.tank.TankEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import software.bernie.geckolib3.GeckoLib;

public class TankMod implements ModInitializer {
	public static final EntityType<ShellEntity> ShellEntityType = Registry.register(
			Registry.ENTITY_TYPE,
			new ResourceLocation(TankMod.MOD_ID, "tank_shell"),
			FabricEntityTypeBuilder.<ShellEntity>create(MobCategory.MISC, ShellEntity::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F))
					.trackRangeBlocks(4).trackedUpdateRate(10)
					.build()
	);

	public static final Item ShellEntityItem = new ShellItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(16));
	public static final Item TANK_CONTROLLER = new Item(new FabricItemSettings().tab(CreativeModeTab.TAB_COMBAT));
	public static final Item NETHERITE_WHEEL = new Item(new FabricItemSettings().tab(CreativeModeTab.TAB_MISC));
	public static String MOD_ID = "trajanstanks";
	public static ResourceLocation TANK_ENTITY = new ResourceLocation(MOD_ID,"tank");
	public static final EntityType<TankEntity> TANK_ENTITY_TYPE = FabricEntityTypeBuilder.create(MobCategory.MISC,TankEntity::new).dimensions(EntityDimensions.fixed(4.0f,2.0f)).trackRangeBlocks(8).build();
	public static final Item TANK_ITEM = new SpawnEggItem(TANK_ENTITY_TYPE, 12895428, 11382189, new Item.Properties().tab(CreativeModeTab.TAB_MISC));
	public static final CreativeModeTab TANKS_GROUP = FabricItemGroupBuilder.create(
			new ResourceLocation("trajanstanks", "tanks_groups"))
			.icon(() -> new ItemStack(TANK_ITEM)).
					appendItems(stacks -> {
						stacks.add(new ItemStack(TANK_ITEM));
						stacks.add(new ItemStack(TANK_CONTROLLER));
						stacks.add(new ItemStack(NETHERITE_WHEEL)); }).build();

	//public static final Item TANK_ITEM = new SpawnEggItem(EntityType.TANK_ENTITY, 12895428, 11382189, new Item.Settings().group(ItemGroup.COMBAT));//
	@Override
	public void onInitialize() {
	Registry.register(Registry.ITEM, new ResourceLocation("trajanstanks", "tank_controller"), TANK_CONTROLLER);
		Registry.register(Registry.ITEM, new ResourceLocation(MOD_ID, "shell_item"), ShellEntityItem);
		GeckoLib.initialize();
		Registry.register(Registry.ENTITY_TYPE, TANK_ENTITY, TANK_ENTITY_TYPE);
		FabricDefaultAttributeRegistry.register(TANK_ENTITY_TYPE, TankEntity.createTankAttributes());
		Registry.register(Registry.ITEM, new ResourceLocation("trajanstanks", "tank_item"), TANK_ITEM);
		Registry.register(Registry.ITEM, new ResourceLocation("trajanstanks", "netherite_wheel"), NETHERITE_WHEEL);



	}
}

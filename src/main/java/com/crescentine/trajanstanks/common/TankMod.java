package com.crescentine.trajanstanks.common;

import com.crescentine.trajanstanks.common.registry.ModEntityTypes;
import com.crescentine.trajanstanks.common.util.RegistryUtil;
import com.crescentine.trajanstanks.common.registry.ModObjects;
import com.crescentine.trajanstanks.common.entity.TankEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.GeckoLib;

public class TankMod implements ModInitializer {

	public static String MOD_ID = "trajanstanks";
	public static final ItemGroup TANKS_GROUP = FabricItemGroupBuilder.build(RegistryUtil.setRegistryName("tanks_groups"), ()-> new ItemStack(ModObjects.TANK_ITEM));

	@Override
	public void onInitialize() {
		GeckoLib.initialize();
		ModObjects.init();
		ModEntityTypes.init();
		ServerPlayNetworking.registerGlobalReceiver(new Identifier("shoot"), (server, player, handler, buf, sender) -> {
			if(player.getVehicle() instanceof TankEntity) {
				TankEntity tank = (TankEntity) player.getVehicle();
				tank.shoot(player);
			}
		});
	}
}

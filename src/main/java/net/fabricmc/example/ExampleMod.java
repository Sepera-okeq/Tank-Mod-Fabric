package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.GeckoLib;

public class ExampleMod implements ModInitializer {
	public static String MOD_ID = "modid";
	public static Identifier TANK_ENTITY = new Identifier(MOD_ID,"tank");
	public static final EntityType<TankEntity> TANK_ENTITY_TYPE = FabricEntityTypeBuilder.create(SpawnGroup.MISC,TankEntity::new).dimensions(EntityDimensions.fixed(4.0f,2.0f)).trackRangeBlocks(8).build();
	@Override
	public void onInitialize() {
		Registry.register(Registry.ENTITY_TYPE, TANK_ENTITY, TANK_ENTITY_TYPE);

	}
}

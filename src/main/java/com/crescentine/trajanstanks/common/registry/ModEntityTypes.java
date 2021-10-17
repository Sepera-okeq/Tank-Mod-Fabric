package com.crescentine.trajanstanks.common.registry;

import com.crescentine.trajanstanks.common.entity.TankEntity;
import com.crescentine.trajanstanks.common.entity.projectile.ShellEntity;
import com.crescentine.trajanstanks.common.util.RegistryUtil;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class ModEntityTypes {

    public static final EntityType<ShellEntity> SHELL_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, RegistryUtil.setRegistryName("tank_shell"), FabricEntityTypeBuilder.<ShellEntity>create(SpawnGroup.MISC, ShellEntity::new).dimensions(EntityDimensions.fixed(0.25F, 0.25F)).trackRangeBlocks(4).trackedUpdateRate(10).build());
    public static final EntityType<TankEntity> TANK_ENTITY_TYPE = FabricEntityTypeBuilder.create(SpawnGroup.MISC,TankEntity::new).dimensions(EntityDimensions.fixed(4.0f,2.0f)).trackRangeBlocks(8).build();

    public static void init() {
        Registry.register(Registry.ENTITY_TYPE, RegistryUtil.setRegistryName("tank"), TANK_ENTITY_TYPE);
        FabricDefaultAttributeRegistry.register(TANK_ENTITY_TYPE, TankEntity.createTankAttributes());

    }
}

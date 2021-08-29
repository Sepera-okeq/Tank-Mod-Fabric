package com.crescentine.tankmod;

import com.crescentine.tankmod.shell.ShellEntitySpawnPacket;
import com.crescentine.tankmod.tank.TankEntityModel;
import com.crescentine.tankmod.tank.TankEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.impl.client.renderer.registry.EntityRendererRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class TankModClient implements ClientModInitializer {
    public void receiveEntityPacket() {
        ClientSidePacketRegistry.INSTANCE.register(PacketID, (ctx, byteBuf) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = ShellEntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = ShellEntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = ShellEntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            ctx.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().world == null)
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                Entity e = et.create(MinecraftClient.getInstance().world);
                if (e == null)
                    throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
                e.updateTrackedPosition(pos);
                e.setPos(pos.x, pos.y, pos.z);
                e.setPitch(pitch);
                e.setYaw(yaw);
                e.setId(entityId);
                e.setUuid(uuid);
                MinecraftClient.getInstance().world.addEntity(entityId, e);
            });
        });
    }
    public static final Identifier PacketID = new Identifier(TankMod.MOD_ID, "spawn_packet");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(TankMod.ShellEntityType, (context) ->
                new FlyingItemEntityRenderer(context));
        receiveEntityPacket();
        EntityRendererRegistryImpl.INSTANCE.register(TankMod.TANK_ENTITY_TYPE, ctx ->
                new TankEntityRenderer(ctx, new TankEntityModel()));
    }
}
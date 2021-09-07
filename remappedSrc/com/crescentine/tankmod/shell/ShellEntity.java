package com.crescentine.tankmod.shell;

import com.crescentine.tankmod.TankMod;
import com.crescentine.tankmod.TankModClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ShellEntity extends ThrowableItemProjectile {
    public ShellEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }
    public ShellEntity(Level world, LivingEntity owner) {
        super(TankMod.ShellEntityType, owner, world);
    }
    public ShellEntity(Level world, double x, double y, double z) {
        super(TankMod.ShellEntityType, x, y, z, world);
    }
    @Override
    protected Item getDefaultItem() {
        return TankMod.ShellEntityItem;
    }
    @Override
    public Packet getAddEntityPacket() {
        return ShellEntitySpawnPacket.create(this, TankModClient.PacketID);
    }

    @Environment(EnvType.CLIENT)
    private ParticleOptions getParticleParameters() {
        ItemStack itemStack = this.getItemRaw();
        return (ParticleOptions) (itemStack.isEmpty() ? ParticleTypes.EXPLOSION : new ItemParticleOption(ParticleTypes.ITEM, itemStack));
    }


    @Environment(EnvType.CLIENT)
    public void handleEntityEvent(byte status) {
        if (status == 3) {
            ParticleOptions particleEffect = this.getParticleParameters();

            for (int i = 0; i < 8; ++i) {
                this.level.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void onHitEntity(EntityHitResult entityHitResult) { // called on entity hit.
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity(); // sets a new Entity instance as the EntityHitResult (victim)
        int i = entity instanceof LivingEntity ? 50 : 0; // sets i to 3 if the Entity instance is an instance of BlazeEntity
        entity.hurt(DamageSource.thrown(this, this.getOwner()), (float) i); // deals damage

        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).addEffect((new MobEffectInstance(MobEffects.BLINDNESS, 20 * 3, 0))); // applies a status effect
            ((LivingEntity) entity).addEffect((new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 3, 2))); // applies a status effect
            entity.playSound(SoundEvents.GENERIC_EXPLODE, 2F, 1F);
        }
    }

    protected void onHit(HitResult hitResult) { // called on collision with a block
        super.onHit(hitResult);
        if (!this.level.isClientSide) { // checks if the world is client
            this.level.broadcastEntityEvent(this, (byte) 3); // particle?
            if (!level.isClientSide) {
                level.explode(this, getX(), getY(), getZ(), 2, Explosion.BlockInteraction.BREAK);
                this.discard();
            }
        }
    }
}
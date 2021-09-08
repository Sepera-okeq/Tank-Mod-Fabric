package com.crescentine.tankmod.tank;

import com.crescentine.tankmod.TankMod;
import net.minecraft.entity.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Set;

@SuppressWarnings("EntityConstructor")

public class TankEntity extends Pig implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    public TankEntity(EntityType<?> entityType, Level world) {
        super((EntityType<? extends Pig>) entityType, world);
    }
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tank.walking", true));
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier.Builder createTankAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 70f)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10.0f)
                .add(Attributes.FOLLOW_RANGE, 0);
    }
    @Override
    public boolean canStandOnFluid(Fluid fluid) {
        return false;
    }
    @Override
    public boolean rideableUnderWater() {
        return false;
    }
    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }
    @Override
    public int getMaxFallDistance() {
        return 30;
    }
    @Override
    protected void removePassenger(Entity entity) {
        super.removePassenger(entity);
        if (entity instanceof PlayerEntity player) {
            player.setInvisible(false);
        }
    }
    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

   @Override
    public void registerControllers(AnimationData animationData) {
    animationData.addAnimationController(new AnimationController<>(this,"controller",0,this::predicate));

    }



    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Set.of();
    }
    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.removeAllGoals();
        removeFreeWill();
    }
    @Override
    public boolean canBeControlledByRider() {
        return true;
    }

    @Override
    protected boolean shouldPassengersInheritMalus() {
            return true;
        }

    @Override
    public boolean isEffectiveAi() {
        return true;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    @Override
    public float getSteeringSpeed() {
        return 0.15f;
    }

    @Override
    public void stopRiding() {
        super.stopRiding();
    }


    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public Pig getBreedOffspring(ServerLevel serverWorld, AgeableMob passiveEntity) {
        return null;
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand) {
        if (!player.getCommandSenderWorld().isClientSide && player.getItemInHand(hand).getItem() == TankMod.TANK_CONTROLLER) {
            player.startRiding(this, true);
            player.setInvisible(true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
    @Override
    protected boolean isImmobile() {
        return false;
    }
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.MINECART_RIDING;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_EXPLODE;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ARMOR_EQUIP_IRON;
    }
    @Override
    protected SoundEvent getFallDamageSound(int distance) {
        return null;
    }
    @Override
    protected SoundEvent getSwimSplashSound() {
        return null;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return null;
    }

    }

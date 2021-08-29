package com.crescentine.tankmod;

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

//TankEntity Class extends Minecraft's Pig Entity and implements GeckoLib library's Animation Features
public class TankEntity extends Pig implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

//Constructor for TankEntity Class
    public TankEntity(EntityType<?> entityType, Level world) {
        super((EntityType<? extends Pig>) entityType, world);
    }
//Extends Geckolib (external library) animation event.
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tank.walking", true));
        return PlayState.CONTINUE;
    }


//Adds unique attributes to replace Regular Minecraft Attributes for Parent Class
    public static AttributeSupplier.Builder createTankAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 70f)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10.0f)
                .add(Attributes.FOLLOW_RANGE, 0);
    }
//Overrides 'canWalkOnFluid' to ensure Entity cannot walk on water.
    @Override
    public boolean canStandOnFluid(Fluid fluid) {
        return false;
    }
//Overrides existing method so once entity is in water, player cannot ride it anymore.
    @Override
    public boolean rideableUnderWater() {
        return false;
    }
//Overrides Minecraft method to make it not take damage upon hitting ground.
    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }
//Distance entity can fall before breaking method is changed via Override.
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
//Overrides Minecraft's entity despawn method, so once placed in the world, TankEntity will remain indefinitely.
    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }
//Overrides animation system so it has the ability to play animations on this Class's entity.
   @Override
    public void registerControllers(AnimationData animationData) {
    animationData.addAnimationController(new AnimationController<>(this,"controller",0,this::predicate));

    }


//Gets animation factory from Geckolib Library.
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
//Overrides method so entity cannot have 'armor' to protect it .
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Set.of();
    }
//Overrides method so entity cannot be equipped.
    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }
//Overrides entity 'brain' to terminate all AI... can only be controlled by Player input.
    @Override
    protected void registerGoals() {
        this.goalSelector.removeAllGoals();
        removeFreeWill();
    }
//Player can control entity via override. (normal minecraft pig entity cannot be controlled)
    @Override
    public boolean canBeControlledByRider() {
        return true;
    }
//Player can move wherever they want instead of Pig AI having control. Overrides method.
    @Override
    protected boolean shouldPassengersInheritMalus() {
            return true;
        }
//Overrides method so user can move wherever they want.More or less same as Previous method.
    @Override
    public boolean isEffectiveAi() {
        return true;
    }
//Overrides method so there cannot be equipment. Void methods do not have return statements.
    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    }

//Overrides entity main arm just for the 'lols' (entity doesn't have arms)
    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }
//Overrides method to control entity speed
    @Override
    public float getSteeringSpeed() {
        return 0.15f;
    }
//Not fully implemented yet.
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


    //When player right clicks they mount the entity by overriding the interact method of Pig Entity.
    @Override
    public InteractionResult interactAt(Player player, Vec3 hitPos, InteractionHand hand) {
        if (!player.getCommandSenderWorld().isClientSide && player.getItemInHand(hand).getItem() == TankMod.TANK_CONTROLLER) {
            player.startRiding(this, true);
            player.setInvisible(true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

//Overrides it so entity can move and not be a statue.
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

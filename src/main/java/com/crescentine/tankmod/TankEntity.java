package com.crescentine.tankmod;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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
public class TankEntity extends PigEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

//Constructor for TankEntity Class
    public TankEntity(EntityType<?> entityType, World world) {
        super((EntityType<? extends PigEntity>) entityType, world);
    }
//Extends Geckolib (external library) animation event.
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tank.walking", true));
        return PlayState.CONTINUE;
    }


//Adds unique attributes to replace Regular Minecraft Attributes for Parent Class
    public static DefaultAttributeContainer.Builder createTankAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 70f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 0);
    }
//Overrides 'canWalkOnFluid' to ensure Entity cannot walk on water.
    @Override
    public boolean canWalkOnFluid(Fluid fluid) {
        return false;
    }
//Overrides existing method so once entity is in water, player cannot ride it anymore.
    @Override
    public boolean canBeRiddenInWater() {
        return false;
    }
//Overrides Minecraft method to make it not take damage upon hitting ground.
    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }
//Distance entity can fall before breaking method is changed via Override.
    @Override
    public int getSafeFallDistance() {
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
    public boolean cannotDespawn() {
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
    public Iterable<ItemStack> getArmorItems() {
        return Set.of();
    }
//Overrides method so entity cannot be equipped.
    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }
//Overrides entity 'brain' to terminate all AI... can only be controlled by Player input.
    @Override
    protected void initGoals() {
        this.goalSelector.clear();
        clearGoalsAndTasks();
    }
//Player can control entity via override. (normal minecraft pig entity cannot be controlled)
    @Override
    public boolean canBeControlledByRider() {
        return true;
    }
//Player can move wherever they want instead of Pig AI having control. Overrides method.
    @Override
    protected boolean movesIndependently() {
        return true;
    }
//Overrides method so user can move wherever they want.More or less same as Previous method.
    @Override
    public boolean canMoveVoluntarily() {
        return true;
    }
//Overrides method so there cannot be equipment. Void methods do not have return statements.
    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
    }

//Overrides entity main arm just for the 'lols' (entity doesn't have arms)
    @Override
    public Arm getMainArm() {
        return Arm.LEFT;
    }
//Overrides method to control entity speed.
    @Override
    public float getSaddledSpeed() {
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
    public PigEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return null;
    }

    //When player right clicks they mount the entity by overriding the interact method of Pig Entity.
    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (!player.getEntityWorld().isClient && player.getStackInHand(hand) == ItemStack.EMPTY && hand == Hand.MAIN_HAND) {
            player.startRiding(this, true);
            player.setInvisible(true);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

//Overrides it so entity can move and not be a statue.
    @Override
    protected boolean isImmobile() {
        return false;
    }

    }

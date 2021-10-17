package com.crescentine.trajanstanks.common.entity;

import com.crescentine.trajanstanks.client.TankModClient;
import com.crescentine.trajanstanks.common.registry.ModObjects;
import com.crescentine.trajanstanks.common.entity.projectile.ShellEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
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

public class TankEntity extends PigEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    private final int cooldown = 75;

    private int time = cooldown;


    public TankEntity(EntityType<?> entityType, World world) {
        super((EntityType<? extends PigEntity>) entityType, world);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tank.walking", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    public static DefaultAttributeContainer.Builder createTankAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 70f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 10.0f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 0);
    }

    @Override
    public boolean canWalkOnFluid(Fluid fluid) {
        return false;
    }

    @Override
    public boolean canBeRiddenInWater() {
        return false;
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    public int getSafeFallDistance() {
        return 30;
    }

    @Override
    protected void removePassenger(Entity entity) {
        super.removePassenger(entity);
        if (entity instanceof PlayerEntity player) {
            entity.setInvisible(false);
        }
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));

    }


    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return Set.of();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.clear();
        clearGoalsAndTasks();
    }
//Movement Related

    @Override
    public boolean canBeControlledByRider() {
        if (TankModClient.STARTMOVING.isPressed()) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean movesIndependently() {
        return true;
    }

    @Override
    public boolean canMoveVoluntarily() {
        return true;
    }

    //Movement Related ^

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    public Arm getMainArm() {
        return Arm.LEFT;
    }

    @Override
    public float getSaddledSpeed() {
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
    public PigEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return null;
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (!player.getEntityWorld().isClient && player.getStackInHand(hand).getItem() == ModObjects.TANK_CONTROLLER) {
            player.startRiding(this, true);
            player.setInvisible(true);
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    protected boolean isImmobile() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_MINECART_RIDING;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GENERIC_EXPLODE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
    }

    @Override
    protected SoundEvent getFallSound(int distance) {
        return SoundEvents.BLOCK_ANVIL_LAND;
    }

    @Override
    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_GENERIC_SPLASH;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_GENERIC_SWIM;
    }
    public void tick() {

        super.tick();

        if(time < cooldown) time++;

    }

    public boolean shoot(PlayerEntity player) {

        PlayerInventory inv = player.getInventory();

        int slot = inv.getSlotWithStack(new ItemStack(ModObjects.SHELL_ITEM));

        if(slot == -1) {
            player.sendMessage(new LiteralText("§cYou don't have any ammo !"), true);
            world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            return false;
        }

        if(time < cooldown) {
            player.sendMessage(new LiteralText("§7Please wait " + (cooldown-time)/20 + " s !"), true);
            world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            return false;
        }

        ShellEntity shellEntity = new ShellEntity(world, player);
        shellEntity.setProperties(player, player.getPitch(), player.getYaw(), 0.0F, 1.5F, 0F);

        double distance = 4.0D;

        double x = -MathHelper.sin(player.getYaw() / 180.0F * (float) Math.PI) * distance;
        double z = MathHelper.cos(player.getYaw() / 180.0F * (float) Math.PI) * distance;

        shellEntity.setPos(shellEntity.getX() + x, shellEntity.getY()-1.0D, shellEntity.getZ() + z);
        world.spawnEntity(shellEntity);

        inv.getStack(slot).decrement(1);

        time = 0;

        return true;

    }

}

package com.crescentine.tankmod.shell;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ShellItem extends Item {
    public ShellItem(Properties settings) {
        super(settings);
    }
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.NEUTRAL, 0.5F, 1F);
		user.getCooldowns().addCooldown(this, 5);
        if (!world.isClientSide) {
            ShellEntity shellEntity = new ShellEntity(world, user);
            shellEntity.setItem(itemStack);
            shellEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 0F);
            world.addFreshEntity(shellEntity);
        }

        user.awardStat(Stats.ITEM_USED.get(this));
        if (!user.getAbilities().instabuild) {
            itemStack.shrink(1); // decrements itemStack if user is not in creative mode
        }

        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }
}

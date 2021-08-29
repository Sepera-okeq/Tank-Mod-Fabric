package com.crescentine.tankmod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TankEntityModel extends AnimatedGeoModel<TankEntity> {
    @Override
    public ResourceLocation getModelLocation(TankEntity object) {
        return new ResourceLocation(TankMod.MOD_ID, "geo/tank.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(TankEntity object) {
        return new ResourceLocation(TankMod.MOD_ID, "textures/item/texture.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(TankEntity animatable) {
        return new ResourceLocation(TankMod.MOD_ID, "animations/tank.animation.json");
    }
}

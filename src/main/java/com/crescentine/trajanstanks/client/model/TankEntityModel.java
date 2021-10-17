package com.crescentine.trajanstanks.client.model;

import com.crescentine.trajanstanks.common.TankMod;
import com.crescentine.trajanstanks.common.entity.TankEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TankEntityModel extends AnimatedGeoModel<TankEntity> {
    @Override
    public Identifier getModelLocation(TankEntity object) {
        return new Identifier(TankMod.MOD_ID, "geo/tank.geo.json");
    }

    @Override
    public Identifier getTextureLocation(TankEntity object) {
        return new Identifier(TankMod.MOD_ID, "textures/item/texture.png");
    }

    @Override
    public Identifier getAnimationFileLocation(TankEntity animatable) {
        return new Identifier(TankMod.MOD_ID, "animations/tank.animation.json");
    }
}

package com.crescentine.tankmod.tank;

import com.crescentine.tankmod.tank.TankEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;


public class TankEntityRenderer extends GeoEntityRenderer<TankEntity>  {
    public TankEntityRenderer(EntityRenderDispatcher ctx, AnimatedGeoModel<TankEntity> modelProvider) {
        super(ctx, modelProvider);
        this.shadowRadius=.5f;
    }
}

/* */
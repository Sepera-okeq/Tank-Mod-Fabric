package com.crescentine.tankmod;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class TankEntityRenderer extends GeoEntityRenderer<TankEntity> {
        public TankEntityRenderer(EntityRendererProvider.Context ctx, AnimatedGeoModel<TankEntity> modelProvider) {
            super(ctx, modelProvider);

            this.shadowRadius = .5f;
        }
}
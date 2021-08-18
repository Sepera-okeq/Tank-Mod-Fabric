package net.fabricmc.example;

import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.example.client.model.entity.ExampleEntityModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

    public class TankEntityRenderer extends GeoEntityRenderer<TankEntity>  {
        public TankEntityRenderer(EntityRendererFactory.Context ctx, AnimatedGeoModel<TankEntity> modelProvider) {
            super(ctx, modelProvider);

            this.shadowRadius = .5f;
        }
}
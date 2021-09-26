package com.crescentine.tankmod.tank;

import com.crescentine.tankmod.tank.TankEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

public class TankEntityRenderer extends GeoEntityRenderer<TankEntity>
{
    public TankEntityRenderer(EntityRenderDispatcher renderManager)
    {
        super(renderManager, new TankEntityModel());
        this.shadowRadius = 0.5F; //change 0.7 to the desired shadow size.
    }
}

/* */
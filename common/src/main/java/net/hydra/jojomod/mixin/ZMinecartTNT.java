package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IMinecartTNT;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(MinecartTNT.class)
public class ZMinecartTNT implements IMinecartTNT {

    @Shadow
    protected void explode(@Nullable DamageSource $$0, double $$1){
    }

    @Override
    public void roundabout$explode(@Nullable DamageSource $$0, double $$1){
        explode($$0,$$1);
    }
}

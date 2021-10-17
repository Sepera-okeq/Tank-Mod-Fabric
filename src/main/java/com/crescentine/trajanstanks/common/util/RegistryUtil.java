package com.crescentine.trajanstanks.common.util;

import com.crescentine.trajanstanks.common.TankMod;
import net.minecraft.util.Identifier;

public class RegistryUtil {

    public static <T extends String> Identifier setRegistryName(final T name) {
        return new Identifier(TankMod.MOD_ID, name);
    }
}

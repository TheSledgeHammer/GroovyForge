
package com.thesledgehammer.groovyforge

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(GroovyForge.MOD_ID)
class GroovyForge {

    static final String MOD_ID = "groovyforge";
    static final String MOD_NAME = "GroovyForge";
    static final String VERSION = "2.0.1";
    static final String MCVERSION = "1.13.2";

    static GroovyForge instance;
    private static final Logger LOGGER = LogManager.getLogger();

    GroovyForge() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
    }
}


/*
 * Copyright [2018] [TheSledgeHammer]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thesledgehammer.groovyforge

import com.ibm.icu.impl.Pair
import net.minecraftforge.eventbus.EventBusErrorMessage
import net.minecraftforge.eventbus.api.BusBuilder
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.IEventListener
import net.minecraftforge.fml.AutomaticEventSubscriber
import net.minecraftforge.fml.ExtensionPoint
import net.minecraftforge.fml.ModContainer
import net.minecraftforge.fml.ModLoadingException
import net.minecraftforge.fml.ModLoadingStage
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager

import java.util.function.Consumer

import static net.minecraftforge.fml.Logging.LOADING

/**
 * Extends FMLModContainer, thus is more likely to be interoperable with the FMLJavaModLoadingContext.
 * Note:Does not allow for use of the FMLJavaModLoadingContext from a Groovy Based Mod. Will throw a cast exception
 **/

class FMLGroovyModContainer extends ModContainer {

    private static final def LOGGER = LogManager.getLogger()
    /*
    private final ModFileScanData scanResults
    private final IEventBus eventBus
    */
    private Object modInstance
    private Class<?> modClass;

    FMLGroovyModContainer(IModInfo info, String className, ClassLoader modClassLoader/*, ModFileScanData modFileScanResults*/) {
        super(info);
        LOGGER.debug(LOADING, "Creating FMLModContainer instance for {} with classLoader {} & {}", className, modClassLoader, getClass().getClassLoader())
        /*
        this.scanResults = modFileScanResults

        this.eventBus = BusBuilder.builder().setExceptionHandler() .setExceptionHandler(this.&onEventFailed).setTrackPhases(false).build()
        this.configHandler = Optional.of({ event -> this.eventBus.post(event) }) as Optional<Consumer<ModConfig.ModConfigEvent>>
        */

        final FMLGroovyModLoadingContext contextExtension = new FMLGroovyModLoadingContext(this)
        this.contextExtension = { -> contextExtension }

        try {
            modClass = Class.forName(className, true, modClassLoader)
            LOGGER.debug(LOADING, "Loaded modclass {} with {}", modClass.getName(), modClass.getClassLoader())
        } catch (Throwable e) {
            LOGGER.error(LOADING, "Failed to load class {}", className, e)
            throw new ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e)
        }
    }

    @Override
    boolean matches(Object mod) {
        if(this.modInstance == mod) {
            return true;
        }
        return false;
    }

    @Override
    Object getMod() {
        return modInstance
    }
/*
    IEventBus getEventBus() {
        return this.eventBus
    }

    @Override
    protected void acceptEvent(final Event e) {
        this.eventBus.post(e)
    }
    */
}
/*
class FMLGroovyModContainer extends FMLModContainer {
    FMLGroovyModContainer(IModInfo info, String className, ClassLoader modClassLoader, ModFileScanData modFileScanResults) {
        super(info, className, modClassLoader, modFileScanResults)
        final FMLGroovyModLoadingContext contextExtension = new FMLGroovyModLoadingContext(this);
        this.contextExtension = { -> contextExtension };
    }
}
*/
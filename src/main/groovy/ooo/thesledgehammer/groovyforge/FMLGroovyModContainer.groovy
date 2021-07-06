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
 *
 * Notice: This file is a modified version of Minecraft Forge's "FMLModContainer".
 */
/*
 * Minecraft Forge
 * Copyright (c) 2016-2020.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package ooo.thesledgehammer.groovyforge

import net.minecraftforge.eventbus.api.BusBuilder
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModContainer
import net.minecraftforge.fml.ModLoadingException
import net.minecraftforge.fml.ModLoadingStage
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.javafmlmod.FMLModContainer
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager

import java.util.function.Consumer

import static net.minecraftforge.fml.Logging.LOADING

class FMLGroovyModContainer extends ModContainer {

    private static final def LOGGER = LogManager.getLogger()
    private final ModFileScanData scanResults
    private final IEventBus eventBus
    private Object modInstance
    private Class<?> modClass;
    private FMLModContainer FML;

    FMLGroovyModContainer(IModInfo info, String className, ClassLoader modClassLoader, ModFileScanData modFileScanResults) {
        super(info);
        LOGGER.debug(LOADING, "Creating FMLModContainer instance for {} with classLoader {} & {}", className, modClassLoader, getClass().getClassLoader());

        this.FML = new FMLModContainer(info, className, modClassLoader, modFileScanResults);
        this.scanResults = modFileScanResults
        this.eventBus = BusBuilder.builder().setExceptionHandler().setExceptionHandler(FML::onEventFailed).setTrackPhases(false).build()
        FML.configHandler = Optional.of(this.eventBus::post) as Optional<Consumer<ModConfig.ModConfigEvent>>

        final FMLGroovyModLoadingContext contextExtension = new FMLGroovyModLoadingContext(this)
        this.contextExtension = { -> contextExtension }

        /* Construct ModClass */
        try {
            this.modClass = Class.forName(className, true, modClassLoader)
            LOGGER.debug(LOADING, "Loaded modclass {} with {}", modClass.getName(), modClass.getClassLoader())
        } catch (Throwable e) {
            LOGGER.error(LOADING, "Failed to load class {}", className, e)
            throw new ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e)
        }

        /* Construct ModInstance */
        if(this.modClass != null) {
            try {
                LOGGER.trace(LOADING, "Loading mod instance {} of type {}", getModId(), modClass.getName());
                this.modInstance = modClass.newInstance();
                LOGGER.trace(LOADING, "Loaded mod instance {} of type {}", getModId(), modClass.getName());
            } catch (Throwable e) {
                LOGGER.error(LOADING,"Failed to create mod instance. ModID: {}, class {}", getModId(), modClass.getName(), e);
                throw new ModLoadingException(modInfo, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmod", e, modClass);
            }
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

    IEventBus getEventBus() {
        return this.eventBus;
    }
}

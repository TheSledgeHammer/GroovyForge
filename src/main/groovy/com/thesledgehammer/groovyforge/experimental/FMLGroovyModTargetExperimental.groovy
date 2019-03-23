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
package com.thesledgehammer.groovyforge.experimental

import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.IModLanguageProvider
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

import static net.minecraftforge.fml.Logging.LOADING

class FMLGroovyModTargetExperimental implements IModLanguageProvider.IModLanguageLoader {

    private static final Logger LOGGER = LogManager.getLogger();
    private final String className;
    private final String modId;

    FMLGroovyModTargetExperimental(String className, String modId) {
        this.className = className;
        this.modId = modId;
    }

    String getModId() {
        return modId;
    }

    @Override
    def <T> T loadMod(IModInfo info, ClassLoader modClassLoader, ModFileScanData modFileScanResults) {
        try {
            final Class<?> fmlContainer = Class.forName("com.thesledgehammer.groovyforge.experimental.FMLGroovyModContainerExperimental", true, Thread.currentThread().getContextClassLoader());
            LOGGER.debug(LOADING, "Loading FMLGroovyModContainer from classloader {} - got {}", Thread.currentThread().getContextClassLoader(), fmlContainer.getClassLoader());
            final Constructor<?> constructor = fmlContainer.getConstructor(IModInfo.class, String.class, ClassLoader.class, ModFileScanData.class);
            return (T) constructor.newInstance(info, className, modClassLoader, modFileScanResults);
        }
        catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.fatal(LOADING,"Unable to load FMLGroovyModContainer, wut?", e);
            throw new RuntimeException(e);
        }
    }
}

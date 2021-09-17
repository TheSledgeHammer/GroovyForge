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
 * Notice: This file is a modified version of Minecraft Forge's "FMLJavaModLanguageProvider".
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

import cpw.mods.modlauncher.api.LamdbaExceptionUtils
import net.minecraftforge.fml.ModLoadingException
import net.minecraftforge.fml.ModLoadingStage
import net.minecraftforge.fml.javafmlmod.FMLJavaModLanguageProvider
import net.minecraftforge.forgespi.language.ILifecycleEvent
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.IModLanguageProvider
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.objectweb.asm.Type

import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors

import static net.minecraftforge.fml.Logging.LOADING
import static net.minecraftforge.fml.Logging.SCAN

class FMLGroovyModLanguageProvider implements IModLanguageProvider {

    private static final Logger LOGGER = LogManager.getLogger()

    private static class FMLGroovyModTarget implements IModLanguageProvider.IModLanguageLoader {

        private static final Logger LOGGER = FMLGroovyModLanguageProvider.LOGGER
        private final String className
        private final String modId

        private FMLGroovyModTarget(String className, String modId) {
            this.className = className
            this.modId = modId
        }

        String getModId() {
            return modId
        }

        @Override
        <T> T loadMod(final IModInfo info, final ModFileScanData modFileScanResults, final ModuleLayer gameLayer) {
            try {
                final Class<?> fmlContainer = Class.forName("ooo.thesledgehammer.groovyforge.FMLGroovyModContainer", true, Thread.currentThread().getContextClassLoader())
                LOGGER.debug(LOADING, "Loading FMLGroovyModContainer from classloader {} - got {}", Thread.currentThread().getContextClassLoader(), fmlContainer.getClassLoader())
                final Constructor<?> constructor = fmlContainer.getConstructor(IModInfo.class, String.class, ModFileScanData.class, ModuleLayer.class)
                return (T) constructor.newInstance(info, className, modFileScanResults, gameLayer)
            } catch (InvocationTargetException e) {
                LOGGER.fatal(LOADING, "Failed to build mod", e)
                if (e.getTargetException() instanceof ModLoadingException) {
                    throw e.getTargetException()
                } else {
                    throw new ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e)
                }
            } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                LOGGER.fatal(LOADING,"Unable to load FMLModContainer, wut?", e)
                final Class<RuntimeException> mle = (Class<RuntimeException>) LamdbaExceptionUtils.uncheck(
                        {
                            ->Class.forName("net.minecraftforge.fml.ModLoadingException", true, Thread.currentThread().getContextClassLoader())
                        } as LamdbaExceptionUtils.Runnable_WithExceptions
                )

                final Class<ModLoadingStage> mls = (Class<ModLoadingStage>) LamdbaExceptionUtils.uncheck(
                        {
                            ->Class.forName("net.minecraftforge.fml.ModLoadingStage", true, Thread.currentThread().getContextClassLoader())
                        } as LamdbaExceptionUtils.Runnable_WithExceptions
                )
                throw LamdbaExceptionUtils.uncheck(
                        {
                            ->LamdbaExceptionUtils.uncheck(mle.getConstructor(IModInfo.class, mls, String.class, Throwable.class) as LamdbaExceptionUtils.Runnable_WithExceptions).newInstance(info, Enum.valueOf(mls, "CONSTRUCT"), "fml.modloading.failedtoloadmodclass", e)
                        } as LamdbaExceptionUtils.Runnable_WithExceptions
                )
            }
        }
    }

    @Override
    String name() {
        return "groovyfml"
    }

    @Override
    Consumer<ModFileScanData> getFileVisitor() {
        return (scanResult -> {
            final Map<String, FMLGroovyModTarget> modTargetMap = scanResult.getAnnotations().stream()
                    .filter(ad -> ad.annotationType().equals(FMLJavaModLanguageProvider.MODANNOTATION))
                    .peek(ad -> LOGGER.debug(SCAN, "Found @Mod class {} with id {}", ad.clazz().getClassName(), ad.annotationData().get("value")))
                    .map(ad -> new FMLGroovyModTarget(ad.clazz().getClassName(), (String) ad.annotationData().get("value")))
                    .collect(Collectors.toMap(FMLGroovyModTarget::getModId, Function.identity(), (a, b) -> a));
            scanResult.addLanguageLoader(modTargetMap) } );
    }

    @Override
    <R extends ILifecycleEvent<R> > void consumeLifecycleEvent(final Supplier<R> consumeEvent) {

    }
}

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

    private static final Logger LOGGER = LogManager.getLogger();

    private static class FMLGroovyModTarget implements IModLanguageLoader {

        private static final Logger LOGGER = FMLGroovyModLanguageProvider.LOGGER
        private final String className
        private final String modId

        FMLGroovyModTarget(String className, String modId) {
            this.className = className
            this.modId = modId
        }

        String getModId() {
            return modId
        }

        @Override
        <T> T loadMod(final IModInfo info, final ClassLoader modClassLoader, final ModFileScanData modFileScanResults) {
            try {
                final Class<?> fmlContainer = Class.forName("com.thesledgehammer.groovyforge.FMLGroovyModContainer", true, Thread.currentThread().getContextClassLoader())
                LOGGER.debug(LOADING, "Loading FMLGroovyModContainer from classloader {} - got {}", Thread.currentThread().getContextClassLoader(), fmlContainer.getClassLoader())
                final Constructor<?> constructor = fmlContainer.getConstructor(IModInfo.class, String.class, ClassLoader.class, ModFileScanData.class)
                return (T) constructor.newInstance(info, className, modClassLoader, modFileScanResults)
            } catch (NoSuchMethodException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                LOGGER.fatal(LOADING,"Unable to load FMLGroovyModContainer, wut?", e)
                throw new RuntimeException(e)
            }
        }
    }

    static final Type MODANNOTATION =  Type.getType("Lnet/minecraftforge/fml/common/Mod;")

    @Override
    String name() {
        return "groovyfml"
    }

    @Override
    Consumer<ModFileScanData> getFileVisitor() {
        return { scanResult ->
            final Map<String, FMLGroovyModTarget> modTargetMap = scanResult.getAnnotations().stream()
                    .filter(ad -> ad.getAnnotationType().equals(MODANNOTATION))
                    .peek(ad -> LOGGER.debug(SCAN, "Found @Mod class {} with id {}", ad.getClassType().getClassName(), ad.getAnnotationData().get("value")))
                    .map(ad -> new FMLGroovyModTarget(ad.getClassType().getClassName(), (String)ad.getAnnotationData().get("value")))
                    .collect(Collectors.toMap({it.getModId()} as Function<FMLGroovyModTarget, String>, Function.identity(), (a, b) -> a))
            scanResult.addLanguageLoader(modTargetMap)
        }
    }

    @Override
    <R extends ILifecycleEvent<R> > void consumeLifecycleEvent(final Supplier<R> consumeEvent) {

    }
}

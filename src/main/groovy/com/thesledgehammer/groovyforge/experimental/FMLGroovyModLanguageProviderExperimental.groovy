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

import net.minecraftforge.fml.javafmlmod.FMLJavaModLanguageProvider
import net.minecraftforge.forgespi.language.ILifecycleEvent
import net.minecraftforge.forgespi.language.IModLanguageProvider
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors

import static net.minecraftforge.fml.Logging.SCAN

class FMLGroovyModLanguageProviderExperimental implements IModLanguageProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    String name() {
        return "groovyfml"
    }

    @Override
    Consumer<ModFileScanData> getFileVisitor() {
        return { scanResult ->
            final Map<String, FMLGroovyModTargetExperimental> modTargetMap = scanResult.getAnnotations().stream()
                    .filter({ad -> ad.getAnnotationType().equals(FMLJavaModLanguageProvider.MODANNOTATION)})
                    .peek({ad -> LOGGER.debug(SCAN, "Found @Mod class {} with id {}", ad.getClassType().getClassName(), ad.getAnnotationData().get("value"))})
                    .map({ad -> new FMLGroovyModTargetExperimental(ad.getClassType().getClassName(), (String)ad.getAnnotationData().get("value"))})
                    .collect(Collectors.toMap({it.getModId()} as Function<FMLGroovyModTargetExperimental, String>, Function.identity(), { a, b -> a}));
            scanResult.addLanguageLoader(modTargetMap);
        }
    }

    @Override
    <R extends ILifecycleEvent<R> > void consumeLifecycleEvent(final Supplier<R> consumeEvent) {

    }
}

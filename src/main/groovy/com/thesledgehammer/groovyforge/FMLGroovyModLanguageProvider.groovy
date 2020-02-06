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
import net.minecraftforge.forgespi.language.IModLanguageProvider
import net.minecraftforge.forgespi.language.ModFileScanData
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.MarkerManager
import org.objectweb.asm.Type

import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors

import static net.minecraftforge.fml.Logging.SCAN

class FMLGroovyModLanguageProvider implements IModLanguageProvider {

    private static final def LOGGER = LogManager.getLogger("Loading");
    static final Type MODANNOTATION =  Type.getType("Lnet/minecraftforge/fml/common/Mod;");

    @Override
    String name() {
        return "groovyfml"
    }

    @Override
    Consumer<ModFileScanData> getFileVisitor() {
        return { scanResult ->
            final Map<String, FMLGroovyModTarget> modTargetMap = scanResult.getAnnotations().stream()
                    .filter({ad -> ad.getAnnotationType().equals(MODANNOTATION)})
                    .peek({ad -> LOGGER.debug(SCAN, "Found @Mod class {} with id {}", ad.getClassType().getClassName(), ad.getAnnotationData().get("value"))})
                    .map({ad -> new FMLGroovyModTarget(ad.getClassType().getClassName(), (String)ad.getAnnotationData().get("value"))})
                    .collect(Collectors.toMap({it.getModId()} as Function<FMLGroovyModTarget, String>, Function.identity(), {a, b -> a}));
            scanResult.addLanguageLoader(modTargetMap);
        }
    }

    @Override
    <R extends ILifecycleEvent<R> > void consumeLifecycleEvent(final Supplier<R> consumeEvent) {

    }
}

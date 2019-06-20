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

import net.minecraftforge.fml.AutomaticEventSubscriber
import net.minecraftforge.fml.LifecycleEventProvider
import net.minecraftforge.fml.ModLoadingException
import net.minecraftforge.fml.ModLoadingStage
import net.minecraftforge.fml.javafmlmod.FMLModContainer
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.ModFileScanData

//Extends FMLModContainer, thus is more likely to be interoperable with the FMLJavaModLoadingContext
class FMLGroovyModContainer extends FMLModContainer {

    private final ModFileScanData scanResults;

    FMLGroovyModContainer(IModInfo info, String className, ClassLoader modClassLoader, ModFileScanData modFileScanResults) {
        super(info, className, modClassLoader, modFileScanResults);
        this.scanResults = modFileScanResults;
        final FMLGroovyModLoadingContext contextExtension = new FMLGroovyModLoadingContext(this);
        this.contextExtension = { -> contextExtension };
    }
}

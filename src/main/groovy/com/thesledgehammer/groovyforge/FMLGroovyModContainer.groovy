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

    private Object modInstance;
    private final ModFileScanData scanResults;
    private final Class<?> modClass;

    FMLGroovyModContainer(IModInfo info, String className, ClassLoader modClassLoader, ModFileScanData modFileScanResults) {
        super(info, className, modClassLoader, modFileScanResults);
        this.scanResults = modFileScanResults;
        final FMLGroovyModLoadingContext contextExtension = new FMLGroovyModLoadingContext(this);
        //this.contextExtension = {Supplier -> contextExtension};
        this.contextExtension = { -> contextExtension };
        try {
            modClass = Class.forName(className, true, modClassLoader);
        } catch (Throwable e) {
            throw new ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e);
        }
    }


    private void constructMod(LifecycleEventProvider.LifecycleEvent event) {
        try
        {
            //LOGGER.debug(LOADING, "Loading mod instance {} of type {}", getModId(), modClass.getName());
            this.modInstance = modClass.newInstance();
            //LOGGER.debug(LOADING, "Loaded mod instance {} of type {}", getModId(), modClass.getName());
        }
        catch (Throwable e)
        {
          //  LOGGER.error(LOADING,"Failed to create mod instance. ModID: {}, class {}", getModId(), modClass.getName(), e);
            throw new ModLoadingException(modInfo, event.fromStage(), "fml.modloading.failedtoloadmod", e, modClass);
        }
        //LOGGER.debug(LOADING, "Injecting Automatic event subscribers for {}", getModId());
        AutomaticEventSubscriber.inject(this, this.scanResults, this.modClass.getClassLoader());
        //LOGGER.debug(LOADING, "Completed Automatic event subscribers for {}", getModId());
    }

    @Override
    boolean matches(Object mod) {
        return mod == modInstance;
    }

    @Override
    Object getMod() {
        return modInstance;
    }
}

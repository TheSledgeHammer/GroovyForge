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


import net.minecraftforge.fml.javafmlmod.FMLModContainer
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.ModFileScanData

/**Extends FMLModContainer, thus is more likely to be interoperable with the FMLJavaModLoadingContext.
 * Note:Does not allow for use of the FMLJavaModLoadingContext from a Groovy Based Mod. Will throw a cast exception
 **/
class FMLGroovyModContainer extends FMLModContainer {

    FMLGroovyModContainer(IModInfo info, String className, ClassLoader modClassLoader, ModFileScanData modFileScanResults) {
        super(info, className, modClassLoader, modFileScanResults)
        final FMLGroovyModLoadingContext contextExtension = new FMLGroovyModLoadingContext(this);
        this.contextExtension = { -> contextExtension };
    }
}
//Experimenting with extending ModContainer over FMLModContainer for advantages/ distadvantages
/*
    private static final def LOGGER = LogManager.getLogger();
    private final ModFileScanData scanResults;
    private final IEventBus eventBus;
    private Object modInstance;
    private final Class<?> modClass;

    FMLGroovyModContainer(IModInfo info, String className, ClassLoader modClassLoader, ModFileScanData modFileScanResults) {
        super(info);
        LOGGER.debug(LOADING, "Creating FMLModContainer instance for {} with classLoader {} & {}", className, modClassLoader, getClass().getClassLoader());
        this.scanResults = modFileScanResults;
        triggerMap.put(ModLoadingStage.CONSTRUCT, dummy()
                .andThen(this.&beforeEvent)
                .andThen(this.&constructMod)
                .andThen(this.&afterEvent)
        );
        triggerMap.put(ModLoadingStage.CREATE_REGISTRIES, dummy()
                .andThen(this.&beforeEvent)
                .andThen(this.&fireEvent)
                .andThen(this.&afterEvent)
        );
        triggerMap.put(ModLoadingStage.LOAD_REGISTRIES, dummy()
                .andThen(this.&beforeEvent)
                .andThen(this.&fireEvent)
                .andThen(this.&afterEvent)
        );
        triggerMap.put(ModLoadingStage.COMMON_SETUP, dummy()
                .andThen(this.&beforeEvent)
                .andThen(this.&preinitMod)
                .andThen(this.&fireEvent)
                .andThen(this.&afterEvent)
        );
        triggerMap.put(ModLoadingStage.SIDED_SETUP, dummy()
                .andThen(this.&beforeEvent)
                .andThen(this.&fireEvent)
                .andThen(this.&afterEvent)
        );
        triggerMap.put(ModLoadingStage.ENQUEUE_IMC, dummy()
                .andThen(this.&beforeEvent)
                .andThen(this.&initMod)
                .andThen(this.&fireEvent)
                .andThen(this.&afterEvent)
        );
        triggerMap.put(ModLoadingStage.PROCESS_IMC, dummy()
                .andThen(this.&beforeEvent)
                .andThen(this.&fireEvent)
                .andThen(this.&afterEvent)
        );
        triggerMap.put(ModLoadingStage.COMPLETE, dummy()
                .andThen(this.&beforeEvent)
                .andThen(this.&completeLoading)
                .andThen(this.&fireEvent)
                .andThen(this.&afterEvent)
        );
        triggerMap.put(ModLoadingStage.GATHERDATA, dummy()
                .andThen(this.&beforeEvent)
                .andThen(this.&fireEvent)
                .andThen(this.&afterEvent)
        );
        this.eventBus = BusBuilder.builder().setExceptionHandler(this.&onEventFailed).setTrackPhases(false).build();
        this.configHandler = Optional.of({ event -> this.eventBus.post(event) }) as Optional<Consumer<ModConfig.ModConfigEvent>>;

        final FMLGroovyModLoadingContext contextExtension = new FMLGroovyModLoadingContext(this);
        this.contextExtension = { -> contextExtension };

        try {
            modClass = Class.forName(className, true, modClassLoader);
            LOGGER.debug(LOADING, "Loaded modclass {} with {}", modClass.getName(), modClass.getClassLoader());
        } catch (Throwable e) {
            LOGGER.error(LOADING, "Failed to load class {}", className, e);
            throw new ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e);
        }
    }

    private void completeLoading(LifecycleEventProvider.LifecycleEvent lifecycleEvent) {

    }

    private void initMod(LifecycleEventProvider.LifecycleEvent lifecycleEvent) {

    }


    private Consumer<LifecycleEventProvider.LifecycleEvent> dummy() {
        return { s -> };
    }

    private static void onEventFailed(IEventBus iEventBus, Event event, IEventListener[] iEventListeners, int i, Throwable throwable) {
        LOGGER.error(new EventBusErrorMessage(event, i, iEventListeners, throwable));
    }

    private void beforeEvent(LifecycleEventProvider.LifecycleEvent lifecycleEvent) {

    }

    private void fireEvent(LifecycleEventProvider.LifecycleEvent lifecycleEvent) {
        final Event event = lifecycleEvent.getOrBuildEvent(this);
        LOGGER.debug(LOADING, "Firing event for modid {} : {}", this.getModId(), event);
        try {
            eventBus.post(event);
            LOGGER.debug(LOADING, "Fired event for modid {} : {}", this.getModId(), event);
        }
        catch (Throwable e) {
            LOGGER.error(LOADING, "Caught exception during event {} dispatch for modid {}", event, this.getModId(), e);
            throw new ModLoadingException(modInfo, lifecycleEvent.fromStage(), "fml.modloading.errorduringevent", e);
        }
    }

    private void afterEvent(LifecycleEventProvider.LifecycleEvent lifecycleEvent) {
        if (getCurrentState() == ModLoadingStage.ERROR) {
            LOGGER.error(LOADING, "An error occurred while dispatching event {} to {}", lifecycleEvent.fromStage(), getModId());
        }
    }

    private void preinitMod(LifecycleEventProvider.LifecycleEvent lifecycleEvent) {

    }

    private void constructMod(LifecycleEventProvider.LifecycleEvent event) {
        try {
            LOGGER.debug(LOADING, "Loading mod instance {} of type {}", getModId(), modClass.getName());
            this.modInstance = modClass.newInstance();
            LOGGER.debug(LOADING, "Loaded mod instance {} of type {}", getModId(), modClass.getName());
        }
        catch (Throwable e) {
            LOGGER.error(LOADING, "Failed to create mod instance. ModID: {}, class {}", getModId(), modClass.getName(), e);
            throw new ModLoadingException(modInfo, event.fromStage(), "fml.modloading.failedtoloadmod", e, modClass);
        }
        LOGGER.debug(LOADING, "Injecting Automatic event subscribers for {}", getModId());
        AutomaticEventSubscriber.inject(this, this.scanResults, this.modClass.getClassLoader());
        LOGGER.debug(LOADING, "Completed Automatic event subscribers for {}", getModId());
    }

    @Override
    boolean matches(Object mod) {
        return mod == modInstance;
    }

    @Override
    Object getMod() {
        return modInstance;
    }

    IEventBus getEventBus() {
        return this.eventBus
    }

    @Override
    protected void acceptEvent(final Event e) {
        this.eventBus.post(e);
    }
}
*/
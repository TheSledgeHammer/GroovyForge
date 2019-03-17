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
package com.thesledgehammer.groovyforge;

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(modid = GroovyForgeProps.MOD_ID, name = GroovyForgeProps.MOD_NAME, version = GroovyForgeProps.VERSION, acceptedMinecraftVersions = GroovyForgeProps.MCVERSION, modLanguageAdapter = "com.thesledgehammer.groovyforge.api.GroovyLanguageAdapter", dependencies = "")
class GroovyForge {

	@Mod.Instance(GroovyForgeProps.MOD_ID)
	static GroovyForge instance;

	//static Logger logger;

	@Mod.EventHandler
	static void preInit(FMLPreInitializationEvent event) {
		//logger = event.getModLog();
		//proxy.preInit(event);
	}

	@Mod.EventHandler
	static void init(FMLInitializationEvent event) {
		//proxy.init(event);
	}

	@Mod.EventHandler
	static void postInit(FMLPostInitializationEvent event) {
		//proxy.postInit(event);
	}
}

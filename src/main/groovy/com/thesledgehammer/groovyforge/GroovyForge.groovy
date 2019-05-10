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

import net.minecraftforge.fml.common.Mod

@Mod(
        modid = GroovyForge.MOD_ID,
        name = GroovyForge.MOD_NAME,
        version = GroovyForge.VERSION,
        updateJSON = GroovyForge.UPDATE_JSON,
        acceptedMinecraftVersions = GroovyForge.MCVERSION,
        modLanguageAdapter = GroovyForge.LANGUAGE_ADAPTER,
        certificateFingerprint = "616437EBB587FE5A83EE547EA1D2E1C403B074CF"
  )
class GroovyForge {
    static final String MOD_ID = "groovyforge"
    static final String MOD_NAME = "GroovyForge"
    static final String VERSION = "@VERSION@"
    static final String MCVERSION = "1.12.2"
    static final String UPDATE_JSON = "@UPDATE@"
    static final String LANGUAGE_ADAPTER = "com.thesledgehammer.groovyforge.GroovyLanguageAdapter"
}

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

import net.minecraftforge.fml.relauncher.IFMLCallHook;

import java.util.Map;

public class GroovyForgeSetup implements IFMLCallHook {

    @Override
    public void injectData(Map<String, Object> data) {
        ClassLoader loader = (ClassLoader) data.get("classloader");
        try {
            loader.loadClass("com.thesledgehammer.groovyforge.GroovyLanguageAdapter");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't find GroovyForge language adapter", e);
        }

    }

    @Override
    public Void call() throws Exception {
        return null;
    }
}

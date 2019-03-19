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

import net.minecraftforge.fml.common.FMLModContainer
import net.minecraftforge.fml.common.ILanguageAdapter
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.relauncher.Side

import java.lang.reflect.Field
import java.lang.reflect.Method

class GroovyLanguageAdapter implements ILanguageAdapter {

    @Override
    Object getNewInstance(FMLModContainer container, Class<?> objectClass, ClassLoader classLoader, Method factoryMarkedAnnotation) throws Exception {
        Class<?> groovyObjectClass = Class.forName(objectClass.getName(), true, classLoader);
        return groovyObjectClass.newInstance();
        /*
        if (factoryMarkedAnnotation != null) {
            return factoryMarkedAnnotation.invoke(null);
        } else {
            return  objectClass.newInstance();
        }*/
    }

    @Override
    boolean supportsStatics() {
        return true;
    }

    @Override
    void setProxy(Field target, Class<?> proxyTarget, Object proxy) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        //target.set(proxyTarget.newInstance(), proxy);
        target.set(null, proxy);
    }

    @Override
    void setInternalProxies(ModContainer mod, Side side, ClassLoader loader) {

    }
}

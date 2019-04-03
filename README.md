# GroovyForge:
A Minecraft Forge Language Adapter for the Apache Groovy programming language.

### Getting Started:
To use the GroovyForge Language Adapter add the following to @Mod in your main class
```
@Mod(modLanguageAdapter = "com.thesledgehammer.groovyforge.GroovyLanguageAdapter")
class modclass {
    //Your mod class info
}

```

For Minecraft Forge Groovy Library please refer to GroovyMC; link below:
https://github.com/TheSledgeHammer/GroovyMC

### Build Gradle Environment:

Maven Repository:
Add the following to your build.Gradle
```
repositories {
  maven {
    name = "GroovyForge"
    url = "https://dl.bintray.com/thesledgehammer/development/"
  }
}

dependencies {
	compile "com.thesledgehammer.GroovyForge:GroovyForge_1.12.2:+:universal"
}
```

### Apache License v2.0:

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

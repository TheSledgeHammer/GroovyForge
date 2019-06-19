# GroovyForge:
A Minecraft Forge Language providers for the Apache Groovy programming language.

### Getting Started:
To use the GroovyForge Language providers add the following to your mods.toml:
```
# The name of the mod loader type to load - for regular FML @Mod mods it should be javafml
modLoader="groovyfml"
```
Currently unsure if this is required for Language providers or if you can just stick with javafml.

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
	compile "com.thesledgehammer.GroovyForge:GroovyForge_1.13.2:+:universal"
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

<p align="center" style="padding: 3em;"><img width="150" src="https://github.com/TheSledgeHammer/GroovyForge/blob/master/src/main/resources/assets/groovyforge/textures/groovyforgelogo.png?raw=true" /></p>
<h1 align="center" style="margin-top: 20px; border-bottom: 0;">GroovyForge</h1>
<p align="center">A Minecraft Forge Language Adapter for the Apache Groovy programming language.</p>
<p align="center">
    <a href="https://minecraft.curseforge.com/projects/groovyforge"><img src="http://cf.way2muchnoise.eu/full_317563_downloads.svg" /></a>
    <a href="https://minecraft.curseforge.com/projects/groovyforge"><img src="http://cf.way2muchnoise.eu/packs/full_317563_in_packs.svg" /></a>
    <a href="https://minecraft.curseforge.com/projects/groovyforge"><img src="http://cf.way2muchnoise.eu/mods/317563.svg" /></a>
    <a href="https://minecraft.curseforge.com/projects/groovyforge"><img src="http://cf.way2muchnoise.eu/versions/317563.svg" /></a>
</p>
# GroovyForge:
A Minecraft Forge Language providers for the Apache Groovy programming language.

### Getting Started:
To use the GroovyForge Language providers add the following to your mods.toml:
```
# The name of the mod loader type to load - for regular FML @Mod mods it should be javafml
modLoader="groovyfml"
loaderVersion="[3,)"
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
	compile "com.thesledgehammer.GroovyForge:GroovyForge_1.15.2:+:universal"
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

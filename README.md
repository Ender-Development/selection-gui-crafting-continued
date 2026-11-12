<!-- v2.1.4 -->
<!---freshmark header
output = [
	'#',
	'<img src="src/main/resources/{{mod_logo_path}}" height="42" width="42">',
'{{mod_name}}'
	].join(' ')
-->
# <img src="src/main/resources/assets/selectionguicrafting/textures/logo.png" height="42" width="42"> Selection GUI Crafting
<!---freshmark /header -->
<!---freshmark shields
function linkShield(name, value, suffix, url) {
    return link(shield(name, name, value, "blue").replace("-blue.svg", suffix), url);
}

var shields = [];

shields.push(linkShield("Kotlin", "{{kotlin_version}}", "-blue?logo=kotlin", "https://kotlinlang.org/"));
shields.push(linkShield("Java", "{{java_version}}", "-blue?logo=openjdk&logoColor=000000", "https://www.azul.com/downloads/?version=java-25-lts&package=jdk#zulu"));
shields.push(linkShield("Gradle", "{{gradle_version}}", "-blue?logo=gradle&logoColor=02303A", "https://gradle.org/releases/"));

if ("{{CURSEFORGE}}" !== "") {
    shields.push("![CurseForge Downloads](https://img.shields.io/curseforge/dt/{{CURSEFORGE}}?logo=curseforge&label=Downloads&color=orange)");
}
if ("{{MODRINTH}}" !== "") {
    shields.push("![Modrinth Downloads](https://img.shields.io/modrinth/dt/{{MODRINTH}}?logo=modrinth&label=Downloads&color=green)");
}

shields.push("");
shields.push("");

shields.push(linkShield("Maven", "{{root_package}}:{{mod_id}}", "-blue?logo=apachemaven&logoColor=C71A36", "{{mavenUrl}}/" + "{{root_package}}/{{mod_id}}/".replaceAll("\\.", "/") + "{{VERSION}}/"));
shields.push(linkShield("Version", "{{VERSION}}", "-blue?logo=semver&logoColor=3F4551", "{{mod_url}}/releases/tag/{{VERSION}}"));
shields.push(linkShield("License", "{{LICENSE}}", "-blue?logo=spdx", "{{mod_url}}/blob/master/LICENSE"));

shields.push("");
shields.push("> {{mod_description}}");

output = shields.join("\n");
-->
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.20-blue?logo=kotlin)](https://kotlinlang.org/)
[![Java](https://img.shields.io/badge/Java-25.0.5-blue?logo=openjdk&logoColor=000000)](https://www.azul.com/downloads/?version=java-25-lts&package=jdk#zulu)
[![Gradle](https://img.shields.io/badge/Gradle-9.9.0-blue?logo=gradle&logoColor=02303A)](https://gradle.org/releases/)
![CurseForge Downloads](https://img.shields.io/curseforge/dt/1185607?logo=curseforge&label=Downloads&color=orange)
![Modrinth Downloads](https://img.shields.io/modrinth/dt/7kAs3nhe?logo=modrinth&label=Downloads&color=green)


[![Maven](https://img.shields.io/badge/Maven-org.ender__development%3Aselectionguicrafting-blue?logo=apachemaven&logoColor=C71A36)](https://maven.ender-development.org/org/ender_development/selectionguicrafting/3.0.1/)
[![Version](https://img.shields.io/badge/Version-3.0.1-blue?logo=semver&logoColor=3F4551)](https://www.curseforge.com/minecraft/mc-mods/selectiongui-crafting-continued/releases/tag/3.0.1)
[![License](https://img.shields.io/badge/License-MIT-blue?logo=spdx)](https://www.curseforge.com/minecraft/mc-mods/selectiongui-crafting-continued/blob/master/LICENSE)

> A mod that gives modpack authors the ability to add crafting recipes into a selection GUI that is triggered by right-clicking while holding the right items.
<!---freshmark /shields -->
<!---freshmark description
output = "{{readme}}".replaceAll("<br>", "\n")
-->
<a href='https://www.akliz.net/enderman'><img src='https://raw.githubusercontent.com/Ender-Development/.github/refs/heads/master/assets/ender-development/banner.png' alt='Promotional Banner'/></a>

This is a fork of [Selection GUI Crafting](https://www.curseforge.com/minecraft/mc-mods/selection-gui-crafting) by [Gliese_832_c](https://www.curseforge.com/members/gliese_832_c). It provides a few improvements to the original mod and also adds support for [GroovyScript](https://www.curseforge.com/minecraft/mc-mods/groovyscript).

![](title.png)

## Description
Minecraft 1.12.2 mod that gives modpack authors the ability to add crafting recipes into a selection GUI that is triggered by right-clicking while holding the right items.
This could, for example, be used for clay recipes. Instead of manually arranging clay balls in a different way in a crafting table, it would be possible to make it so that you right-click with a spatula in one hand, clay in the other, and just select the clay item you want to make.
Another use example would be forging. Right-click with a hammer in one hand, an ingot in the other, and select your desired tool head.
## Fork? Rewrite!
What started as a fork of the original mod, quickly turned into a rewrite. The original crafting system felt way to clunky and not user-friendly. The new system is much more intuitive and user-friendly. It also allows much more customization and is more flexible to benefit the needs of modpack developers. An added bonus is the support for GroovyScript. The old CraftTweaker support was rewritten as well to reflect the new system.
<!---freshmark /description -->
<!-- modrinth_exclude.start -->
<!---freshmark partnership
output = "{{partnership}}".replaceAll("<br>", "\n")
-->
# Partnership with Akliz
> It's a pleasure to be partnered with Akliz. Besides being a fantastic server provider, which makes it incredibly easy to set up a server of your choice, they help me to push myself and the quality of my projects to the next level. Furthermore, you can click on the banner below to get a discount. :3

<p align='center'><a href='https://www.akliz.net/enderman'><img src='https://raw.githubusercontent.com/Ender-Development/.github/refs/heads/master/assets/ender-development/partnership.png' alt='Akliz Official Partner'/></a></p>

If you aren't located in the [US](https://www.akliz.net/enderman), Akliz now offers servers in:
- [Europe](https://www.akliz.net/enderman-eu)
- [Oceania](https://www.akliz.net/enderman-oce)
<!---freshmark /partnership -->
<!-- modrinth_exclude.end -->

<!-- v2.1.0 -->
<!---freshmark header
output = [
	'#',
	'<img src="src/main/resources/{{mod_logo_path}}" height="42" width="42">',
'{{mod_name}}'
	].join(' ')
-->
# <img src="src/main/resources/assets/template_mod/textures/logo.png" height="42" width="42"> Ender-Development.org Template
<!---freshmark /header -->
<!---freshmark shields
output = [
link(shield("Kotlin", "Kotlin", "{{kotlin_version}}", "blue"), "https://kotlinlang.org/"),
link(shield("Java", "Java", "{{java_version}}", "blue"), "https://www.azul.com/downloads/?version=java-25-lts&package=jdk#zulu"),
link(shield("Maven artifact", "Maven", "{{root_package}}:{{mod_id}}", "blue"), "{{mavenUrl}}/" + "{{root_package}}/{{mod_id}}/".replaceAll("\\.", "/")),
link(shield("Version", "Version", "{{VERSION}}", "blue"), "{{mod_url}}/tags"),
link(shield("License", "License", "{{LICENSE}}", "blue"), "{{mod_url}}/blob/master/LICENSE"),
"",
"> {{mod_description}}"
].join("\n")
-->
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.20-blue.svg)](https://kotlinlang.org/)
[![Java](https://img.shields.io/badge/Java-25.0.5-blue.svg)](https://www.azul.com/downloads/?version=java-25-lts&package=jdk#zulu)
[![Maven artifact](https://img.shields.io/badge/Maven-org.ender__development%3Atemplate__mod-blue.svg)](https://maven.ender-development.org/org/ender_development/template_mod/)
[![Version](https://img.shields.io/badge/Version-2.1.0-blue.svg)](https://github.com/Ender-Development/EnderDevelopmentTemplate/tags)
[![License](https://img.shields.io/badge/License-LGPL--3.0--only-blue.svg)](https://github.com/Ender-Development/EnderDevelopmentTemplate/blob/master/LICENSE)

> Universal Java / Kotlin Template for Minecraft 1.12 utilizing the latest tooling. If you have further questions, feel free to reach out to us.
<!---freshmark /shields -->
<!---freshmark description
output = "{{readme}}".replaceAll("<br>", "\n")
-->
<a href='https://www.akliz.net/enderman'><img src='https://raw.githubusercontent.com/Ender-Development/.github/refs/heads/master/assets/ender-development/banner.png' alt='Promotional Banner'/></a>

# About
This is the successor to our [Catalyx-Template](https://github.com/Ender-Development/Catalyx-Template). The main changes are:
- adapted [RFG 2.0+](https://github.com/GTNewHorizons/RetroFuturaGradle/releases), which requires Java 25 in the dev env
- removed the build-in plugin, everything is now handled by `build.gradle.kts` and `settings.gradle.kts`
- choose between Java and Kotlin, write your mod in whatever language you prefer
- project configuration through `project.properties`, `dependencies.properties`, and `tags.properties`
- the `gradle.properties` file is no longer intended to be edited by the user
- automated property documentation, see [PROPERTIES.md](gradle/template/PROPERTIES.md)
- automated mixin file generation
- automated `README.md` population
- all `*.properties` can be used in lang and mixin files
- dependencies are managed via a `keys#url` approach, for more information check out `dependencies.properties`

Besides these the template still offers:
- automatic Tag (Java) / Reference (Kotlin) class creation, based on `tags.properties`
- built-in mixin, coremod and access transformer support, all configured via the `project.properties` defaulting to the common Ender-Development.org values
- credential management via `secrets.properties`, check `secrets.example.properties` for a template
- providing most common mavens by default
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

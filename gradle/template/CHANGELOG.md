# Ender-Development.org Template Changelog

<!--
Writing a changelog is a good practice for maintaining a clear history of changes in your project.
Try to follow the [Common Changelog](https://common-changelog.org/) conventions.
-->

## [Unreleased]

### Changes

### Bugfixes

### Miscellaneous

## [2.1.4] - 2026-11-12

### Changes

- refactor README.md shields section
- let "updateBuildScript" only sync up to latest tag
- add icons to README.md shields

## [2.1.3] - 2026-11-11

### Changes

- simplify template check
- add grs example JSONs to .gitignore
- comment out Tag code, class doesn't generate when using kotlin as language

### Miscellaneous

- apply spotless

## [2.1.2] - 2026-11-11

### Bugfixes

- 'Release Release [...]' commit message

## [2.1.1] - 2026-11-11

### Changes

- update gradle.properties
- add settings.gradle.kts to versioned files

### Miscellaneous

- update gradle 9.8.2 -> 9.9.0
- Unit -> continue

## [2.1.0] - 2026-11-11

### Changes

- add proper build.gradle.kts header
- add .gitattributes to the versioned files
- add .gitignore to the versioned files

### Miscellaneous

- apply formatting to build.gradle.kts
- iterate with forEach instead of for-loop

## [2.0.9] - 2026-11-11

### Changes

- require 'mod_url' property, remove empty default

### Bugfixes

- release commit message

### Miscellaneous

- cleanup

## [2.0.8] - 2026-11-11

### Changes

- run spotless after clearskies expansion
- add 'zone.rong.clearskies' to expand star imports
- include changelog and author signing in automated release

### Miscellaneous

- always apply spotless last after sync

## [2.0.7] - 2026-11-11

### Changes

- refactor idea tasks
- disable maven publishing in template

### Bugfixes

- instead of hardcoding 'origin' as remote iterate through all

## [2.0.6] - 2026-11-11

### Changes

- move mustRunAfter into actual task
- apply 2.0.5 retroactively
- remove buildscript_version from property docs

### Bugfixes

- spotless implicit dependency

### Miscellaneous

- manually add changelog for 2.0.5

## [2.0.5] - 2026-11-11

### Changes

- isolated verifyRelease task
- apply spotless last
- add prepareRelease task
- add custom version handling for the README.md shield
- work on more release shenanigans
- tweak task order for release task
- add spotless to release task
- move buildscript version bump to release task

## [2.0.4] - 2026-11-11

### Changes

- work on automated changelog generation
- update build.gradle.kts to use tag based versioning
- add kotlin reference object to .gitignore
- remove Kotlin reference object
- add unreleased section to changelog
- remove trailing whitespace from prop doc string
- remove mod_version property
- add recon for tag based versioning

### Bugfixes

- hardcoded changelog path
- path for jetbrains changelog plugin
- maven url in README.md

### Miscellaneous

- reinforce markdown formatting
- hide gradle deprecation warning

## [2.0.3] - 2026-11-10

_Getting Groovy_

- add formatting for Groovy(Script) files
- fix GroovyScript jvmArg not getting applied
- README.md will now get updated with buildscripts
- template version becomes project version in template repo

## [2.0.2] - 2026-11-09

_Java Auto Import_

- disable Java star import

## [2.0.1] - 2026-11-09

_IntelliJ Formatting_

- fix auto formatting of `*.properties` files
- improve docs for `tags.properties` and `secrets.example.properties`
- set vendor and langVersion for Idea

## [2.0.0] - 2026-11-09

_The major refactor_

- rewrite all buildscripts from scratch
- remove custom plugin
- move functionality to `build.gradle.kts`
- updated RFG to 2.0.6

[Unreleased]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.1.4...HEAD
[2.1.4]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.1.3...2.1.4
[2.1.3]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.1.2...2.1.3
[2.1.2]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.1.1...2.1.2
[2.1.1]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.1.0...2.1.1
[2.1.0]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.0.9...2.1.0
[2.0.9]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.0.8...2.0.9
[2.0.8]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.0.7...2.0.8
[2.0.7]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.0.6...2.0.7
[2.0.6]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.0.5...2.0.6
[2.0.5]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.0.4...2.0.5
[2.0.4]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.0.3...2.0.4
[2.0.3]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.0.2...2.0.3
[2.0.2]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.0.1...2.0.2
[2.0.1]: https://github.com/Ender-Development/EnderDevelopmentTemplate/compare/2.0.0...2.0.1
[2.0.0]: https://github.com/Ender-Development/EnderDevelopmentTemplate/commits/2.0.0

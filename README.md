<!-- v2.2.0 -->
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

shields.push(linkShield("Kotlin", "{{kotlin_version}}", "-blue?logo=kotlin&color=7F52FF", "https://kotlinlang.org/"));
shields.push(linkShield("Java", "{{java_version}}", "-blue?logo=data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz48IS0tIFVwbG9hZGVkIHRvOiBTVkcgUmVwbywgd3d3LnN2Z3JlcG8uY29tLCBHZW5lcmF0b3I6IFNWRyBSZXBvIE1peGVyIFRvb2xzIC0tPgo8c3ZnIHdpZHRoPSI4MDBweCIgaGVpZ2h0PSI4MDBweCIgdmlld0JveD0iMCAwIDMyIDMyIiBmaWxsPSJub25lIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciPg0KPHBhdGggZD0iTTE2LjA0OTcgOC40NDA2MkMyMi42Mzc4IDMuMzI2MDcgMTkuMjU2NiAwIDE5LjI1NjYgMEMxOS43NTk4IDUuMjg3MzggMTMuODEzIDYuNTM1ODMgMTIuMjE4OSAxMC4xNjkyQzExLjEzMTIgMTIuNjQ4NSAxMi45NjM4IDE0LjgxOTMgMTYuMDQ3NSAxNy41NTU0QzE1Ljc3NDkgMTYuOTQ5NCAxNS4zNTQ0IDE2LjM2MDYgMTQuOTI4OCAxNS43NjQ1QzEzLjQ3NjkgMTMuNzMxMyAxMS45NjQ1IDExLjYxMzIgMTYuMDQ5NyA4LjQ0MDYyWiIgZmlsbD0iI0U3NkYwMCIvPg0KPHBhdGggZD0iTTE3LjEwMTUgMTguNjc3QzE3LjEwMTUgMTguNjc3IDE5LjA4MzUgMTcuMDc3OSAxNy41MTM5IDE1LjMwMDhDMTIuMTkzMSA5LjI3MTg2IDIzLjMzMzMgNi41MzU4MyAyMy4zMzMzIDYuNTM1ODNDMTYuNTMxNyA5LjgxMjUgMTcuNTQ3MSAxMS43NTc0IDE5LjI1NjcgMTQuMTIwMkMyMS4wODcxIDE2LjY1MzggMTcuMTAxNSAxOC42NzcgMTcuMTAxNSAxOC42NzdaIiBmaWxsPSIjRTc2RjAwIi8+DQo8cGF0aCBkPSJNMjIuOTM3IDIzLjQ0NTZDMjkuMDQyMyAyMC4zMjU4IDI2LjIxOTUgMTcuMzI3OCAyNC4yNDkyIDE3LjczMTdDMjMuNzY2MiAxNy44MzA1IDIzLjU1MDkgMTcuOTE2MiAyMy41NTA5IDE3LjkxNjJDMjMuNTUwOSAxNy45MTYyIDIzLjczMDIgMTcuNjQgMjQuMDcyNiAxNy41MjA0QzI3Ljk3MDUgMTYuMTcyOSAzMC45NjgyIDIxLjQ5NDkgMjIuODE0MyAyMy42MDI4QzIyLjgxNDMgMjMuNjAyOSAyMi45MDg4IDIzLjUxOTggMjIuOTM3IDIzLjQ0NTZaIiBmaWxsPSIjNTM4MkExIi8+DQo8cGF0aCBkPSJNMTAuMjMzIDE5LjQ5NjlDNi40MTMxMiAxOC45OTUzIDEyLjMyNzUgMTcuNjEzOSAxMi4zMjc1IDE3LjYxMzlDMTIuMzI3NSAxNy42MTM5IDEwLjAzMDcgMTcuNDYxNiA3LjIwNTkyIDE4LjgwNDNDMy44NjU3NyAyMC4zOTMyIDE1LjQ2ODEgMjEuMTE1OCAyMS40NzQgMTkuNTYyNUMyMi4wOTg0IDE5LjE0MzIgMjIuOTYxNCAxOC43Nzk4IDIyLjk2MTQgMTguNzc5OEMyMi45NjE0IDE4Ljc3OTggMjAuNTAzNyAxOS4yMTE0IDE4LjA1NjEgMTkuNDE0NUMxNS4wNjEyIDE5LjY2MTIgMTEuODQ1OSAxOS43MDkzIDEwLjIzMyAxOS40OTY5WiIgZmlsbD0iIzUzODJBMSIvPg0KPHBhdGggZD0iTTExLjY4NjQgMjIuNDc1OEM5LjU1NjI0IDIyLjI1OTIgMTAuOTUxIDIxLjI0MzkgMTAuOTUxIDIxLjI0MzlDNS40Mzg5OCAyMy4wNDI5IDE0LjAxNzggMjUuMDgzIDIxLjcxOTkgMjIuODY4MkMyMC45MDEyIDIyLjU4NDQgMjAuMzgwNiAyMi4wNjUzIDIwLjM4MDYgMjIuMDY1M0MxNi42MTYzIDIyLjc3ODEgMTQuNDQxIDIyLjc1NTMgMTEuNjg2NCAyMi40NzU4WiIgZmlsbD0iIzUzODJBMSIvPg0KPHBhdGggZD0iTTEyLjYxNDUgMjUuNjk5MUMxMC40ODYgMjUuNDU4NSAxMS43Mjk1IDI0Ljc0NzQgMTEuNzI5NSAyNC43NDc0QzYuNzI1OTQgMjYuMTIyMiAxNC43NzI5IDI4Ljk2MjUgMjEuMTQzMyAyNi4yNzc3QzIwLjA5OTkgMjUuODc4NyAxOS4zNTI4IDI1LjQxODEgMTkuMzUyOCAyNS40MTgxQzE2LjUxMTEgMjUuOTQ2OSAxNS4xOTMxIDI1Ljk4ODQgMTIuNjE0NSAyNS42OTkxWiIgZmlsbD0iIzUzODJBMSIvPg0KPHBhdGggZD0iTTI1LjkzODcgMjcuMzM4OEMyNS45Mzg3IDI3LjMzODggMjYuODU4OSAyOC4wODQ0IDI0LjkyNTIgMjguNjYxMkMyMS4yNDgxIDI5Ljc1NjYgOS42MjA5MyAzMC4wODc0IDYuMzkwOTQgMjguNzA0OUM1LjIyOTg0IDI4LjIwODIgNy40MDcyMyAyNy41MTg5IDguMDkyMTUgMjcuMzc0MkM4LjgwNjQ2IDI3LjIyMTkgOS4yMTQ2NiAyNy4yNTAzIDkuMjE0NjYgMjcuMjUwM0M3LjkyMzQgMjYuMzU1OCAwLjg2ODQ4OSAyOS4wMDY3IDUuNjMxMTEgMjkuNzY1OUMxOC42MTk1IDMxLjgzNzIgMjkuMzA3NyAyOC44MzMxIDI1LjkzODcgMjcuMzM4OFoiIGZpbGw9IiM1MzgyQTEiLz4NCjxwYXRoIGQ9Ik0yOCAyOC45Njc5QzI3Ljc4NjkgMzEuNjk0NyAxOC43ODc3IDMyLjI2ODMgMTIuOTI3NCAzMS44OTk0QzkuMTA0MzIgMzEuNjU4MyA4LjMzODEyIDMxLjA1NTggOC4zMjY5MSAzMS4wNDdDMTEuOTg1OSAzMS42NDAyIDE4LjE1NDkgMzEuNzQ4MiAyMy4xNTY4IDMwLjgyMjVDMjcuNTkwMyAzMC4wMDE2IDI4IDI4Ljk2NzkgMjggMjguOTY3OVoiIGZpbGw9IiM1MzgyQTEiLz4NCjwvc3ZnPg==&labelColor=ffffff&color=5382A1", "https://www.azul.com/downloads/?version=java-25-lts&package=jdk#zulu"));
shields.push(linkShield("Gradle", "{{gradle_version}}", "-blue?logo=gradle&logoColor=02303A&color=02303A", "https://gradle.org/releases/"));

if ("{{CURSEFORGE}}" !== "" && "{{CURSEFORGE_LINK}}" !== "") {
    shields.push(link("![CurseForge Downloads](https://img.shields.io/curseforge/dt/{{CURSEFORGE}}?logo=curseforge&label=CurseForge&color=F16436)", "{{CURSEFORGE_LINK}}"));
}
if ("{{MODRINTH}}" !== "" && "{{MODRINTH_LINK}}" !== "") {
    shields.push(link("![Modrinth Downloads](https://img.shields.io/modrinth/dt/{{MODRINTH}}?logo=modrinth&label=Modrinth&color=00AF5C)", "{{MODRINTH_LINK}}"));
}
if ("{{MODDEX}}" !== "") {
    shields.push(link("![Static Badge](https://img.shields.io/badge/{{REVIEW}}-blue?logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyBpZD0idXVpZC1iYTQ0MGRjMy0zODUxLTRkMWItOGUyMS02OTIzMTRlODA5OWUiIGRhdGEtbmFtZT0iTGF5ZXIgMiIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB2aWV3Qm94PSIwIDAgMTYxLjE5IDE0NC44OCI%2BCiAgPGcgaWQ9InV1aWQtYWE5YzhmZDYtMGU5My00ZWIxLThkMjUtZTIyNzUxMzk4MjgxIiBkYXRhLW5hbWU9IkxheWVyIDExIj4KICAgIDxnIGlkPSJ1dWlkLTI5YWU5MzFiLTU4YzUtNDU0Yi05N2M0LWZiYWU0MjE4NGQ5YSIgZGF0YS1uYW1lPSJBc3NldCA0NFZlY3RvcnMiPgogICAgICA8Zz4KICAgICAgICA8cGF0aCBkPSJNNDYuODIsNjIuNzdsMTEuMiwxMy4xNWMuNDMuNTEsMS4wOS43NywxLjc2LjdsMTcuMTgtMS43OGMxLjEyLS4xMiwyLjEyLjcsMi4yMywxLjgyLjA1LjQ0LS4wNi44OS0uMjksMS4yN2wtOS4wNCwxNC43MWMtLjM1LjU3LS40LDEuMjgtLjEzLDEuODlsNywxNS43OWMuNDYsMS4wMywwLDIuMjMtMS4wNCwyLjY5LS40MS4xOC0uODcuMjItMS4zLjEybC0xNi43OS00LjA1Yy0uNjUtLjE2LTEuMzQuMDItMS44NC40NmwtMTIuODUsMTEuNTNjLS44NC43NS0yLjEyLjY4LTIuODgtLjE2LS4zLS4zMy0uNDgtLjc2LS41MS0xLjJsLTEuMzQtMTcuMjJjLS4wNS0uNjctLjQzLTEuMjctMS4wMS0xLjZsLTE0Ljk0LTguNjZjLS45Ny0uNTYtMS4zLTEuODEtLjc0LTIuNzguMjItLjM5LjU3LS42OS45OC0uODZsMTUuOTYtNi41OWMuNjItLjI2LDEuMDctLjgsMS4yMS0xLjQ2bDMuNjItMTYuODljLjI0LTEuMSwxLjMyLTEuOCwyLjQyLTEuNTYuNDQuMDkuODMuMzMsMS4xMi42N1oiIGZpbGw9IiMwMGQ0OTIiLz4KICAgICAgICA8cGF0aCBkPSJNMTQ5LjYzLDBsLTM0LjI1LDI3LjI4Yy0uMjksMy4xLTEuNzgsNi4wOC00LjQsOC4xOGwtMTEuNDgsOS4xNGMtMi42MywyLjA5LTUuODcsMi44OC04Ljk1LDIuNDZsLTYuMDMsNC44Yy0yMC40Ni0xNi4yMi01MC4zNS0xNC44OS02OS4yNSw0LjAxLTIwLjM0LDIwLjM0LTIwLjM0LDUzLjQzLDAsNzMuNzYsMTAuMTcsMTAuMTcsMjMuNTMsMTUuMjUsMzYuODgsMTUuMjVzMjYuNzEtNS4wOCwzNi44OC0xNS4yNWMxNy4yOS0xNy4yOSwxOS44Ny00My44LDcuNzYtNjMuODVsNS4xMy00LjA5Yy4yMS0zLjIyLDEuNzEtNi4zNCw0LjQzLTguNWwxMS40OC05LjE0YzIuNzItMi4xNyw2LjEtMi45Myw5LjI4LTIuNDFsMzQuMS0yNy4xNS0xMS41Ni0xNC40OFpNODAuMDEsMTIwLjYyYy0xNS4zNywxNS4zNy00MC4zOCwxNS4zNy01NS43NCwwLTE1LjM2LTE1LjM3LTE1LjM3LTQwLjM4LDAtNTUuNzQsNy40My03LjQzLDE3LjMzLTExLjUzLDI3Ljg3LTExLjUzczIwLjQ0LDQuMDksMjcuODcsMTEuNTMsMTEuNTMsMTcuMzMsMTEuNTMsMjcuODctNC4wOSwyMC40NC0xMS41MywyNy44N1oiIGZpbGw9IiMxMzc3ZmEiLz4KICAgICAgPC9nPgogICAgPC9nPgogIDwvZz4KPC9zdmc%2B&label=ModDex.gg&color=1377FA)", "https://moddex.gg/mod/{{MODDEX}}"))
}

shields.push("");
shields.push("");

shields.push(linkShield("Maven", "{{root_package}}:{{mod_id}}", "-blue?logo=apachemaven&logoColor=C71A36&color=C71A36", "{{mavenUrl}}/" + "{{root_package}}/{{mod_id}}/".replaceAll("\\.", "/") + "{{VERSION}}/"));
shields.push(linkShield("Latest Release", "{{VERSION}}", "-blue?logo=semver&logoColor=FFFFFF&color=3F4551", "{{mod_url}}/releases/tag/{{VERSION}}"));
shields.push(linkShield("License", "{{LICENSE}}", "-blue?logo=spdx&color=4398CC", "{{mod_url}}/blob/master/LICENSE"));

shields.push("");
shields.push("> {{mod_description}}");

output = shields.join("\n");
-->
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.20-blue?logo=kotlin&color=7F52FF)](https://kotlinlang.org/)
[![Java](https://img.shields.io/badge/Java-25.0.5-blue?logo=data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz48IS0tIFVwbG9hZGVkIHRvOiBTVkcgUmVwbywgd3d3LnN2Z3JlcG8uY29tLCBHZW5lcmF0b3I6IFNWRyBSZXBvIE1peGVyIFRvb2xzIC0tPgo8c3ZnIHdpZHRoPSI4MDBweCIgaGVpZ2h0PSI4MDBweCIgdmlld0JveD0iMCAwIDMyIDMyIiBmaWxsPSJub25lIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciPg0KPHBhdGggZD0iTTE2LjA0OTcgOC40NDA2MkMyMi42Mzc4IDMuMzI2MDcgMTkuMjU2NiAwIDE5LjI1NjYgMEMxOS43NTk4IDUuMjg3MzggMTMuODEzIDYuNTM1ODMgMTIuMjE4OSAxMC4xNjkyQzExLjEzMTIgMTIuNjQ4NSAxMi45NjM4IDE0LjgxOTMgMTYuMDQ3NSAxNy41NTU0QzE1Ljc3NDkgMTYuOTQ5NCAxNS4zNTQ0IDE2LjM2MDYgMTQuOTI4OCAxNS43NjQ1QzEzLjQ3NjkgMTMuNzMxMyAxMS45NjQ1IDExLjYxMzIgMTYuMDQ5NyA4LjQ0MDYyWiIgZmlsbD0iI0U3NkYwMCIvPg0KPHBhdGggZD0iTTE3LjEwMTUgMTguNjc3QzE3LjEwMTUgMTguNjc3IDE5LjA4MzUgMTcuMDc3OSAxNy41MTM5IDE1LjMwMDhDMTIuMTkzMSA5LjI3MTg2IDIzLjMzMzMgNi41MzU4MyAyMy4zMzMzIDYuNTM1ODNDMTYuNTMxNyA5LjgxMjUgMTcuNTQ3MSAxMS43NTc0IDE5LjI1NjcgMTQuMTIwMkMyMS4wODcxIDE2LjY1MzggMTcuMTAxNSAxOC42NzcgMTcuMTAxNSAxOC42NzdaIiBmaWxsPSIjRTc2RjAwIi8+DQo8cGF0aCBkPSJNMjIuOTM3IDIzLjQ0NTZDMjkuMDQyMyAyMC4zMjU4IDI2LjIxOTUgMTcuMzI3OCAyNC4yNDkyIDE3LjczMTdDMjMuNzY2MiAxNy44MzA1IDIzLjU1MDkgMTcuOTE2MiAyMy41NTA5IDE3LjkxNjJDMjMuNTUwOSAxNy45MTYyIDIzLjczMDIgMTcuNjQgMjQuMDcyNiAxNy41MjA0QzI3Ljk3MDUgMTYuMTcyOSAzMC45NjgyIDIxLjQ5NDkgMjIuODE0MyAyMy42MDI4QzIyLjgxNDMgMjMuNjAyOSAyMi45MDg4IDIzLjUxOTggMjIuOTM3IDIzLjQ0NTZaIiBmaWxsPSIjNTM4MkExIi8+DQo8cGF0aCBkPSJNMTAuMjMzIDE5LjQ5NjlDNi40MTMxMiAxOC45OTUzIDEyLjMyNzUgMTcuNjEzOSAxMi4zMjc1IDE3LjYxMzlDMTIuMzI3NSAxNy42MTM5IDEwLjAzMDcgMTcuNDYxNiA3LjIwNTkyIDE4LjgwNDNDMy44NjU3NyAyMC4zOTMyIDE1LjQ2ODEgMjEuMTE1OCAyMS40NzQgMTkuNTYyNUMyMi4wOTg0IDE5LjE0MzIgMjIuOTYxNCAxOC43Nzk4IDIyLjk2MTQgMTguNzc5OEMyMi45NjE0IDE4Ljc3OTggMjAuNTAzNyAxOS4yMTE0IDE4LjA1NjEgMTkuNDE0NUMxNS4wNjEyIDE5LjY2MTIgMTEuODQ1OSAxOS43MDkzIDEwLjIzMyAxOS40OTY5WiIgZmlsbD0iIzUzODJBMSIvPg0KPHBhdGggZD0iTTExLjY4NjQgMjIuNDc1OEM5LjU1NjI0IDIyLjI1OTIgMTAuOTUxIDIxLjI0MzkgMTAuOTUxIDIxLjI0MzlDNS40Mzg5OCAyMy4wNDI5IDE0LjAxNzggMjUuMDgzIDIxLjcxOTkgMjIuODY4MkMyMC45MDEyIDIyLjU4NDQgMjAuMzgwNiAyMi4wNjUzIDIwLjM4MDYgMjIuMDY1M0MxNi42MTYzIDIyLjc3ODEgMTQuNDQxIDIyLjc1NTMgMTEuNjg2NCAyMi40NzU4WiIgZmlsbD0iIzUzODJBMSIvPg0KPHBhdGggZD0iTTEyLjYxNDUgMjUuNjk5MUMxMC40ODYgMjUuNDU4NSAxMS43Mjk1IDI0Ljc0NzQgMTEuNzI5NSAyNC43NDc0QzYuNzI1OTQgMjYuMTIyMiAxNC43NzI5IDI4Ljk2MjUgMjEuMTQzMyAyNi4yNzc3QzIwLjA5OTkgMjUuODc4NyAxOS4zNTI4IDI1LjQxODEgMTkuMzUyOCAyNS40MTgxQzE2LjUxMTEgMjUuOTQ2OSAxNS4xOTMxIDI1Ljk4ODQgMTIuNjE0NSAyNS42OTkxWiIgZmlsbD0iIzUzODJBMSIvPg0KPHBhdGggZD0iTTI1LjkzODcgMjcuMzM4OEMyNS45Mzg3IDI3LjMzODggMjYuODU4OSAyOC4wODQ0IDI0LjkyNTIgMjguNjYxMkMyMS4yNDgxIDI5Ljc1NjYgOS42MjA5MyAzMC4wODc0IDYuMzkwOTQgMjguNzA0OUM1LjIyOTg0IDI4LjIwODIgNy40MDcyMyAyNy41MTg5IDguMDkyMTUgMjcuMzc0MkM4LjgwNjQ2IDI3LjIyMTkgOS4yMTQ2NiAyNy4yNTAzIDkuMjE0NjYgMjcuMjUwM0M3LjkyMzQgMjYuMzU1OCAwLjg2ODQ4OSAyOS4wMDY3IDUuNjMxMTEgMjkuNzY1OUMxOC42MTk1IDMxLjgzNzIgMjkuMzA3NyAyOC44MzMxIDI1LjkzODcgMjcuMzM4OFoiIGZpbGw9IiM1MzgyQTEiLz4NCjxwYXRoIGQ9Ik0yOCAyOC45Njc5QzI3Ljc4NjkgMzEuNjk0NyAxOC43ODc3IDMyLjI2ODMgMTIuOTI3NCAzMS44OTk0QzkuMTA0MzIgMzEuNjU4MyA4LjMzODEyIDMxLjA1NTggOC4zMjY5MSAzMS4wNDdDMTEuOTg1OSAzMS42NDAyIDE4LjE1NDkgMzEuNzQ4MiAyMy4xNTY4IDMwLjgyMjVDMjcuNTkwMyAzMC4wMDE2IDI4IDI4Ljk2NzkgMjggMjguOTY3OVoiIGZpbGw9IiM1MzgyQTEiLz4NCjwvc3ZnPg==&labelColor=ffffff&color=5382A1)](https://www.azul.com/downloads/?version=java-25-lts&package=jdk#zulu)
[![Gradle](https://img.shields.io/badge/Gradle-9.9.1-blue?logo=gradle&logoColor=02303A&color=02303A)](https://gradle.org/releases/)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1185607?logo=curseforge&label=CurseForge&color=F16436)](https://www.curseforge.com/minecraft/mc-mods/selectiongui-crafting-continued)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/7kAs3nhe?logo=modrinth&label=Modrinth&color=00AF5C)](https://modrinth.com/mod/selection-gui-crafting-continued)
[![Static Badge](https://img.shields.io/badge/4.0%2F5.0-blue?logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyBpZD0idXVpZC1iYTQ0MGRjMy0zODUxLTRkMWItOGUyMS02OTIzMTRlODA5OWUiIGRhdGEtbmFtZT0iTGF5ZXIgMiIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB2aWV3Qm94PSIwIDAgMTYxLjE5IDE0NC44OCI%2BCiAgPGcgaWQ9InV1aWQtYWE5YzhmZDYtMGU5My00ZWIxLThkMjUtZTIyNzUxMzk4MjgxIiBkYXRhLW5hbWU9IkxheWVyIDExIj4KICAgIDxnIGlkPSJ1dWlkLTI5YWU5MzFiLTU4YzUtNDU0Yi05N2M0LWZiYWU0MjE4NGQ5YSIgZGF0YS1uYW1lPSJBc3NldCA0NFZlY3RvcnMiPgogICAgICA8Zz4KICAgICAgICA8cGF0aCBkPSJNNDYuODIsNjIuNzdsMTEuMiwxMy4xNWMuNDMuNTEsMS4wOS43NywxLjc2LjdsMTcuMTgtMS43OGMxLjEyLS4xMiwyLjEyLjcsMi4yMywxLjgyLjA1LjQ0LS4wNi44OS0uMjksMS4yN2wtOS4wNCwxNC43MWMtLjM1LjU3LS40LDEuMjgtLjEzLDEuODlsNywxNS43OWMuNDYsMS4wMywwLDIuMjMtMS4wNCwyLjY5LS40MS4xOC0uODcuMjItMS4zLjEybC0xNi43OS00LjA1Yy0uNjUtLjE2LTEuMzQuMDItMS44NC40NmwtMTIuODUsMTEuNTNjLS44NC43NS0yLjEyLjY4LTIuODgtLjE2LS4zLS4zMy0uNDgtLjc2LS41MS0xLjJsLTEuMzQtMTcuMjJjLS4wNS0uNjctLjQzLTEuMjctMS4wMS0xLjZsLTE0Ljk0LTguNjZjLS45Ny0uNTYtMS4zLTEuODEtLjc0LTIuNzguMjItLjM5LjU3LS42OS45OC0uODZsMTUuOTYtNi41OWMuNjItLjI2LDEuMDctLjgsMS4yMS0xLjQ2bDMuNjItMTYuODljLjI0LTEuMSwxLjMyLTEuOCwyLjQyLTEuNTYuNDQuMDkuODMuMzMsMS4xMi42N1oiIGZpbGw9IiMwMGQ0OTIiLz4KICAgICAgICA8cGF0aCBkPSJNMTQ5LjYzLDBsLTM0LjI1LDI3LjI4Yy0uMjksMy4xLTEuNzgsNi4wOC00LjQsOC4xOGwtMTEuNDgsOS4xNGMtMi42MywyLjA5LTUuODcsMi44OC04Ljk1LDIuNDZsLTYuMDMsNC44Yy0yMC40Ni0xNi4yMi01MC4zNS0xNC44OS02OS4yNSw0LjAxLTIwLjM0LDIwLjM0LTIwLjM0LDUzLjQzLDAsNzMuNzYsMTAuMTcsMTAuMTcsMjMuNTMsMTUuMjUsMzYuODgsMTUuMjVzMjYuNzEtNS4wOCwzNi44OC0xNS4yNWMxNy4yOS0xNy4yOSwxOS44Ny00My44LDcuNzYtNjMuODVsNS4xMy00LjA5Yy4yMS0zLjIyLDEuNzEtNi4zNCw0LjQzLTguNWwxMS40OC05LjE0YzIuNzItMi4xNyw2LjEtMi45Myw5LjI4LTIuNDFsMzQuMS0yNy4xNS0xMS41Ni0xNC40OFpNODAuMDEsMTIwLjYyYy0xNS4zNywxNS4zNy00MC4zOCwxNS4zNy01NS43NCwwLTE1LjM2LTE1LjM3LTE1LjM3LTQwLjM4LDAtNTUuNzQsNy40My03LjQzLDE3LjMzLTExLjUzLDI3Ljg3LTExLjUzczIwLjQ0LDQuMDksMjcuODcsMTEuNTMsMTEuNTMsMTcuMzMsMTEuNTMsMjcuODctNC4wOSwyMC40NC0xMS41MywyNy44N1oiIGZpbGw9IiMxMzc3ZmEiLz4KICAgICAgPC9nPgogICAgPC9nPgogIDwvZz4KPC9zdmc%2B&label=ModDex.gg&color=1377FA)](https://moddex.gg/mod/selectiongui-crafting-continued)


[![Maven](https://img.shields.io/badge/Maven-org.ender__development%3Aselectionguicrafting-blue?logo=apachemaven&logoColor=C71A36&color=C71A36)](https://maven.ender-development.org/org/ender_development/selectionguicrafting/3.0.1/)
[![Latest Release](https://img.shields.io/badge/Latest_Release-3.0.1-blue?logo=semver&logoColor=FFFFFF&color=3F4551)](https://www.curseforge.com/minecraft/mc-mods/selectiongui-crafting-continued/releases/tag/3.0.1)
[![License](https://img.shields.io/badge/License-MIT-blue?logo=spdx&color=4398CC)](https://www.curseforge.com/minecraft/mc-mods/selectiongui-crafting-continued/blob/master/LICENSE)

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

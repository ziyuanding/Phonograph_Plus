# Phonograph Plus

[![Crowdin](https://badges.crowdin.net/phonograph-plus/localized.svg)](https://crowdin.com/project/phonograph-plus)
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://github.com/chr56/Phonograph_Plus/blob/release/LICENSE.txt)
[<img src="https://github.com/chr56/Phonograph_Plus/actions/workflows/dev.yml/badge.svg" alt="Dev CI Status">](https://github.com/chr56/Phonograph_Plus/actions/workflows/dev.yml)


**A fork of Phonograph under maintenance and development**

A material designed local music player for Android.

<br/>


[简体中文](./README_ZH.md)

This is a fork of [Phonograph](https://github.com/kabouzeid/Phonograph), and is currently under my maintenance and development.

## **Additional Features**

All listed features are compared with original Phonograph.

- Unlock pro.

- Automatic & adaptive dark mode.

- Plenty of user interface changes.

- Change language in application manually.

- Brand-new Detail page with more information like tags

- Show Time Axis in "Lyrics" Dialog and allow seeking basing lyric's time axis and
  support lyrics following.

- User-defined click behavior for songs

- Support history of playing queue.

- Fix Android 11 Scope Storage. (Partial)

- Improve menu experience.

- Improve “Library” pages user experience.

- Support more sort orders.

- Allow collecting app crash report.

- use Path filter to replace, support "exclude mode" (blacklist) and "include mode" (whitelist).

- Add song menu shortcut to add new items to excluded-list (blacklist).

- Co-work-with/Support StatusBar Lyric
  Xposed Module (api)

- Export internal databases for the need of backup.

- Increase history played tracks and top played tracks entries capacity (
  100->150).

- Allow tabs fixed.

- Brand-new File tab.

- Allow deleting songs file along with its external lyrics file. 

- and more small features/fixes.



It is suggested to browser the [Changelog](https://phonographplus.github.io/changelogs/changeslogs/changelog.html) to learn all features completely

## **Translation**

Translate Phonograph Plus into your language -> [Crowdin](https://crowdin.com/project/phonograph-plus)

We have removed Swedish and Norwegian Nynorsk translations due to missing too many translations

## **Screenshot**

For reference only, actual app might be different

|                                       Card Player                                       |                                       Flat Player                                       |
|:---------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------:|
| ![Screenshots](fastlane/metadata/android/en-US/images/phoneScreenshots/05.jpg?raw=true) | ![Screenshots](fastlane/metadata/android/en-US/images/phoneScreenshots/08.jpg?raw=true) |

|                                         Drawer                                          |                                         Setting                                         |
|:---------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------:|
| ![Screenshots](fastlane/metadata/android/en-US/images/phoneScreenshots/03.jpg?raw=true) | ![Screenshots](fastlane/metadata/android/en-US/images/phoneScreenshots/01.jpg?raw=true) |

|                                          Songs                                          |                                         Folders                                         |
|:---------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------:|
| ![Screenshots](fastlane/metadata/android/en-US/images/phoneScreenshots/09.jpg?raw=true) | ![Screenshots](fastlane/metadata/android/en-US/images/phoneScreenshots/10.jpg?raw=true) |

|                                         Artists                                         |                                        Playlists                                        |
|:---------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------:|
| ![Screenshots](fastlane/metadata/android/en-US/images/phoneScreenshots/07.jpg?raw=true) | ![Screenshots](fastlane/metadata/android/en-US/images/phoneScreenshots/06.jpg?raw=true) |

|                                        Song Menu                                        |                                 Tag Editor (Deprecated)                                 |
|:---------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------:|
| ![Screenshots](fastlane/metadata/android/en-US/images/phoneScreenshots/02.jpg?raw=true) | ![Screenshots](fastlane/metadata/android/en-US/images/phoneScreenshots/04.jpg?raw=true) |

## **Build Instructions & Developer Guide**

See [Build_Instructions.md](./Build_Instructions.md)

## **Development Plan (or Road Map?)** & **TO-DO list**

### **2022**

- [x] Refactor File Fragment

- [x] Refactor Library UI

- [x] Better 'My Top Songs' algorithm

- [x] Complete README

- [x] Refactor MusicService

- [x] Migrate Glide to Coil

- [x] Whitelist

- [x] Migrate Song Detail to Jetpack Compose

- [x] Refactor Update Dialog

- [x] User-defined click behavior for songs

- [x] Support history of playing queue.

- [ ] Refactor Setting UI (WIP⭕)

- [ ] Refactor Search

- [ ] Use AndroidX Room to build
  Media database, to parse multi-artists songs and ‘;’, '&', '/', '\', ',' in tags, and improve search result

- [ ] ...

### **2023~2024(?)**

- [ ] Rewrite Tag Editor (Single File⭕, Batch❌)

- [ ] Refactor Main Player (WIP⭕)

- [ ] Enhance Playlist Detail: support search ❌, Better way to
  modify ❗WIP, handle intent of open (playlist) file ❌

- [ ] Desktop lyrics (?)

- [ ] Support some Android's StatusBar lyrics, such as FlyMe / EvolutionX

- [ ] improve SlidingMusicBar

- [ ] Valid files</del>

- [ ] Adapter Android11+ File Permission perfectly</del>

- [ ] Refactor so-called Theme Engine</del>

- [ ] Make songs listening statistics</del>

- [ ] ...

<br/>
<br/>
<br/>
<br/>

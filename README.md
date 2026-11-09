# Purely Music · 文件夹导入增强版

[![Android 8.0+](https://img.shields.io/badge/Android-8.0%2B-3DDC84?logo=android&logoColor=white)](https://developer.android.com/about/versions/oreo)
[![License: Mulan PSL v2](https://img.shields.io/badge/License-Mulan%20PSL%20v2-blue.svg)](LICENSE)
[![Android CI](https://github.com/XDXxc95/pure_music_pro/actions/workflows/android.yml/badge.svg)](https://github.com/XDXxc95/pure_music_pro/actions/workflows/android.yml)

Purely Music 是一款开源 Android 本地音乐播放器：导入设备中的音乐，整理专辑与播放列表，跟随同步歌词聆听，并按自己的喜好调整播放衔接与声音效果。

> **本仓库是上游项目 [Purely Music](https://github.com/eason204646-droid/Purely-Music) 的修改版本（fork）**，基于上游 `3.0`（`versionCode 30`）源码修改，遵循 [木兰宽松许可证，第 2 版（Mulan PSL v2）](LICENSE) 发布。全部修改内容记录在 [NOTICE.md](NOTICE.md)。

## 本版本相对上游的修改

| 能力 | 本版本行为 |
| --- | --- |
| **导入文件夹**（新增） | 导入菜单新增「导入文件夹」入口。选取一个文件夹后递归遍历其中的音频文件（子目录层级上限 12 层），一次完成整个文件夹的导入。 |
| **零复制导入** | 文件夹导入直接以系统文档 URI（SAF）映射原文件位置并写入歌曲表，不再把音频复制进应用私有目录。导入耗时取决于文件数量，与音频体积无关。 |
| **按子文件夹建播放列表** | 以音频文件所在子文件夹名自动创建播放列表；多层目录用 `" / "` 连接（例如 `歌手 / 专辑`）。同名播放列表已存在时追加歌曲并自动去重，封面缺失时自动补齐。 |
| **歌名取原始文件名** | 导入后显示的歌曲名为磁盘文件名去掉音频扩展名；音频标签中的曲名仅用于联网检索元数据。 |
| **后台补全元数据** | 导入完成即可使用，歌手、封面与歌词在后台读取内嵌标签并按设置异步联网补全，不阻塞导入流程。 |

单曲导入与多选批量导入保持上游行为（会把音频复制到应用私有目录），因为这两类文件选择器只提供临时读取授权。修改的完整清单与说明见 [NOTICE.md](NOTICE.md)。

## 功能

- **本地音乐库**：导入单曲、多个文件，或整个文件夹；按歌曲、专辑和自建播放列表浏览与管理。
- **播放器与系统控制**：后台播放、播放队列、最近播放、通知栏与锁屏媒体控制，以及可设置停止时间的睡眠定时器。
- **歌词**：解析并同步显示 LRC 歌词，点击歌词跳转到对应进度；支持单行/多行样式、敏感词过滤与非中文歌词翻译。
- **自动补全**：联网获取歌曲封面与歌词，可在设置中选择获取源。网络补全需要配置 `MUSIC_API_KEY`；本地播放不依赖网络。
- **播放与声音设置**：智能混音（AutoMix）分析曲目片段并尝试匹配节拍与调性，也可选择固定时长交叉渐变；另有均衡器、环绕音效与歌词显示选项。
- **个性化**：中文与 English 界面，并根据系统外观适配明暗主题。

智能混音与固定时长交叉渐变是两种可选的自动切歌方式。若歌曲不适合分析或设备无法处理，应用会继续普通播放。

### 支持的音频格式

文件夹导入按扩展名或 MIME 类型识别音频，覆盖 `mp3`、`flac`、`m4a`、`m4b`、`aac`、`wav`、`ogg`、`opus`、`ape`、`wma`、`amr`、`3gp`、`3g2`、`mka`、`mp4`、`mp2`、`mpga`、`aif`、`aiff`、`alac`、`wv`、`dsf`、`dff` 等常见格式。实际播放能力取决于系统解码器与内置的 FFmpeg 解码组件。

## 获取应用

前往本仓库的 [Releases](https://github.com/XDXxc95/pure_music_pro/releases/latest) 下载 `purelymusic.apk`，要求 **Android 8.0（API 26）或更高版本**。

该安装包为 debug 签名构建，与官方版本的签名不同，安装前需要先卸载已安装的官方版本；安装包内不含任何联网服务密钥。

## 环境要求

- JDK 17
- Android SDK 36（`compileSdk = 36`，`targetSdk = 36`，`minSdk = 26`）
- Git

## 从源码构建

```bash
git clone https://github.com/XDXxc95/pure_music_pro.git
cd pure_music_pro
```

macOS / Linux：

```bash
./gradlew assembleDebug
```

Windows PowerShell：

```powershell
.\gradlew.bat assembleDebug
```

Debug 安装包输出到 `app/build/outputs/apk/debug/purelymusic.apk`。也可以直接用 Android Studio 打开仓库并运行 `app` 配置。首次构建需要联网下载 Gradle 发行版与依赖。

### 可选：配置联网服务

歌曲信息补全与歌词翻译使用 `MUSIC_API_KEY`。在项目根目录的 `local.properties` 中添加自己的服务密钥后重新构建：

```properties
MUSIC_API_KEY=your_api_key
```

未配置密钥时应用仍可正常本地播放。请勿将密钥提交到版本库，也不要在公开分发的安装包中复用个人密钥：该值会被打包进客户端，无法作为服务端机密保管。

## 测试与持续集成

```bash
./gradlew testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
```

Windows PowerShell：

```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug assembleDebugAndroidTest
```

其中 `assembleDebugAndroidTest` 只编译仪器测试，运行仪器测试需要已连接的设备或模拟器。仓库的 GitHub Actions 会在推送到 `main` 分支或创建 Pull Request 时执行单元测试、Android Lint 与 Debug 构建；构建通过后可在该次运行的 Artifacts 中下载 Debug APK、测试报告与 Lint 报告。

## 项目结构

```text
app/src/main/java/com/music/purelymusic/
├── data/       Room 数据库、文件存储与在线元数据获取
├── playback/   后台播放、音频分析与过渡渲染
├── ui/         Jetpack Compose 界面
├── utils/      歌词解析、语言检测、导入支持与偏好设置
└── viewmodel/  界面状态和应用逻辑
```

文件夹导入相关的核心实现：

- `utils/FolderImportSupport.kt`：音频文件识别、文件名转歌名、播放列表命名规则（纯 Kotlin，便于单元测试）
- `viewmodel/PlayerViewModel.kt`：目录树扫描、导入表登记、播放列表持久化、后台元数据补全
- `data/SongDao.kt`：按主键查询歌曲

## 文档

- [使用说明](help/使用说明.md)
- [功能特性](help/功能特性.md)
- [疑难解答](help/疑难解答.md)
- [歌词翻译说明](help/歌词翻译说明.md)

## 许可证与合规

本项目采用 [木兰宽松许可证，第 2 版（Mulan PSL v2）](LICENSE)。

再分发时请遵守以下事项：

- 向接收者提供本许可证的副本（即保留 [LICENSE](LICENSE) 文件），并保留软件中的版权、商标、专利及免责声明；
- 源文件头部原有的版权与许可证声明保持不变；
- 本仓库的修改内容记录在 [NOTICE.md](NOTICE.md) 中，修改后的代码同样以 Mulan PSL v2 分发。

## 上游与致谢

- 上游项目：<https://github.com/eason204646-droid/Purely-Music>
- 上游版权：Copyright (c) 2026 eason204646
- 本仓库的修改部分：见 [NOTICE.md](NOTICE.md)

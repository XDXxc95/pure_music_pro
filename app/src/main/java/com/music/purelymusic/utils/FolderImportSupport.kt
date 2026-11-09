//Copyright (c) [2026] [eason204646]
//[purelymusic] is licensed under Mulan PSL v2.
//You can use this software according to the terms and conditions of the Mulan
//PSL v2.
//You may obtain a copy of Mulan PSL v2 at:
//         http://license.coscl.org.cn/MulanPSL2
//THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY
//KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
//NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
//See the Mulan PSL v2 for more details.
//
//Mulan Permissive Software License，Version 2
//
//Mulan Permissive Software License，Version 2 (Mulan PSL v2)
//
//January 2020 http://license.coscl.org.cn/MulanPSL2
package com.music.purelymusic.utils

/**
 * 文件夹导入的纯逻辑：判断文件是否音频、以及按所在文件夹生成歌单名。
 * 刻意不依赖 Android 框架，方便单元测试。
 */
object FolderImportSupport {
    private val AUDIO_EXTENSIONS = setOf(
        "mp3", "flac", "m4a", "m4b", "aac", "wav", "ogg", "opus", "ape", "wma",
        "amr", "3gp", "3g2", "mka", "mp4", "mp2", "mpga", "aif", "aiff", "alac",
        "wv", "dsf", "dff"
    )

    /** 取小写扩展名（不含点），没有扩展名时返回空串。 */
    fun extensionOf(fileName: String?): String =
        fileName?.substringAfterLast('.', "")?.trim()?.lowercase().orEmpty()

    fun isAudioFileName(fileName: String?): Boolean = extensionOf(fileName) in AUDIO_EXTENSIONS

    /**
     * 由文件名生成歌曲名：去掉音频扩展名，保留文件名原样。
     * 文件夹导入时歌名以用户磁盘上的原始文件名为准，而不是内嵌标签里的名字。
     */
    fun titleFromFileName(fileName: String?): String {
        val name = fileName?.trim().orEmpty()
        if (name.isEmpty()) return "Untitled"
        val extension = extensionOf(name)
        val withoutExtension = if (extension in AUDIO_EXTENSIONS && name.length > extension.length + 1) {
            name.dropLast(extension.length + 1)
        } else {
            name
        }
        return withoutExtension.trim().ifEmpty { name }
    }

    /** 是否为 LRC 歌词文件（扩展名为 .lrc，大小写不敏感）。 */
    fun isLrcFileName(fileName: String?): Boolean = extensionOf(fileName) == "lrc"

    /**
     * 判断歌词文件是否属于某个音频文件（同目录同名是最常见的整理方式）：
     * `晴天.mp3` 对应 `晴天.lrc`，也兼容 `晴天.mp3.lrc` 这种写法。
     * 比较时忽略大小写与首尾空白。
     */
    fun lrcMatchesAudio(audioFileName: String?, lrcFileName: String?): Boolean {
        val lrc = lrcFileName?.trim()?.lowercase().orEmpty()
        if (!isLrcFileName(lrc)) return false
        val lrcBase = lrc.removeSuffix(".lrc")
        if (lrcBase.isEmpty()) return false
        val audio = audioFileName?.trim()?.lowercase().orEmpty()
        if (audio.isEmpty()) return false
        val extension = extensionOf(audio)
        val audioBase = if (extension in AUDIO_EXTENSIONS && audio.length > extension.length + 1) {
            audio.dropLast(extension.length + 1)
        } else {
            audio
        }
        return lrcBase == audioBase || lrcBase == audio
    }

    fun isAudioMimeType(mimeType: String?): Boolean {
        val mime = mimeType?.lowercase() ?: return false
        return mime.startsWith("audio/") || mime == "application/ogg" || mime == "application/x-ogg"
    }

    /**
     * 按音乐文件*所在*的文件夹名生成歌单名。
     * 直接位于根目录下的文件用根文件夹名；子目录用相对路径拼接，
     * 例如子目录 `周杰伦/范特西` 生成 `周杰伦 / 范特西`，避免不同层级的同名文件夹互相覆盖。
     */
    fun playlistNameFor(rootFolderName: String?, relativeFolderSegments: List<String>): String {
        val segments = relativeFolderSegments.map { it.trim() }.filter { it.isNotEmpty() }
        if (segments.isNotEmpty()) return segments.joinToString(" / ")
        return rootFolderName?.trim()?.takeUnless { it.isEmpty() } ?: "Imported"
    }
}

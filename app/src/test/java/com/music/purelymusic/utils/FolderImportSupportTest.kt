package com.music.purelymusic.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** FolderImportSupport 的单元测试：音频判定与按文件夹生成歌单名。 */
class FolderImportSupportTest {

    @Test
    fun `recognises common audio extensions case insensitively`() {
        assertTrue(FolderImportSupport.isAudioFileName("track.mp3"))
        assertTrue(FolderImportSupport.isAudioFileName("Track.FLAC"))
        assertTrue(FolderImportSupport.isAudioFileName("a.m4a"))
        assertTrue(FolderImportSupport.isAudioFileName("x.wav"))
        assertTrue(FolderImportSupport.isAudioFileName("x.ape"))
    }

    @Test
    fun `rejects non audio files and blank names`() {
        assertFalse(FolderImportSupport.isAudioFileName("cover.jpg"))
        assertFalse(FolderImportSupport.isAudioFileName("lyric.lrc"))
        assertFalse(FolderImportSupport.isAudioFileName("no-extension"))
        assertFalse(FolderImportSupport.isAudioFileName(null))
        assertFalse(FolderImportSupport.isAudioFileName(""))
    }

    @Test
    fun `recognises audio mime types`() {
        assertTrue(FolderImportSupport.isAudioMimeType("audio/mpeg"))
        assertTrue(FolderImportSupport.isAudioMimeType("AUDIO/FLAC"))
        assertTrue(FolderImportSupport.isAudioMimeType("application/ogg"))
        assertFalse(FolderImportSupport.isAudioMimeType("image/jpeg"))
        assertFalse(FolderImportSupport.isAudioMimeType(null))
    }

    @Test
    fun `uses subfolder name as playlist name`() {
        assertEquals("周杰伦", FolderImportSupport.playlistNameFor("Music", listOf("周杰伦")))
        assertEquals("Album A", FolderImportSupport.playlistNameFor("Music", listOf("Album A")))
    }

    @Test
    fun `nested folders join relative path to avoid collisions`() {
        assertEquals(
            "周杰伦 / 范特西",
            FolderImportSupport.playlistNameFor("Music", listOf("周杰伦", "范特西"))
        )
    }

    @Test
    fun `files directly in root use root folder name`() {
        assertEquals("My Music", FolderImportSupport.playlistNameFor("My Music", emptyList()))
        assertEquals("My Music", FolderImportSupport.playlistNameFor("My Music", listOf("", "  ")))
        assertEquals("Imported", FolderImportSupport.playlistNameFor(null, emptyList()))
        assertEquals("Imported", FolderImportSupport.playlistNameFor("   ", emptyList()))
    }

    @Test
    fun `segments are trimmed`() {
        assertEquals("A / B", FolderImportSupport.playlistNameFor("root", listOf(" A ", " B ")))
    }

    @Test
    fun `song title comes from original file name`() {
        assertEquals("晴天", FolderImportSupport.titleFromFileName("晴天.mp3"))
        assertEquals("01 - Intro", FolderImportSupport.titleFromFileName("01 - Intro.flac"))
        assertEquals("Song.2019", FolderImportSupport.titleFromFileName("Song.2019.m4a"))
        assertEquals("track", FolderImportSupport.titleFromFileName("track"))
        assertEquals("Untitled", FolderImportSupport.titleFromFileName(null))
        assertEquals("Untitled", FolderImportSupport.titleFromFileName("   "))
    }

    @Test
    fun `recognises lrc files`() {
        assertTrue(FolderImportSupport.isLrcFileName("晴天.lrc"))
        assertTrue(FolderImportSupport.isLrcFileName("TRACK.LRC"))
        assertFalse(FolderImportSupport.isLrcFileName("晴天.mp3"))
        assertFalse(FolderImportSupport.isLrcFileName("lrc"))
        assertFalse(FolderImportSupport.isLrcFileName(null))
    }

    @Test
    fun `matches lyric file sitting next to its audio file`() {
        assertTrue(FolderImportSupport.lrcMatchesAudio("晴天.mp3", "晴天.lrc"))
        assertTrue(FolderImportSupport.lrcMatchesAudio("01 - Intro.flac", "01 - intro.LRC"))
        assertTrue(FolderImportSupport.lrcMatchesAudio("Song.2019.m4a", "Song.2019.lrc"))
        assertTrue(FolderImportSupport.lrcMatchesAudio("track", "track.lrc"))
        // 有些整理方式把扩展名也写进歌词文件名
        assertTrue(FolderImportSupport.lrcMatchesAudio("晴天.mp3", "晴天.mp3.lrc"))
        assertFalse(FolderImportSupport.lrcMatchesAudio("晴天.mp3", "雨天.lrc"))
        assertFalse(FolderImportSupport.lrcMatchesAudio("晴天.mp3", "晴天.txt"))
        assertFalse(FolderImportSupport.lrcMatchesAudio(null, "晴天.lrc"))
        assertFalse(FolderImportSupport.lrcMatchesAudio("晴天.mp3", null))
    }
}

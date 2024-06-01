package com.facebook.subtitle_demo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.SubtitleView
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.Util

class MainActivity : AppCompatActivity() {
    private lateinit var exoPlayerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var subtitleView: SubtitleView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        exoPlayerView = findViewById(R.id.player_view)
        subtitleView =findViewById(R.id.exo_subtitles)


        // Create an instance of the ExoPlayer
        exoPlayer = SimpleExoPlayer.Builder(this).build()
        // Set the player to the PlayerView
        exoPlayerView.player = exoPlayer

        /*// Create a default DataSourceFactory
        dataSourceFactory = DefaultDataSourceFactory(this, "ExoPlayerDemo")

        // Example MP4 URL
        val mp4Uri = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

        // Example HLS (m3u8) URL
        val hlsUri = "https://futuretdy.s.llnwi.net/v4/files/a8wn4hw/vi/c1/2b/10463341/s10463341.m3u8"

        // Create MediaItems for MP4 and HLS streams
        val mp4MediaItem = MediaItem.fromUri(mp4Uri)
        val hlsMediaItem = MediaItem.fromUri(hlsUri)

        // Create MediaSources for MP4 and HLS
        val mp4MediaSource: MediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mp4MediaItem)

        val hlsMediaSource: MediaSource =
            com.google.android.exoplayer2.source.hls.HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(hlsMediaItem)
        // Set up ExoPlayer with the MediaSources
        exoPlayer.setMediaSource(hlsMediaSource)*/

        val dataSourceFactory = DefaultHttpDataSource.Factory().apply {
            setUserAgent(
                Util.getUserAgent(
                    this@MainActivity,
                    getString(R.string.app_name)
                ))
        }

        val subtitleUri = Uri.parse("https://futuretdy.s.llnwi.net/v4/files/a8wn4hw/cc/02/8d/f041a53e58806a8ed358fc844ed72de7.vtt")

        val subtitle = MediaItem.SubtitleConfiguration.Builder(subtitleUri)
            .setMimeType(MimeTypes.TEXT_VTT) // The correct MIME type (required).
            .setLanguage("en") // MUST, The subtitle language (optional).
            .setSelectionFlags(C.SELECTION_FLAG_DEFAULT) //MUST,  Selection flags for the track (optional).
            .build()

        val subtitleSource = SingleSampleMediaSource.Factory(dataSourceFactory)
            .createMediaSource(subtitle, C.TIME_UNSET)


        val mediaItem: MediaItem = MediaItem.Builder()
            .setUri("https://futuretdy.s.llnwi.net/v4/files/a8wn4hw/vi/31/a1/d3e17b08400e7a75edd239757926e2da.mp4")//videouri
            .build()

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        // merge subtitle source and media source
        val mediaSourceWithsubtitle = MergingMediaSource(mediaSource, subtitleSource)

        exoPlayer?.setMediaSource(mediaSourceWithsubtitle)

        exoPlayer.prepare()
        exoPlayer.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
}

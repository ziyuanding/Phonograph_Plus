/*
 * Copyright (c) 2022 chr_56 & Abou Zeid (kabouzeid) (original author)
 */

package player.phonograph.ui.fragments.player.card

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import player.phonograph.databinding.FragmentCardPlayerPlaybackControlsBinding
import player.phonograph.helper.MusicPlayerRemote
import player.phonograph.helper.PlayPauseButtonOnClickHandler
import player.phonograph.ui.fragments.player.AbsPlayerControllerFragment
import player.phonograph.views.PlayPauseDrawable
import util.mdcolor.ColorUtil
import util.mddesign.util.MaterialColorHelper
import util.mddesign.util.TintHelper

class CardPlayerControllerFragment : AbsPlayerControllerFragment() {

    private var viewBinding: FragmentCardPlayerPlaybackControlsBinding? = null
    private val v: FragmentCardPlayerPlaybackControlsBinding get() = viewBinding!!

    private var _playerPlayPauseFab: FloatingActionButton? = null
    val playerPlayPauseFab: FloatingActionButton get() =  _playerPlayPauseFab!!
    var progressSliderHeight: Int = 0
        private set

    override fun bindView(inflater: LayoutInflater): View {
        viewBinding = FragmentCardPlayerPlaybackControlsBinding.inflate(inflater)
        _playerPlayPauseFab = v.playerPlayPauseFab
        prevButton = v.playerPrevButton
        nextButton = v.playerNextButton
        repeatButton = v.playerRepeatButton
        shuffleButton = v.playerShuffleButton
        progressSlider = v.playerProgressSlider
        songCurrentProgress = v.playerSongCurrentProgress
        songTotalTime = v.playerSongTotalTime
        return v.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressSliderHeight = v.playerProgressSlider.height
        super.onViewCreated(view, savedInstanceState)
    }

    override fun unbindView() {
        viewBinding = null
        _playerPlayPauseFab = null
        progressSliderHeight = -1
    }

    override fun setUpPlayPauseButton() {
        val fabColor = Color.WHITE
        TintHelper.setTintAuto(v.playerPlayPauseFab, fabColor, true)

        playPauseDrawable = PlayPauseDrawable(requireActivity())
        playPauseDrawable.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                MaterialColorHelper.getSecondaryTextColor(requireContext(), ColorUtil.isColorLight(fabColor)), BlendModeCompat.SRC_IN
            )

        v.playerPlayPauseFab.setImageDrawable(playPauseDrawable) // Note: set the drawable AFTER TintHelper.setTintAuto() was called

        v.playerPlayPauseFab.setOnClickListener(PlayPauseButtonOnClickHandler())
        v.playerPlayPauseFab.post {
            v.playerPlayPauseFab.pivotX = v.playerPlayPauseFab.width.toFloat() / 2
            v.playerPlayPauseFab.pivotY = v.playerPlayPauseFab.height.toFloat() / 2
        }
    }

    override fun updatePlayPauseDrawableState(animate: Boolean) {
        if (MusicPlayerRemote.isPlaying()) {
            playPauseDrawable.setPause(animate)
        } else {
            playPauseDrawable.setPlay(animate)
        }
    }

    override fun show() {
        v.playerPlayPauseFab.animate()
            .scaleX(1f)
            .scaleY(1f)
            .rotation(360f)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    override fun hide() {
        v.playerPlayPauseFab.apply {
            scaleX = 0f
            scaleY = 0f
            rotation = 0f
        }
    }
}

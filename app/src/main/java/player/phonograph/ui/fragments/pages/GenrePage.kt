/*
 * Copyright (c) 2022 chr_56 & Abou Zeid (kabouzeid) (original author)
 */

package player.phonograph.ui.fragments.pages

import player.phonograph.BuildConfig.DEBUG
import player.phonograph.R
import player.phonograph.adapter.display.DisplayAdapter
import player.phonograph.adapter.display.GenreDisplayAdapter
import player.phonograph.mediastore.GenreLoader
import player.phonograph.model.Genre
import player.phonograph.model.sort.SortMode
import player.phonograph.model.sort.SortRef
import player.phonograph.ui.components.popup.ListOptionsPopup
import player.phonograph.ui.fragments.pages.util.DisplayConfig
import player.phonograph.ui.fragments.pages.util.DisplayConfigTarget
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope

class GenrePage : AbsDisplayPage<Genre, DisplayAdapter<Genre>, GridLayoutManager>() {

    override val viewModel: AbsDisplayPageViewModel<Genre> get() = _viewModel

    private val _viewModel: GenrePageViewModel by viewModels()

    class GenrePageViewModel : AbsDisplayPageViewModel<Genre>() {
        override suspend fun loadDataSetImpl(context: Context, scope: CoroutineScope): Collection<Genre> {
            return GenreLoader.getAllGenres(context)
        }

        override val headerTextRes: Int get() = R.plurals.item_genres
    }


    override val displayConfigTarget get() = DisplayConfigTarget.GenrePage

    override fun initLayoutManager(): GridLayoutManager {
        return GridLayoutManager(hostFragment.requireContext(), 1)
            .also { it.spanCount = DisplayConfig(displayConfigTarget).gridSize }
    }

    override fun initAdapter(): DisplayAdapter<Genre> {
        return GenreDisplayAdapter(
            hostFragment.mainActivity,
            hostFragment.cabController,
            ArrayList(), // empty until Genre loaded
            R.layout.item_list_no_image
        ) {
            showSectionName = true
        }
    }


    override fun updateDataset() {
        adapter.dataset = viewModel.dataSet.value.toList()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun refreshDataSet() {
        adapter.notifyDataSetChanged()
    }


    override fun setupSortOrderImpl(
        displayConfig: DisplayConfig,
        popup: ListOptionsPopup,
    ) {

        val currentSortMode = displayConfig.sortMode
        if (DEBUG) Log.d(TAG, "Read cfg: sortMode $currentSortMode")

        popup.allowRevert = true
        popup.revert = currentSortMode.revert

        popup.sortRef = currentSortMode.sortRef
        popup.sortRefAvailable = arrayOf(SortRef.DISPLAY_NAME, SortRef.SONG_COUNT)
    }

    override fun saveSortOrderImpl(
        displayConfig: DisplayConfig,
        popup: ListOptionsPopup,
    ) {
        val selected = SortMode(popup.sortRef, popup.revert)
        if (displayConfig.sortMode != selected) {
            displayConfig.sortMode = selected
            viewModel.loadDataset(requireContext())
            Log.d(TAG, "Write cfg: sortMode $selected")
        }
    }

    companion object {
        const val TAG = "GenrePage"
    }
}

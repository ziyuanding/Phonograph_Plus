/*
 *  Copyright (c) 2022~2023 chr_56
 */

package player.phonograph.ui.modules.playlist

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemState
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemViewHolder
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.draggable.annotation.DraggableItemStateFlags
import player.phonograph.R
import player.phonograph.mechanism.actions.ActionMenuProviders
import player.phonograph.mechanism.actions.ClickActionProviders
import player.phonograph.model.Song
import player.phonograph.model.UIMode
import player.phonograph.ui.adapter.OrderedItemAdapter
import player.phonograph.util.produceSafeId
import player.phonograph.util.ui.hitTest
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.coroutines.launch

class PlaylistSongDisplayAdapter(
    activity: FragmentActivity,
    val viewModel: PlaylistDetailViewModel,
) : OrderedItemAdapter<Song>(activity, R.layout.item_list, showSectionName = true),
    DraggableItemAdapter<PlaylistSongDisplayAdapter.PlaylistSongViewHolder> {

    override fun getItemId(position: Int): Long =
        produceSafeId(dataset[position].getItemID(), position)

    override fun getSectionNameImp(position: Int): String = (position + 1).toString()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderedItemViewHolder<Song> {
        return PlaylistSongViewHolder(inflatedView(parent, viewType))
    }

    override fun onBindViewHolder(holder: OrderedItemViewHolder<Song>, position: Int) {
        if (viewModel.currentMode.value == UIMode.Editor) {
            holder.dragView?.visibility = View.VISIBLE
        }
        super.onBindViewHolder(holder, position)
    }

    override fun onCheckCanStartDrag(holder: PlaylistSongViewHolder, position: Int, x: Int, y: Int): Boolean =
        position >= 0 && (hitTest(holder.dragView!!, x, y) || hitTest(holder.image!!, x, y))

    override fun onGetItemDraggableRange(holder: PlaylistSongViewHolder, position: Int): ItemDraggableRange =
        ItemDraggableRange(0, dataset.size - 1)

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
        activity.lifecycleScope.launch { moveSong(fromPosition, toPosition) }
    }

    private suspend fun moveSong(fromPosition: Int, toPosition: Int) {
        if (viewModel.moveItem(activity, fromPosition, toPosition).await()) {
            synchronized(dataset) {
                val newSongs: MutableList<Song> = dataset.toMutableList()
                val song = newSongs.removeAt(fromPosition)
                newSongs.add(toPosition, song)
                dataset = newSongs
            }
            // if (fromPosition > toPosition)
            //     notifyItemRangeChanged(toPosition, fromPosition)
            // else
            //     notifyItemRangeChanged(fromPosition, toPosition)
        }
    }

    private suspend fun deleteSong(position: Int) {
        val song = dataset[position]
        if (viewModel.deleteItem(activity, song, position).await()) {
            synchronized(dataset) {
                dataset = dataset.toMutableList().also { it.removeAt(position) }
            }
            notifyItemRangeChanged(position, dataset.size - 1)
        }
    }

    override fun onCheckCanDrop(draggingPosition: Int, dropPosition: Int): Boolean =
        (dropPosition >= 0) && (dropPosition <= dataset.size - 1)

    override fun onItemDragStarted(position: Int) {}

    override fun onItemDragFinished(fromPosition: Int, toPosition: Int, result: Boolean) {
        @Suppress("KotlinConstantConditions")
        when {
            fromPosition < toPosition  -> notifyItemRangeChanged(fromPosition, toPosition)
            fromPosition > toPosition  -> notifyItemRangeChanged(toPosition, fromPosition)
            fromPosition == toPosition -> notifyItemChanged(fromPosition)
        }

        // special for top and bottle
        if (fromPosition == 0) {
            notifyItemChanged(toPosition)
        }
        if (fromPosition == dataset.size - 1 && toPosition == 0) {
            notifyItemChanged(dataset.size - 1)
        }
    }

    inner class PlaylistSongViewHolder(itemView: View) :
            OrderedItemViewHolder<Song>(itemView),
            DraggableItemViewHolder {

        override fun onClick(position: Int, dataset: List<Song>, imageView: ImageView?): Boolean {
            return ClickActionProviders.SongClickActionProvider()
                .listClick(dataset, position, itemView.context, imageView)
        }


        override fun prepareMenu(item: Song, position: Int, menuButtonView: View) {
            menuButtonView.setOnClickListener {
                if (viewModel.currentMode.value == UIMode.Editor) {
                    PlaylistEditorItemMenuProvider(position, ::dataset, ::deleteSong, ::moveSong)
                        .prepareMenu(menuButtonView, item)
                } else {
                    ActionMenuProviders.SongActionMenuProvider(showPlay = false)
                        .prepareMenu(menuButtonView, item)
                }
            }
        }

        override fun getRelativeOrdinalText(item: Song, position: Int): String = (position + 1).toString()

        //region DragState
        @DraggableItemStateFlags
        private var mDragStateFlags = 0

        @DraggableItemStateFlags
        override fun getDragStateFlags(): Int = mDragStateFlags
        override fun setDragStateFlags(@DraggableItemStateFlags flags: Int) {
            mDragStateFlags = flags
        }

        override fun getDragState(): DraggableItemState {
            return DraggableItemState().apply {
                this.flags = mDragStateFlags
            }
        }
        //endregion
    }
}

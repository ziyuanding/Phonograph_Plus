/*
 *  Copyright (c) 2022~2024 chr_56
 */

package player.phonograph.ui.modules.playlist

import com.github.chr56.android.menu_dsl.attach
import com.github.chr56.android.menu_dsl.menuItem
import com.github.chr56.android.menu_dsl.submenu
import player.phonograph.R
import player.phonograph.mechanism.actions.ActionMenuProviders.ActionMenuProvider
import player.phonograph.mechanism.actions.actionGotoDetail
import player.phonograph.model.Song
import player.phonograph.ui.dialogs.DeletionDialog
import player.phonograph.ui.modules.tag.TagBrowserActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import android.content.Context
import android.view.Menu
import kotlinx.coroutines.launch


class PlaylistEditorItemMenuProvider(
    private val bindingAdapterPosition: Int,
    private val dataset: () -> List<Song>,
    private val deleteSong: suspend (Int) -> Unit,
    private val moveSong: suspend (Int, Int) -> Unit,
) : ActionMenuProvider<Song> {
    override fun inflateMenu(menu: Menu, context: Context, item: Song) {
        editorItemMenu(menu, context as FragmentActivity, dataset(), bindingAdapterPosition, deleteSong, moveSong)
    }


    private fun editorItemMenu(
        menu: Menu,
        activity: FragmentActivity,
        dataset: List<Song>,
        bindingAdapterPosition: Int,
        deleteSong: suspend (Int) -> Unit,
        moveSong: suspend (Int, Int) -> Unit,
    ) = attach(activity, menu) {
        val song = dataset[bindingAdapterPosition]
        menuItem {
            titleRes(R.string.action_remove_from_playlist)
            onClick {
                activity.lifecycleScope.launch { deleteSong(bindingAdapterPosition) }
                true
            }
        }
        menuItem {
            titleRes(R.string.move_to_top)
            onClick {
                activity.lifecycleScope.launch { moveSong(bindingAdapterPosition, 0) }
                true
            }
        }
        menuItem {
            titleRes(R.string.move_up)
            onClick {
                if (bindingAdapterPosition != 0) {
                    activity.lifecycleScope.launch { moveSong(bindingAdapterPosition, bindingAdapterPosition - 1) }
                    true
                } else false
            }
        }
        menuItem {
            titleRes(R.string.move_down)
            onClick {
                if (bindingAdapterPosition != dataset.size - 1) {
                    activity.lifecycleScope.launch { moveSong(bindingAdapterPosition, bindingAdapterPosition + 1) }
                    true
                } else false
            }
        }
        menuItem {
            titleRes(R.string.move_to_bottom)
            onClick {
                activity.lifecycleScope.launch { moveSong(bindingAdapterPosition, dataset.size - 1) }
                true
            }
        }

        menuItem {
            titleRes(R.string.action_details)
            onClick {
                song.actionGotoDetail(activity)
                true
            }
        }

        submenu(
            context.getString(R.string.more_actions)
        ) {
            menuItem {
                titleRes(R.string.action_tag_editor)
                onClick {
                    TagBrowserActivity.launch(activity, song.data)
                    true
                }
            }
            menuItem {
                titleRes(R.string.action_delete_from_device)
                onClick {
                    DeletionDialog.create(arrayListOf(song))
                        .show(activity.supportFragmentManager, "DELETE_SONGS")
                    true
                }
            }
        }
    }
}
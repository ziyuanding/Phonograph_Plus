/*
 * Copyright (c) 2022 chr_56
 */

package player.phonograph.ui.fragments.explorer

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView.SectionedAdapter
import player.phonograph.adapter.base.MultiSelectionAdapterContract
import player.phonograph.adapter.base.MultiSelectionCabController
import player.phonograph.adapter.base.MultiSelectionController
import player.phonograph.databinding.ItemListBinding
import player.phonograph.model.file.FileEntity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import android.annotation.SuppressLint

abstract class AbsFilesAdapter<VH : AbsFilesAdapter.ViewHolder>(
    val activity: ComponentActivity,
    dataset: MutableList<FileEntity>,
    cabController: MultiSelectionCabController?,
) : RecyclerView.Adapter<VH>(),
    SectionedAdapter,
    MultiSelectionAdapterContract<FileEntity> {

    var dataSet: MutableList<FileEntity> = dataset
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //todo
    protected val controller: MultiSelectionController<FileEntity> =
        MultiSelectionController(
            this,
            cabController,
            { activity as? androidx.activity.ComponentActivity },
            multiSelectMenuHandler
        )

    abstract val multiSelectMenuHandler: ((Toolbar) -> Boolean)?

    override fun getItem(datasetPosition: Int): FileEntity = dataSet[datasetPosition]

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(dataSet[position], position, controller)
    }

    override fun getSectionName(position: Int): String = dataSet[position].name.take(2)

    abstract class ViewHolder(var binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: FileEntity, position: Int, controller: MultiSelectionController<FileEntity>)
    }

}

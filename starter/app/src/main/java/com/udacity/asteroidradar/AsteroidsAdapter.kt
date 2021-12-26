package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidItemViewBinding
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class AsteroidsAdapter(private val clickListener: AsteroidListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(AsteroidDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<Asteroid>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.AsteroidItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> AsteroidViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewtype $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AsteroidViewHolder -> {
                Timber.e("is AsteroidViewHolder")
                val asteroidItem = getItem(position) as DataItem.AsteroidItem
                holder.bind(asteroidItem, clickListener)
                Timber.e("is AsteroidViewHolder / when holder")
            }
            else -> Timber.e("else / when holder")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            else -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        val itemCount = super.getItemCount()
        Timber.e("Asteroids to render in recyclerview:${itemCount-1}") // remove one -> header
        return itemCount
    }
    class AsteroidViewHolder private constructor(private val binding: AsteroidItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItem.AsteroidItem, clickListener: AsteroidListener) {
            binding.asteroid = item.asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidItemViewBinding.inflate(layoutInflater, parent, false)
                return AsteroidViewHolder(binding)
            }
        }
    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

class AsteroidDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return false // return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return false //  oldItem.id == newItem.id  // Asteroid values are not changed over time so if the id is the same, it is the same asteroid
    }

}

class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}

sealed class DataItem {
    data class AsteroidItem(val asteroid: Asteroid) : DataItem() {
        override val id = asteroid.id
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}
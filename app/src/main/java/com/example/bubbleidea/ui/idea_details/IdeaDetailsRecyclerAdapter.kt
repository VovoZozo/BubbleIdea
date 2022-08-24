package com.example.bubbleidea.ui.idea_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bubbleidea.database.entites.AssociativeIdea
import com.example.bubbleidea.databinding.OwnIdeaCardViewBinding

class IdeaDetailsRecyclerAdapter (
    private val layoutInflater: LayoutInflater
) : ListAdapter<AssociativeIdea, IdeaDetailsRecyclerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = OwnIdeaCardViewBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(associativeIdeaList: List<AssociativeIdea>) {
        submitList(associativeIdeaList.toMutableList())
    }

    inner class MyViewHolder(private val binding: OwnIdeaCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(associativeIdea: AssociativeIdea) {
            binding.apply {
                previewOwnIdea.text = associativeIdea.associativeIdea
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<AssociativeIdea> = object : DiffUtil.ItemCallback<AssociativeIdea>() {
            override fun areItemsTheSame(oldItem: AssociativeIdea, newItem: AssociativeIdea): Boolean {
                return oldItem.associativeIdeaId == newItem.associativeIdeaId
            }

            override fun areContentsTheSame(oldItem: AssociativeIdea, newItem: AssociativeIdea): Boolean {
                return oldItem == newItem
            }
        }
    }
}
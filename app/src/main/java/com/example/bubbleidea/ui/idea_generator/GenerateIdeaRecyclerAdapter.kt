package com.example.bubbleidea.ui.idea_generator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bubbleidea.database.entites.AssociativeIdea
import com.example.bubbleidea.databinding.GenerateIdeaCardViewBinding

class GenerateIdeaRecyclerAdapter (
    private val layoutInflater: LayoutInflater,
    private val clickListener: SaveIdeaClickListener
) : ListAdapter<AssociativeIdea, GenerateIdeaRecyclerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = GenerateIdeaCardViewBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(associativeIdeaList: List<AssociativeIdea>) {
        submitList(associativeIdeaList.toMutableList())
    }

    inner class MyViewHolder(private val binding: GenerateIdeaCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.saveButton.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    val associativeIdea = getItem(absoluteAdapterPosition)
                    clickListener.onSaveIdeaClicked(associativeIdea)
                }
            }
        }

        fun bind(associativeIdea: AssociativeIdea) {
            binding.apply {
                ideaText.text = associativeIdea.associativeIdea
            }
        }
    }

    interface SaveIdeaClickListener {
        fun onSaveIdeaClicked(associativeIdea: AssociativeIdea)
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
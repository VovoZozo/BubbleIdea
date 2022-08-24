package com.example.bubbleidea.ui.associations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bubbleidea.database.entites.Association
import com.example.bubbleidea.databinding.WordCardViewBinding

class AssociationsRecyclerAdapter(
    private val layoutInflater: LayoutInflater
) : ListAdapter<Association, AssociationsRecyclerAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = WordCardViewBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(associationsList: List<Association>) {
        submitList(associationsList.toMutableList())
    }

    inner class MyViewHolder(private val binding: WordCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(association: Association) {
            binding.apply {
                wordText.text = association.association
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Association> = object : DiffUtil.ItemCallback<Association>() {
            override fun areItemsTheSame(oldItem: Association, newItem: Association): Boolean {
                return oldItem.associationId == newItem.associationId
            }
            override fun areContentsTheSame(oldItem: Association, newItem: Association): Boolean {
                return oldItem == newItem
            }
        }
    }
}
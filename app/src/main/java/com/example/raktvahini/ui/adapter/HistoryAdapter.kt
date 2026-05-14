package com.example.raktvahini.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.raktvahini.data.entity.DonationRecord
import com.example.raktvahini.databinding.ItemDonationHistoryBinding

class HistoryAdapter : ListAdapter<DonationRecord, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemDonationHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(private val binding: ItemDonationHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(record: DonationRecord) {
            binding.tvDate.text = record.date
            binding.tvLocation.text = record.location
            binding.tvBloodGroup.text = record.bloodGroup
        }
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<DonationRecord>() {
        override fun areItemsTheSame(oldItem: DonationRecord, newItem: DonationRecord): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: DonationRecord, newItem: DonationRecord): Boolean = oldItem == newItem
    }
}

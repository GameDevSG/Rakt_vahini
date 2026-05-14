package com.example.raktvahini.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.raktvahini.data.entity.User
import com.example.raktvahini.databinding.DonorItemBinding
import com.example.raktvahini.utils.DonorEligibilityFilter

/**
 * DonorAdapter: Now uses the Unified User table to display results.
 */
class DonorAdapter(private val onCallClick: (User) -> Unit) :
    ListAdapter<User, DonorAdapter.DonorViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DonorItemBinding.inflate(layoutInflater, parent, false)
        return DonorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonorViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DonorViewHolder(private val binding: DonorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.tvDonorName.text = user.name
            binding.tvBloodGroup.text = user.bloodGroup
            binding.tvLocation.text = user.location
            
            binding.tvLastDonated.text = if (user.lastDonationDate.isNotEmpty()) {
                "Last donated: ${user.lastDonationDate}"
            } else {
                "No previous donation"
            }
            
            // Check eligibility using central logic
            val isEligible = DonorEligibilityFilter.isEligible(user.lastDonationDate, user.isReadyToDonate)
            binding.tvStatusBadge.visibility = if (isEligible) View.VISIBLE else View.GONE

            binding.btnCall.setOnClickListener {
                onCallClick(user)
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
    }
}

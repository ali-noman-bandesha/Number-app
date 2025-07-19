package com.devsync.numberfinder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.SimCountriesRecyclerItemBinding
import com.devsync.numberfinder.remote.models.CountryDetails
import com.devsync.numberfinder.ui.interfaces.BlockedNumClickListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SimCountryAdapter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val list : List<CountryDetails>,
    private val listener: BlockedNumClickListener
)
    : RecyclerView.Adapter<SimCountryAdapter.Holder> ()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.sim_countries_recycler_item, parent, false)
        return SimCountryAdapter.Holder(itemView)
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list.get(position)
        holder.binding.simCountryName.text = item.name
        holder.binding.simCountryCard.setOnClickListener {
            listener.onDataReceived(item)
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }



    class Holder (itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val binding = SimCountriesRecyclerItemBinding.bind(itemView)

    }
}
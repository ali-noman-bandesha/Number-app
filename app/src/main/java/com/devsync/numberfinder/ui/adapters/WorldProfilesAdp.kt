package com.devsync.numberfinder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.CountriesRecyclerItemDesignBinding
import com.devsync.numberfinder.remote.models.CountriesInfo
import com.devsync.numberfinder.ui.interfaces.CountryClickListener

class WorldProfilesAdp
    (
    private val context: Context,
    private val list : List<CountriesInfo>,
    private val listener: CountryClickListener,
)
    : RecyclerView.Adapter<WorldProfilesAdp.ViewHolder> ()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.countries_recycler_item_design,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=list.get(position)
        Glide.with(context).load(item.flags.png).into(holder.binding.countryFlag)

        holder.binding.countryNameTV.text= item.name.common
        holder.binding.ItemCard.setOnClickListener {
            listener.onDataReceived(item)
        }

    }

    override fun getItemCount(): Int
    {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val binding= CountriesRecyclerItemDesignBinding.bind(itemView)
    }


}
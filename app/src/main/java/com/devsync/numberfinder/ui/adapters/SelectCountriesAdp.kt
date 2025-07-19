package com.devsync.numberfinder.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devsync.numberfinder.R
import com.devsync.numberfinder.databinding.SelectCountriesRecyclerItemDesignBinding
import com.devsync.numberfinder.remote.models.CountriesInfo
import com.devsync.numberfinder.ui.interfaces.CountryClickListener


class SelectCountriesAdp(
    private val context: Context,
    private val list : List<CountriesInfo>,
    private val listener: CountryClickListener
)
    : RecyclerView.Adapter<SelectCountriesAdp.ViewHolder> ()
{
    var code :String = "+"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.select_countries_recycler_item_design,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item=list.get(position)
        Glide.with(context).load(item.flags.png).into(holder.binding.selectCountryFlag)
        holder.binding.selectCountryNameTV.text= item.name.common
        try {
            code = item.idd.root+item.idd.suffixes[0]
        }
        catch (e:Exception)
        {}
        holder.binding.countryCodeTV.text= code

        holder.binding.selectCountryCard.setOnClickListener {
            listener.onDataReceived(item)
        }
    }

    override fun getItemCount(): Int
    {
        return list.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val binding = SelectCountriesRecyclerItemDesignBinding.bind(itemView)
    }
}
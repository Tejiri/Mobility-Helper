package com.example.mobilityhelper.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilityhelper.MapActivity
import com.example.mobilityhelper.R
import com.example.mobilityhelper.databinding.RvTaxisBinding
import com.example.mobilityhelper.models.Taxi

class TaxiAdapter(val taxis: ArrayList<Taxi>) : RecyclerView.Adapter<TaxiAdapter.ViewHolder>() {
    private lateinit var binding: RvTaxisBinding

    inner class ViewHolder(binding: RvTaxisBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.ivPhone.setOnClickListener {
                var position: Int = adapterPosition
                var taxi: Taxi = taxis[position]

                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:" + taxi.number)
                itemView.context.startActivity(callIntent)
            }

            binding.tvTaxiAddress.setOnClickListener {

                var position: Int = adapterPosition
                var taxi: Taxi = taxis[position]

                val mapIntent = Intent(itemView.context, MapActivity::class.java)
                mapIntent.putExtra("latitude", taxi.latitude)
                mapIntent.putExtra("longitude", taxi.longitude)
                mapIntent.putExtra("title", taxi.name)
                itemView.context.startActivity(mapIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaxiAdapter.ViewHolder {
        binding = RvTaxisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaxiAdapter.ViewHolder, position: Int) {
        val taxi = taxis[position]
        binding.tvTaxiName.text = taxi.name
        binding.tvTaxiAddress.text = taxi.address
        binding.tvTaxiNumber.text = taxi.number

    }

    override fun getItemCount() = taxis.size

}
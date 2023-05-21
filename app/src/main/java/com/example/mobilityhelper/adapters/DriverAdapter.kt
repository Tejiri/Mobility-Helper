package com.example.mobilityhelper.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilityhelper.MapActivity
import com.example.mobilityhelper.databinding.RvDriversBinding
import com.example.mobilityhelper.databinding.RvTaxisBinding
import com.example.mobilityhelper.models.Taxi
import com.example.mobilityhelper.models.User

class DriverAdapter(val drivers: ArrayList<User>) :
    RecyclerView.Adapter<DriverAdapter.ViewHolder>() {
    private lateinit var binding: RvDriversBinding

    inner class ViewHolder(binding: RvDriversBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.cvDriver.setOnClickListener {
                var position: Int = adapterPosition
                var driver: User = drivers[position]

                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:" + driver.phoneNumber)
                itemView.context.startActivity(callIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DriverAdapter.ViewHolder {
        binding = RvDriversBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DriverAdapter.ViewHolder, position: Int) {
        val driver = drivers[position]
        val driverFullName = driver.firstName + " " + driver.lastName
        val driverPhoneNumber = driver.phoneNumber
        if (driverFullName.length > 14) {

            binding.tvDriverName.text = driverFullName.substring(0, 10) + "....."
        } else {
            binding.tvDriverName.text = driverFullName
        }

        if (driverPhoneNumber.length > 11) {
            binding.tvDriverPhoneNumber.text = driverPhoneNumber.substring(0, 8) + "....."

        } else {
            binding.tvDriverPhoneNumber.text = driverPhoneNumber

        }

    }

    override fun getItemCount() = drivers.size

}
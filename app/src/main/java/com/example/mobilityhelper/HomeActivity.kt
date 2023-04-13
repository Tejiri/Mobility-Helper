package com.example.mobilityhelper

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilityhelper.adapters.DriverAdapter
import com.example.mobilityhelper.adapters.TaxiAdapter
import com.example.mobilityhelper.databinding.ActivityHomeBinding
import com.example.mobilityhelper.databinding.ActivityLoginBinding
import com.example.mobilityhelper.databinding.HomeBottomSheetDialogBinding
import com.example.mobilityhelper.databinding.RvTaxisBinding
import com.example.mobilityhelper.models.Taxi
import com.example.mobilityhelper.models.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.json.JSONArray

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomSheetBinding: HomeBottomSheetDialogBinding

    lateinit var batteryManager: BatteryManager
    lateinit var connectivityManager: ConnectivityManager

    var batteryLevel: Int? = null
    var capabilities: NetworkCapabilities? = null
    var sharedPreference: SharedPreferences? = null

    var networkMessage: String? = null

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        auth = Firebase.auth
        sharedPreference = getSharedPreferences(
            resources.getString(R.string.sharedPreferencesName), Context.MODE_PRIVATE
        )

        setTaxisRecyclerView()
        setDriversRecyclerView()

    }

    fun setDriversRecyclerView() {
        var driverArray = ArrayList<User>()
        db.collection(resources.getString(R.string.usersCollectionName)).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val user = User(document.data as Map<String, String>)
                    driverArray.add(user)
                }

                binding.pbHomeDriversLoader.visibility = View.GONE
                binding.rvDrivers.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.rvDrivers.adapter = DriverAdapter(driverArray)
            }.addOnFailureListener { exception ->
                binding.pbHomeDriversLoader.visibility = View.GONE
                println("Error getting documents.${exception.message}")
            }
    }

    fun setTaxisRecyclerView() {
        var taxiArray = ArrayList<Taxi>()

        val fileInString: String =
            applicationContext.assets.open("taxis.json").bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(fileInString)
        for (i in 0 until jsonArray.length()) {
            val taxi = Taxi(jsonArray.getJSONObject(i))
            taxiArray.add(taxi)
        }

        binding.rvStokeTaxis.layoutManager = LinearLayoutManager(this)
        binding.rvStokeTaxis.adapter = TaxiAdapter(taxiArray)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true

    }

    private fun checkNetWorkAndBaterryLevel(): Boolean {
        batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                networkMessage = "CELLULAR NETWORK"
                return true
            } else if (capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                networkMessage = "WIFI NETWORK"

                return true
            } else if (capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                networkMessage = "ETHERNET NETWORK"
                return true
            }

        }
        networkMessage = "NO INTERNET AVAILABLE"
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menuItemHomeDark -> {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            }
            R.id.menuItemHomeLight -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                true
            }

            R.id.menuItemHomePhone -> {
                val goToontactsPage = Intent(this, ContactsActivity::class.java)
                startActivity(goToontactsPage)
                true
            }
            R.id.menuItemHomeLogout -> {
                auth.signOut()
                val goToLoginPageIntent = Intent(this, LoginActivity::class.java)
                startActivity(goToLoginPageIntent)
                finish()
                true
            }
            R.id.menuItemHomeSettings -> {
                checkNetWorkAndBaterryLevel()
                val dialog = BottomSheetDialog(this)
                bottomSheetBinding = HomeBottomSheetDialogBinding.inflate(layoutInflater)

                ObjectAnimator.ofInt(
                    bottomSheetBinding.pbHomeBottomSheet, "progress", batteryLevel!!
                ).setDuration(2500).start()

                bottomSheetBinding.tvHomeBottomSheetEmail.text =
                    sharedPreference!!.getString("email", "email")
                bottomSheetBinding.tvHomeBottomSheetName.text = sharedPreference!!.getString(
                    "firstName", "name"
                ) + " " + sharedPreference!!.getString("lastName", "name")
                bottomSheetBinding.tvHomeBottomSheetPhoneNumber.text =
                    sharedPreference!!.getString("phoneNumber", "number")
                bottomSheetBinding.tvHomeBottomSheetGender.text =
                    sharedPreference!!.getString("gender", "gender")
                bottomSheetBinding.tvHomeBottomSheetWifiStatus.text = networkMessage

                dialog.setContentView(bottomSheetBinding.root)
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
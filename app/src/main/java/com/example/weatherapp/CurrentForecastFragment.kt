package com.example.weatherapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CurrentForecastFragment : Fragment() {
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val forecastRepository: ForecastRepository = ForecastRepository()
    private lateinit var appNavigator: AppNavigator

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is AppNavigator)
            appNavigator = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_current_forecast, container, false)

        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        val forecastList: RecyclerView = view.findViewById(R.id.forecastList)
        forecastList.layoutManager = LinearLayoutManager(requireContext())

        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager) {
            showForecastDetails(it)
        }
        forecastList.adapter = dailyForecastAdapter

        forecastRepository.weeklyForecast.observe(this, Observer { forecastItems ->
            dailyForecastAdapter.submitList(forecastItems)
        })

        val locationEntryButton = view.findViewById<FloatingActionButton>(R.id.locationEntryButton)
        locationEntryButton.setOnClickListener {
            appNavigator.navigateToLocationEntry()
        }

        forecastRepository.loadForecast(arguments!!.getString(KEY_ZIPCODE, ""))

        return view
    }

    private fun showForecastDetails(dailyForecast: DailyForecast) {
        val forecastDetailsIntent = Intent(requireContext(), ForecastDetailsActivity::class.java)
        forecastDetailsIntent.putExtra("key_temp", dailyForecast.temperature)
        forecastDetailsIntent.putExtra("key_description",dailyForecast .description)
        startActivity(forecastDetailsIntent )
    }

    companion object {
        const val KEY_ZIPCODE = "key_zipcode"

        fun newInstance(zipcode: String) : CurrentForecastFragment{
            val currentForecastFragment = CurrentForecastFragment()

            val args = Bundle()
            args.putString(KEY_ZIPCODE, zipcode)

            currentForecastFragment.arguments = args

            return currentForecastFragment
        }
    }

}
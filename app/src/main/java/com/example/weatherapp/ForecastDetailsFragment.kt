package com.example.weatherapp

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class ForecastDetailsFragment : Fragment() {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private val args: ForecastDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view  = inflater.inflate(R.layout.fragment_forecast_details, container, false)


        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        val tempText: TextView = view.findViewById(R.id.temperatureText)
        val descText: TextView = view.findViewById(R.id.descriptionText)

        tempText.text = formatTemperature(args.temp, tempDisplaySettingManager.getDisplaySetting())
        descText.text = args.desc

        return view
    }
}
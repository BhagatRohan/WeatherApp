package com.example.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.weatherapp.databinding.FragmentForecastDetailsBinding
import com.example.weatherapp.models.ForecastDetailViewState
import com.example.weatherapp.utils.TempDisplaySettingManager
import com.example.weatherapp.utils.formatTemperature
import com.example.weatherapp.viewModel.ForecastDetailViewModel
import com.example.weatherapp.viewModel.ForecastDetailsViewModelFactory


class ForecastDetailsFragment : Fragment() {

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager

    private var _binding: FragmentForecastDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory
    private val viewModel: ForecastDetailViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    private val args: ForecastDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)

        viewModelFactory =
            ForecastDetailsViewModelFactory(
                args
            )

        tempDisplaySettingManager =
            TempDisplaySettingManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewStateObserver = Observer<ForecastDetailViewState> { viewState ->

            binding.temperatureText.text =
                formatTemperature(
                    viewState.temp,
                    tempDisplaySettingManager.getDisplaySetting()
                )
            binding.descriptionText.text = viewState.description
            binding.dateText.text = viewState.date
            binding.forecastIcon.load(viewState.icons)

        }

        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
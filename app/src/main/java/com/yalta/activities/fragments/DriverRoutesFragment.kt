package com.yalta.activities.fragments

import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.datepicker.MaterialDatePicker
import com.yalta.R
import com.yalta.databinding.FragmentDriverRoutesBinding
import com.yalta.di.YaltaApplication
import com.yalta.viewmodel.DriverRoutesViewModel
import kotlinx.android.synthetic.main.fragment_driver_routes.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import javax.inject.Inject

class DriverRoutesFragment : Fragment() {
    @Inject
    lateinit var viewModel: DriverRoutesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDriverRoutesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_driver_routes, container, false)

        YaltaApplication.appComponent.inject(this)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.routeRecyclerView.addItemDecoration(DividerItemDecoration(context, ClipDrawable.HORIZONTAL))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectRangeButton.setOnClickListener {
            viewModel.showDatesError(false)
            val timeZone = DateTimeZone.forID("Europe/Moscow")
            val picker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setTheme(R.style.DatePickerStyle)
                .build()
            picker.show(requireFragmentManager(), picker.toString())

            picker.addOnPositiveButtonClickListener {
                viewModel.fromDate.value = DateTime(it.first, timeZone)
                viewModel.toDate.value = DateTime(it.second, timeZone)
                viewModel.getSomeRoutes()
            }
        }

        viewModel.routesNames.observeForever { names ->
            routesSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, names)
        }

        routesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selectedRouteChanged(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.selectedRouteChanged(null)
            }
        }
    }
}

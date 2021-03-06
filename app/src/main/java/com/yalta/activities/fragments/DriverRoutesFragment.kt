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
import com.yalta.viewModel.DriverRoutesViewModel
import common.Route
import kotlinx.android.synthetic.main.fragment_driver_routes.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
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
            val timeZone = DateTimeZone.forID("Europe/Moscow")
            val picker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setTheme(R.style.DatePickerStyle)
                .build()
            picker.show(requireActivity().supportFragmentManager, picker.toString())

            picker.addOnPositiveButtonClickListener {
                viewModel.fromDate.value = DateTime(it.first, timeZone)
                viewModel.toDate.value = DateTime(it.second, timeZone)
                viewModel.getSomeRoutes()
            }
        }

        viewModel.storage.routes.observeForever { routes ->
            routesSpinner.adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    routes.map(::getRouteSpinnerDescription)
                )
            val selection = viewModel.selectedRoute
            if (selection != null && selection < viewModel.storage.routes.value?.size!!) {
                routesSpinner.setSelection(selection)
            }
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

    private fun getRouteSpinnerDescription(route: Route): String {
        val builder = StringBuilder()
            .append("ID: ${route.id} ")
            .append("Date: ${route.routeDate.toString(DateTimeFormat.forPattern("dd-MM-yy"))}")
        if (viewModel.isRouteDateNow(route)) {
            builder.append(" [✓] ")
        }
        return builder.toString()
    }
}

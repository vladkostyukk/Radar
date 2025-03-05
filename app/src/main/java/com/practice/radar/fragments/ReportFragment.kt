package com.practice.radar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practice.radar.MainActivity
import com.practice.radar.R
import com.practice.radar.adapters.RecordNoteAdapter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportFragment : Fragment() {
    private lateinit var tvDateRange: TextView
    private lateinit var rvNodes: RecyclerView
    private lateinit var cbSelectAll: CheckBox
    private lateinit var noteAdapter: RecordNoteAdapter

    private val items = mutableListOf("Узел 1", "Узел 2", "Узел 3", "Узел 4")
    private val selectedItems = mutableSetOf<Int>()
    private val isCheckedList = MutableList(items.size) { false }

    override fun onResume() {
        super.onResume()
        (requireActivity() as? MainActivity)?.updateToolbarTitle(R.string.report)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        tvDateRange = view.findViewById(R.id.tvDateRange)
        tvDateRange.text = "Не выбрано"

        val btnSelectDateRange = view.findViewById<Button>(R.id.btnSelectDateRange)
        btnSelectDateRange.setOnClickListener { showDateRangePicker() }

        noteAdapter = RecordNoteAdapter(items, isCheckedList) { position, isChecked ->
            if (isChecked) {
                selectedItems.add(position)
            } else {
                selectedItems.remove(position)
            }
            isCheckedList[position] = isChecked
            updateSelectAllCheckbox()
        }

        rvNodes = view.findViewById(R.id.rvItems)
        rvNodes.layoutManager = LinearLayoutManager(requireContext())
        rvNodes.adapter = noteAdapter

        cbSelectAll = view.findViewById(R.id.cbSelectAll)
        cbSelectAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectAllItems()
            } else {
                deselectAllItems()
            }
        }

        return view
    }

    private fun showDateRangePicker() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(getString(R.string.select_dates))
                .setTheme(R.style.MaterialCalendar)
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first ?: return@addOnPositiveButtonClickListener
            val endDate = selection.second ?: return@addOnPositiveButtonClickListener

            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val startDateString = dateFormat.format(Date(startDate))
            val endDateString = dateFormat.format(Date(endDate))

            tvDateRange.text = getString(R.string.date_range_format, startDateString, endDateString)
        }

        dateRangePicker.show(childFragmentManager, "DATE_PICKER")
    }

    private fun selectAllItems() {
        selectedItems.clear()
        selectedItems.addAll(0 until items.size)
        isCheckedList.replaceAll { true }
        noteAdapter.notifyDataSetChanged()
    }

    private fun deselectAllItems() {
        selectedItems.clear()
        isCheckedList.replaceAll { false }
        noteAdapter.notifyDataSetChanged()
    }

    private fun updateSelectAllCheckbox() {
        cbSelectAll.isChecked = selectedItems.size == items.size
    }
}
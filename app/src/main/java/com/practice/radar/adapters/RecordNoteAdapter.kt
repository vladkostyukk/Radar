package com.practice.radar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.practice.radar.R

class RecordNoteAdapter(
    private var items: List<String>,
    private val isCheckedList: List<Boolean>,
    private val onCheckedChangeListener: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<RecordNoteAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.cbItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_record_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBox.text = items[position]
        holder.checkBox.isChecked = isCheckedList[position]

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckedChangeListener(position, isChecked)
        }
    }

    override fun getItemCount(): Int = items.size

}
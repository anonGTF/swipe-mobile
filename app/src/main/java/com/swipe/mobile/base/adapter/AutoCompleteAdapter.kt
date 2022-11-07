package com.swipe.mobile.base.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.swipe.mobile.R
import com.swipe.mobile.utils.inflate

class AutoCompleteAdapter<T : AutoCompleteItem> : BaseAdapter(), Filterable {
    private var _items = mutableListOf<T>()
    private var items: List<T> = _items
    private val filter = mutableListOf<T>()

    fun addAll(list: List<T>) {
        _items.addAll(list)
        notifyDataSetChanged()
    }

    fun update(list: List<T>) {
        _items.clear()
        _items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getCount() = _items.size

    override fun getItem(position: Int): T {
        return _items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0L
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView as? ConstraintLayout
            ?: parent.inflate(R.layout.item_auto_complete) as ConstraintLayout

        val text = view.findViewById(R.id.tvItem) as AppCompatTextView
        val separator = view.findViewById(R.id.separator) as View

        text.text = _items[position].displayText

        if (position == _items.size.minus(1)) {
            separator.visibility = View.GONE
        } else {
            separator.visibility = View.VISIBLE
        }

        return view
    }

    override fun getFilter(): Filter {
        return NoFilter()
    }

    inner class NoFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val result = FilterResults()
            if (constraint != null) {
                filter.clear()
                for (itm : T in items) {
                    if (itm.displayText!!.contains(constraint)) {
                        filter.add(itm)
                    }
                }
                result.count = filter.size
                result.values = filter
            }
            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val filteredList = (results!!.values?:mutableListOf<T>()) as List<T>
            _items = items.toMutableList()
            if (results.count > 0) {
                _items.clear()
                for (itm in filteredList) {
                    _items.add(itm)
                }
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}

interface AutoCompleteItem {
    val displayText: String?
}
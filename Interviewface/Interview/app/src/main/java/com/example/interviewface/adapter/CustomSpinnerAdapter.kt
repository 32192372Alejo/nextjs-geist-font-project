package com.example.interviewface.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.interviewface.R

/**
 * Adaptador personalizado para los spinners que muestra un indicador en la opción seleccionada
 */
class CustomSpinnerAdapter(
    context: Context,
    resource: Int,
    private val items: Array<String>
) : ArrayAdapter<String>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item_improved, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = items[position]
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_dropdown_item_improved, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = items[position]
        
        // Mostrar el indicador solo si no es la primera posición (hint)
        val indicator = view.findViewById<View>(R.id.selectionIndicator)
        indicator.visibility = if (position > 0) View.VISIBLE else View.INVISIBLE
        
        return view
    }
}

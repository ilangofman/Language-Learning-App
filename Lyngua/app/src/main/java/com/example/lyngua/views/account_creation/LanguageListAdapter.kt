package com.example.lyngua.views.account

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lyngua.R
import com.google.cloud.translate.Language
import kotlinx.android.synthetic.main.custom_language_view.view.*

class LanguageListAdapter(
    private var languageList: List<Language>,
    private var selectedPos: Int = RecyclerView.NO_POSITION,
    private var callback: (language: Language) -> Unit
): RecyclerView.Adapter<LanguageListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.custom_language_view,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLanguage = languageList[position]

        holder.itemView.textView_language.text = currentLanguage.name
        holder.itemView.isSelected = (selectedPos == position)

        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedPos)
            selectedPos = position
            notifyItemChanged(selectedPos)

            callback(currentLanguage)
        }
    }

    override fun getItemCount(): Int {
        return languageList.size
    }
}
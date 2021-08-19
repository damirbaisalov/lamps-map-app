package kz.bfgroup.formmap.requests_all.view

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.formmap.R
import kz.bfgroup.formmap.requests_all.models.RequestApiData

class RequestViewHolder(
    itemView: View,
    private val requestClickListener: RequestClickListener
): RecyclerView.ViewHolder(itemView) {

    private val requestText: TextView = itemView.findViewById(R.id.request_item_text)
    private val requestDate: TextView = itemView.findViewById(R.id.request_item_create_date)
    private val requestDone: Button = itemView.findViewById(R.id.request_item_done)

    fun onBind(requestApiData: RequestApiData) {
        requestText.text = requestApiData.text
        requestDate.text = requestApiData.date
        requestDone.setOnClickListener {
            requestClickListener.onClick(requestApiData.id)
        }
    }
}
package kz.bfgroup.formmap.requests_all.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.formmap.R
import kz.bfgroup.formmap.requests_all.models.RequestApiData

class RequestAdapter(
    private val requestClickListener: RequestClickListener
): RecyclerView.Adapter<RequestViewHolder>() {

    private var dataList: MutableList<RequestApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.request_item, parent, false)

        return RequestViewHolder(
            rootView ,
            requestClickListener
        )
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun setList(requestApiDataList: List<RequestApiData>) {
        dataList.clear()
        dataList.addAll(requestApiDataList)
        notifyDataSetChanged()
    }

    fun clearAll() {
        (dataList as? ArrayList<RequestApiData>)?.clear()
        notifyDataSetChanged()
    }
}
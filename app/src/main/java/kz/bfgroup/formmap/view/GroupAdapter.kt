package kz.bfgroup.formmap.view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.formmap.R
import kz.bfgroup.formmap.models.GroupApiData

class GroupAdapter: RecyclerView.Adapter<GroupViewHolder>() {

    private var dataList: MutableList<GroupApiData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(View.inflate(parent.context, R.layout.group_item, null))
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun setList(groupApiDataList: List<GroupApiData>) {
        dataList.clear()
        dataList.addAll(groupApiDataList)
        notifyDataSetChanged()
    }
}
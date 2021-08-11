package kz.bfgroup.formmap.view

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.bfgroup.formmap.R
import kz.bfgroup.formmap.models.GroupApiData

class GroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val itemLayout: LinearLayout = itemView.findViewById(R.id.item_linear_layout)
    private val groupName: TextView = itemView.findViewById(R.id.item_group_name)

    fun onBind(groupApiData: GroupApiData) {
        groupName.text = groupApiData.name
    }
}
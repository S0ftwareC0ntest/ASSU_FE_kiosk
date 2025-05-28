package com.example.assu_fe_kiosk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_kiosk.databinding.ItemMenuTypesBinding

class MenuAdapter(
    private val menuList: List<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    var selectedIndex = 0
        private set

    fun setSelectedIndex(index: Int){
        val prev = selectedIndex
        selectedIndex = index
        notifyItemChanged(prev)
        notifyItemChanged(selectedIndex)
    }

    inner class MenuViewHolder(val binding: ItemMenuTypesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuTypesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = menuList[position]
        holder.binding.btnNavigatorMenuTypeSelect.text = item
        holder.binding.btnNavigatorMenuTypeSelect.isSelected = position == selectedIndex

        val pos = position
        holder.binding.btnNavigatorMenuTypeSelect.setOnClickListener {
            if (pos != selectedIndex) {
                val prev = selectedIndex
                selectedIndex = pos
                notifyItemChanged(prev)
                notifyItemChanged(pos)
                onItemClick(pos)
            }
        }
    }

    override fun getItemCount(): Int = menuList.size
}

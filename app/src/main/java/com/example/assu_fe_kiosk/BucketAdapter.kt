package com.example.assu_fe_kiosk

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.example.assu_fe_kiosk.databinding.ItemBucketBinding

class BucketAdapter (
    private val itemList: MutableList<BucketItem>,
    private val onCountChanged: () -> Unit
): RecyclerView.Adapter<BucketAdapter.MenuViewHolder>(){

    inner class MenuViewHolder(val binding: ItemBucketBinding):
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBucketBinding.inflate(inflater, parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = itemList[position]

        holder.binding.tvSideBarBucketItemName.text = item.name
        holder.binding.tvSideBarBucketItemCnt.text = item.count.toString()
        holder.binding.tvSideBarBucketItemPrice.text = "%,d원".format(item.price * item.count)

        holder.binding.ivSideBarBucketItemPlus.setOnClickListener{
            item.count++
            notifyItemChanged(position)
            onCountChanged()
        }

        holder.binding.ivSideBarBucketItemMinus.setOnClickListener{
            if(item.count > 1){
                item.count--
                notifyItemChanged(position)
                onCountChanged()
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

}
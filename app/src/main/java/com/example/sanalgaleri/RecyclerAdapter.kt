package com.example.sanalgaleri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sanalgaleri.Model.ItemMenuModel

class RecyclerAdapter(
    private val liste: ArrayList<ItemMenuModel>
    ) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    //item click listener
    private lateinit var mListener: onItemCLickListener

    interface onItemCLickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemCLickListener(listener: onItemCLickListener){
        mListener = listener
    }

    inner class ViewHolder(itemView: View , listener: onItemCLickListener): RecyclerView.ViewHolder(itemView){

        val image: ImageView = itemView.findViewById(R.id.item_image)
        val title: TextView = itemView.findViewById(R.id.item_title)
        val detail: TextView = itemView.findViewById(R.id.item_detail)


        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemview = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_card,parent,false)
        return ViewHolder(itemview,mListener)
    }

    override fun getItemCount(): Int {
        return liste.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {

        val ItemMenu = liste[position]
        holder.title.text = ItemMenu.title
        holder.detail.text = ItemMenu.detail


        Glide.with(holder.itemView)
            .load(ItemMenu.image)
            .into(holder.image)

    }
}
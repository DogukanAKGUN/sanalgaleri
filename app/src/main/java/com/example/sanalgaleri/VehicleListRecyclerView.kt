package com.example.sanalgaleri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sanalgaleri.Model.VehicleList
import com.example.sanalgaleri.Model.suvBrandModel
import kotlinx.android.synthetic.main.activity_carousel.view.*
import kotlinx.android.synthetic.main.vehicle_list_card.view.*

class VehicleListRecyclerAdapter (
    private val VehicleList: ArrayList<VehicleList>
): RecyclerView.Adapter<VehicleListRecyclerAdapter.ViewHolder>() {

    private lateinit var mListener: onItemCLickListener

    interface onItemCLickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemCLickListener(listener: onItemCLickListener){
        mListener = listener
    }
    inner class ViewHolder(itemView: View, listener: onItemCLickListener): RecyclerView.ViewHolder(itemView){

        /*val image: ImageView = itemView.findViewById(R.id.)
        val title: TextView = itemView.findViewById(R.id.)
        val price: TextView = itemView.findViewById(R.id.)*/

        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.vehicle_list_card,parent,false)
        return ViewHolder(v,mListener)
    }

    override fun getItemCount(): Int {
        return VehicleList.size
    }

    override fun onBindViewHolder(holder: VehicleListRecyclerAdapter.ViewHolder, position: Int) {


        val VehicleListe = VehicleList[position]
        holder.itemView.title.text = VehicleListe.title
        holder.itemView.price.text = VehicleListe.price

        /*Glide.with(holder.itemView)
            .load(VehicleListe.image)
            .into(holder.itemView.image)*/

    }


}

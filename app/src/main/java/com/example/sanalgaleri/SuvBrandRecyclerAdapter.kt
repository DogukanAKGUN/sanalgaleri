package com.example.sanalgaleri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sanalgaleri.Model.suvBrandModel

class SuvBrandRecyclerAdapter  (
    val suvBrandListe: ArrayList<suvBrandModel>
): RecyclerView.Adapter<SuvBrandRecyclerAdapter.ViewHolder>() {

    //private var titles = arrayOf("Otomobil","Elektrikli Otomobil","SUV","Arazi Araçları","Motorsiklet")
    //private var images = intArrayOf(R.drawable.otomobil , R.drawable.elektrikli_araba,R.drawable.suv, R.drawable.arazi_araci,R.drawable.motorsiklet)
    //private var detail = arrayOf("Gündelik kullanım için binek araçlar","Gündelik kullanım için elektrikli araçlar","Gündelik kullanım için SUV araçlar","Yollar dışarısında kullanmak için sınır tanımayan arazi araçları","Gündelik kullanım için motorsikler")

    private lateinit var mListener: onItemCLickListener

    interface onItemCLickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemCLickListener(listener: onItemCLickListener){
        mListener = listener
    }

    inner class ViewHolder(itemView: View , listener: onItemCLickListener): RecyclerView.ViewHolder(itemView){

        //val image: ImageView = itemView.findViewById(R.id.brand_image)
        val title: TextView = itemView.findViewById(R.id.brand_title)


        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
        /*
        val image : ImageView
        val title : TextView
        val detail : TextView

        init {
            image = itemView.findViewById(R.id.item_image)
            title = itemView.findViewById(R.id.item_title)
            detail = itemView.findViewById(R.id.item_detail)
        }
        */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.brand_card,parent,false)
        return ViewHolder(v,mListener)
    }

    override fun getItemCount(): Int {
        return suvBrandListe.size
    }

    override fun onBindViewHolder(holder: SuvBrandRecyclerAdapter.ViewHolder, position: Int) {

        val suvBrandModel = suvBrandListe[position]
        holder.title.text = suvBrandModel.brandName
        //holder.image.setImageResource(otomobilBrandModel.brandImage.toString())


        //val ItemMenuModel = liste[position]
        //holder.image.setImageResource(ItemMenuModel.image)
        //holder.title.text = ItemMenuModel.title
        //holder.detail.text = ItemMenuModel.detail

    }

    //override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    //   TODO("Not yet implemented")
    //}
}
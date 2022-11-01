package com.example.sanalgaleri

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_menu.view.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.coroutines.NonDisposableHandle.parent

class RecyclerAdapter(
    val liste: ArrayList<ItemMenuModel>
    ) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    //private var titles = arrayOf("Otomobil","Elektrikli Otomobil","SUV","Arazi Araçları","Motorsiklet")
    //private var images = intArrayOf(R.drawable.otomobil , R.drawable.elektrikli_araba,R.drawable.suv, R.drawable.arazi_araci,R.drawable.motorsiklet)
    //private var detail = arrayOf("Gündelik kullanım için binek araçlar","Gündelik kullanım için elektrikli araçlar","Gündelik kullanım için SUV araçlar","Yollar dışarısında kullanmak için sınır tanımayan arazi araçları","Gündelik kullanım için motorsikler")

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val title: TextView = itemView.findViewById(R.id.item_detail)
        val detail: TextView = itemView.findViewById(R.id.item_title)

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
            .inflate(R.layout.menu_card,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return liste.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {

        val ItemMenuModel = liste[position]
        //holder.image.setImageResource(ItemMenuModel.image)
        holder.title.text = ItemMenuModel.title
        holder.detail.text = ItemMenuModel.detail
        //holder.title.text = liste[position].title
       // holder.detail.text = liste[position].detail

        //holder.itemTitle.text = titles[position]
        //holder.itemDetail.text = detail[position]
        //holder.itemImage.setImageResource(images[position])
    }
}
package com.example.sanalgaleri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.sanalgaleri.Model.CarDetailModel
import com.example.sanalgaleri.Model.CarDetails
import com.smarteist.autoimageslider.SliderViewAdapter

// on below line we are creating a class for slider
// adapter and passing our array list to it.
class SliderAdapter(private val image: ArrayList<CarDetails>) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {


    var sliderList: ArrayList<CarDetails> = image
    val a = sliderList[0].images
    // on below line we are calling get method
    override fun getCount(): Int {
        return sliderList[0].images.size
    }

    // on below line we are calling on create view holder method.
    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapter.SliderViewHolder {
        // inside this method we are inflating our layout file for our slider view.
        val inflate: View =
            LayoutInflater.from(parent!!.context).inflate(R.layout.item_carousel,parent, false)

        // on below line we are simply passing
        // the view to our slider view holder.
        return SliderViewHolder(inflate)
    }

    // on below line we are calling on bind view holder method to set the data to our image view.
    override fun onBindViewHolder(viewHolder: SliderViewHolder, position: Int) {


        // on below line we are checking if the view holder is null or not.
        Glide.with(viewHolder.itemView).load(a[position]).fitCenter()
            .into(viewHolder.imageView)
    }

    // on below line we are creating a class for slider view holder.
    class SliderViewHolder(itemView: View) : ViewHolder(itemView) {

        // on below line we are creating a variable for our
        // image view and initializing it with image id.
        var imageView: ImageView = itemView.findViewById(R.id.myimage)
    }
}
/*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sanalgaleri.Model.CarDetails
import kotlinx.android.synthetic.main.item_carousel.view.*

class CarouselRecyclerAdapter (private val carDetails: ArrayList<CarDetails>) :
    RecyclerView.Adapter<CarouselRecyclerAdapter.CarouselItemViewHolder>() {

    class CarouselItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.carousel_image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int ) {
        val Item = carDetails[position]

        for( i in Item.images) {
            Glide.with(holder.itemView)
                .load(i)
                .into(holder.image)
        }
    }

    override fun getItemCount(): Int {
        return carDetails.size
    }

}
*/
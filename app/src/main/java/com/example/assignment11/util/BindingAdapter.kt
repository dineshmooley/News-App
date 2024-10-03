package com.example.assignment11.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

//Setting the icon image
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, iconId:String?)   {

    iconId?.let {
        val url = "https://openweathermap.org/img/wn/$iconId.png"
        Glide.with(imageView.context).load(url).into(imageView)
    }


}
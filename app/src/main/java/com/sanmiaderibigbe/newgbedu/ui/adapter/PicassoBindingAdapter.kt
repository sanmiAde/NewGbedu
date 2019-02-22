package com.sanmiaderibigbe.newgbedu.ui.adapter

import android.databinding.BindingAdapter

import android.widget.ImageView
import com.sanmiaderibigbe.newgbedu.R

import com.squareup.picasso.Picasso


@BindingAdapter("imageUrl")
 fun setImageUrl(view: ImageView, url: String) {
    Picasso.with(view.context)
        .load(url)
        .placeholder(R.drawable.ic_launcher_background)
        .into(view)
}



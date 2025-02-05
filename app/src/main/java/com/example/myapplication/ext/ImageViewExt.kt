package com.example.myapplication.ext

import android.widget.ImageView

fun ImageView.flipIfNeed(velocityX: Float) {
    if (velocityX > 0) {
        this.scaleX = -this.scaleX
    }
}

fun ImageView.setSizeImage(size: Int) {

}
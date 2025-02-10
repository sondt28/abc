package com.example.myapplication.ext

import android.widget.ImageView

fun ImageView.flipIfNeed(velocityX: Float) {
    if ((velocityX > 0 && this.scaleX > 0) || (velocityX < 0 && this.scaleX < 0)) {
        this.scaleX = -this.scaleX
    }
}
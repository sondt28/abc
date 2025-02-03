package com.example.myapplication.ext

import android.widget.ImageView

fun ImageView.flip() {
    this.scaleX = -this.scaleX
}
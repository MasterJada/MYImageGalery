package com.envionsoftware.upworktest.models

import android.graphics.Bitmap
import android.view.View

interface IPictureModel {
    fun getViewId(): Int
    fun fillItem(v: View)
    fun collSpan(): Int
}
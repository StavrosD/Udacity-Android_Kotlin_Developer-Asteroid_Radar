package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("name")
fun bindAsteroidsName(textView: TextView, item:Asteroid){
    textView.text = item.codename
}

@BindingAdapter("magnitude")
fun bindAsteroidsMagnitude(textView: TextView, item:Asteroid){
    textView.text = item.absoluteMagnitude.toString()
}
@BindingAdapter("showProgress")
fun bindProgressBar(progressBar : ProgressBar, status:Boolean){
  progressBar.visibility = if (status)  View.VISIBLE else  View.GONE
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, item: Asteroid) {
    if (item.isPotentiallyHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = "This asteroid is potentially hazardous!"
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = "This asteroid is safe!"
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

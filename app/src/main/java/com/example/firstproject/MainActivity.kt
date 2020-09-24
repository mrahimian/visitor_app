package com.example.firstproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import com.example.firstproject.StartActivity.Companion.brands
import com.example.firstproject.StartActivity.Companion.cars
import com.example.firstproject.StartActivity.Companion.parts
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        car_search.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean -> car_field.isEnabled = b }
        model_search.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean -> model_field.isEnabled = b }

        val carAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, cars.toArray())
        car_field.setAdapter(carAdapter)

        val partAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, parts.toArray())
        part.setAdapter(partAdapter)

        val brandAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, brands.toArray())
        model_field.setAdapter(brandAdapter)
    }

    public fun search(v : View){
        var part = part.text.toString()
        var car = car_field.text.toString()
        var model = model_field.text.toString()
        var intent : Intent = Intent(this, ResultList::class.java)
        intent.putExtra("part",part)
        intent.putExtra("car",car)
        intent.putExtra("model",model)
        intent.putExtra("carChecked",car_field.text.toString())
        intent.putExtra("modelChecked",model_field.text.toString())
        startActivity(intent)

    }


}
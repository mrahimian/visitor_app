package com.example.firstproject

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.firstproject.StartActivity.Companion.users
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var visibility = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun login(v : View){
        if (!users.containsKey(username.text.toString())){
            var toast = Toast.makeText(this,"نام کاربری صحیح نمی باشد",Toast.LENGTH_SHORT)
            toast.view.background = getDrawable(R.drawable.warning_toast)
            toast.show()
            return
        }
        if (!users.get(username.text.toString())?.equals(password.text.toString())!!){
            var toast = Toast.makeText(this,"نام کاربری و رمز عبور مطابقت ندارند",Toast.LENGTH_SHORT)
            toast.view.background = getDrawable(R.drawable.warning_toast)
            toast.show()
            return
        }
//        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

        startActivity(Intent(this , MainActivity::class.java))
    }

    public fun seen(v : View){
        if (v.id == R.id.not_see){
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            not_see.visibility = ImageView.GONE
            see.visibility = ImageView.VISIBLE
        }
        else{
            password.setTransformationMethod(PasswordTransformationMethod.getInstance())
            see.visibility = ImageView.GONE
            not_see.visibility = ImageView.VISIBLE
        }
    }
}
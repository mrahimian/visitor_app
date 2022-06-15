package com.example.firstproject

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_start.*
import java.security.AccessController.getContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class StartActivity : AppCompatActivity() {
    var frontFlag = false
    var backFlag = false

    companion object {
        val users: HashMap<String, String> = hashMapOf()
        val admins: HashMap<String, String> = hashMapOf()
        val stockInfo : ArrayList<SearchInfo> = arrayListOf()
        val parts : HashSet<String> = hashSetOf()
        val cars : HashSet<String> = hashSetOf()
        val brands : HashSet<String> = hashSetOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        logo.animate().alpha(1.0f).setDuration(2000)
        var ra = RotateAnimation(270f, 365f)
        ra.duration = 2000
        logo.startAnimation(ra)
        GetData(this).execute()

        ra.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                ra = RotateAnimation(365f, 360f)
                ra.duration = 500
                logo.startAnimation(ra)
                ra.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        frontFlag = true
                        if (frontFlag && backFlag) {
                            progressBar.visibility = ProgressBar.GONE
                            val intent = Intent(this@StartActivity, LoginActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                        if (frontFlag && !backFlag) {
                            progressBar.visibility = ProgressBar.VISIBLE
                            textView.visibility = TextView.VISIBLE
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                    }

                })
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })

    }


    inner class GetData(val context: Context) : AsyncTask<String, Void, String>() {


        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun doInBackground(vararg params: String?): String {
            admins.put("mahdi","admin")

            val _user = "sa"
            val _pass = "**********"
            val _DB = "yadak"
            val _server = "**********"
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
            var conn: Connection? = null
            var ConnURL: String? = null
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver")
                ConnURL = ("jdbc:jtds:sqlserver://" + _server + ";"
                        + "databaseName=" + _DB + ";user=" + _user + ";password="
                        + _pass + ";")
                conn = DriverManager.getConnection(ConnURL)

                val queryStmt =
                    "select * from dbo.users2"
                val queryStmt2 =
                    "select * from dbo.adminUsers"

                val stmt = conn.createStatement()
                val stmt2 = conn.createStatement()
                val rslt = stmt.executeQuery(queryStmt)
                val rslt2 = stmt2.executeQuery(queryStmt2)

                while (rslt.next()) {
                    var user = rslt.getString(1)
                    var pass = rslt.getString(2)
                    users.put(user, pass)
                }

                while (rslt2.next()) {
                    var user = rslt2.getString(1)
                    var pass = rslt2.getString(2)
                    admins.put(user, pass)
                }

                val tableInfoQuery = "select * from dbo.Stock"
                val result = stmt.executeQuery(tableInfoQuery)

                while (result.next()){
                    var id = result.getString(1)
                    var partName = result.getString(2)
                    var carName = result.getString(3)
                    var creationTime = result.getString(4)
                    var carModel = result.getString(5)
                    var carChassis = result.getString(6)
                    var brandName = result.getString(7)
                    var country = result.getString(8)
                    var price = result.getString(9)
                    var supplierId = result.getString(10)
                    var stockCount = result.getString(11)
                    var description = result.getString(13)
                    var lastModificationTime = result.getString(16)
                    stockInfo.add(SearchInfo(id.toInt(),partName,carName,creationTime,carModel,carChassis,brandName,country,price.toInt(),supplierId,stockCount.toInt(),description,lastModificationTime))
                }
                fill()


                backFlag = true

                if (frontFlag && backFlag) {
                    runOnUiThread {progressBar.visibility = ProgressBar.GONE}
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }

                stmt.close()
                conn.close()
                rslt.close()
                rslt2.close()

            } catch (se: SQLException) {
                runOnUiThread {
                    // some code #3 (Write your code here to run in UI thread)
                    Toast.makeText(context, "خطا در ایجاد اتصال", Toast.LENGTH_LONG).show()
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage("خطا در ایجاد اتصال (ممکن است نیاز باشد فیلترشکن خود را روشن کنید)")
                    builder.apply {
                        setPositiveButton("اتصال مجدد",
                            DialogInterface.OnClickListener { dialog, id ->
                                // User clicked OK button
                                doInBackground()
                            })
                        setNegativeButton("خروج از برنامه",
                            DialogInterface.OnClickListener { dialog, id ->
                                // User cancelled the dialog
                                System.exit(0);
                            })
                    }
                    // Set other dialog properties

                    // Create the AlertDialog
                    builder.create()
                    builder.show()
                }
                se.printStackTrace()
            } catch (e: Exception) {
                runOnUiThread {
                    // some code #3 (Write your code here to run in UI thread)
                    Toast.makeText(context, "PLEASE CHECK YOUR CONNECTION!", Toast.LENGTH_LONG)
                        .show()
                }
                e.printStackTrace()
            }
            return ""
        }

        fun fill(){
            for(inf in stockInfo){
                parts.add(inf.partName)
                cars.add(inf.carName)
                brands.add(inf.brandName)
            }
        }


    }
}

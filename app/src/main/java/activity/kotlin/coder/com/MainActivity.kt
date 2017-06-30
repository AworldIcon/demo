package activity.kotlin.coder.com

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import cn.jpush.android.api.JPushInterface



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text.text="hello android"
        val atyu=Atyu()
        atyu.name="android"
       // text.setOnClickListener{toast("hello"+text.text)}
        text.setOnClickListener {
            v: View -> Toast.makeText(this,atyu.name,Toast.LENGTH_SHORT).show()
        }
    }
    fun Context.toast(message:String, length : Int = Toast.LENGTH_SHORT){
        Toast.makeText(this,message,length)
    }
     fun String.getData(){}
    override fun onResume() {
        super.onResume()
        JPushInterface.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        JPushInterface.onPause(this)
    }
}

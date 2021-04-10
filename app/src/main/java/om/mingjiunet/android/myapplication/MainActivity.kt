package om.mingjiunet.android.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //设置点击事件
        tvMainSchedule?.setOnClickListener {
            tvMainSchedule?.countDownSchedule()
        }
    }
}
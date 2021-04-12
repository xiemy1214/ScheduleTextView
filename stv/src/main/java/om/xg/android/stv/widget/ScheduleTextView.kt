package om.xg.android.stv.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import om.xg.android.stv.R
import om.xg.android.stv.thread.CountDownTask

class ScheduleTextView : AppCompatTextView {

    @DrawableRes
    @ColorRes //默认字体颜色
    private var defaultTextColor: Int = R.color.white

    @DrawableRes
    @ColorRes //开始计时字体颜色
    private var startTextColor: Int = R.color._999999

    @DrawableRes
    @ColorRes //默认背景颜色
    private val defaultBgRes = R.drawable.bg_corners4_fb3244

    @DrawableRes
    @ColorRes //开始计时背景颜色
    private val startBgRes = R.drawable.bg_corners4_f4f4f4

    //默认倒计时时长
    private var scheduleTime = 60
    private var sendCode: String = "发送验证码"

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = resources.obtainAttributes(attrs, R.styleable.ScheduleTextView)
            //字体颜色
            defaultTextColor = typedArray.getResourceId(
                R.styleable.ScheduleTextView_schedule_default_bg_color,
                defaultTextColor
            )
            startTextColor = typedArray.getResourceId(
                R.styleable.ScheduleTextView_schedule_start_text_color,
                startTextColor
            )
            //字体颜色
            defaultTextColor = typedArray.getResourceId(
                R.styleable.ScheduleTextView_schedule_default_bg_color,
                defaultTextColor
            )
            startTextColor = typedArray.getResourceId(
                R.styleable.ScheduleTextView_schedule_start_bg_color,
                startTextColor
            )
            sendCode =
                typedArray.getString(R.styleable.ScheduleTextView_schedule_send_code) ?: sendCode
            scheduleTime =
                typedArray.getInt(R.styleable.ScheduleTextView_schedule_time, scheduleTime)
        }
        enabled()
        gravity = Gravity.CENTER
    }

    /***倒计时**/
    fun countDownSchedule(scheduleTime: Int?) {
        if (scheduleTime != null) this.scheduleTime = scheduleTime
        countDownSchedule()
    }

    /***倒计时**/
    fun countDownSchedule() {
        CountDownTask.newInstance()
            .countDownSchedule(scheduleTime, object : CountDownTask.CountDownCallback {
                override fun timerRun(surplusTime: Int) {
                    enabled(false, surplusTime.toString() + "s")
                }

                override fun timerStop() {
                    enabled()
                }
            })
    }

    private fun enabled(enabled: Boolean? = true, mText: String? = sendCode) {
        isEnabled = enabled ?: true
        text = if (isEnabled) {
            setBackgroundResource(defaultBgRes)
            setTextColor(ContextCompat.getColor(context, defaultTextColor))
            mText
        } else {
            setBackgroundResource(startBgRes)
            setTextColor(ContextCompat.getColor(context, startTextColor))
            mText
        }
    }
}
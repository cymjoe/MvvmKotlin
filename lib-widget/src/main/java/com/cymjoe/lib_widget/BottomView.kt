package com.cymjoe.lib_widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.cymjoe.lib_widget.R
import kotlinx.android.synthetic.main.layout_bottom_view.view.*

class BottomView : FrameLayout {
    constructor(context: Context) : super(context) {
        initView(context)
    }

    @SuppressLint("ResourceAsColor")
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.BottomView)
        try {
            textCheckColor =
                typedArray.getColor(
                    R.styleable.BottomView_colorButtonChecked,
                    Color.parseColor("#333333")
                )
            textNormalColor =
                typedArray.getColor(
                    R.styleable.BottomView_colorButtonNormal,
                    Color.parseColor("#333333")
                )
            srcCheck = typedArray.getResourceId(
                R.styleable.BottomView_checkedIcon,
                android.R.mipmap.sym_def_app_icon
            )
            srcNormal = typedArray.getResourceId(
                R.styleable.BottomView_normalIcon,
                android.R.mipmap.sym_def_app_icon
            )
            check = typedArray.getBoolean(R.styleable.BottomView_checked, false)
            content = typedArray.getString(R.styleable.BottomView_contents).toString()
        } finally {
            typedArray.recycle()
        }

        initView(context)
    }

    @SuppressLint("Recycle", "ResourceAsColor")
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {


        initView(context)
    }


    private var textCheckColor = 0
    private var textNormalColor = 0
    private var srcCheck = 0
    private var srcNormal = 0
    private var check = false
    private var content = ""


    fun setSelectImg(src: Int) {
        this.srcCheck = src
    }

    fun setCheckColors(color: Int) {
        textCheckColor = color

    }

    fun setNormalColors(color: Int) {
        textNormalColor = color
        tv_bottom?.setTextColor(textNormalColor)

    }


    fun setChecks(check: Boolean) {

        if (check) {
            setCheckColors(textCheckColor)
            setCheckImg(srcCheck)
        } else {
            setImg(srcNormal)
            setNormalColors(textNormalColor)
        }
    }

    private var tv_bottom: TextView? = null
    private var img_bottom: ImageView? = null


    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.layout_bottom_view, this)
        tv_bottom = findViewById(R.id.tv_bottom)

        img_bottom = findViewById(R.id.img_bottom)


    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setChecks(check)
        setBottomTitle(content)

    }

    /**
     * 显示红点
     */
    fun showRedPoint(position: Int) {
        when (position) {
            0 -> tv_red_point.visibility = View.GONE
            else -> {
                tv_red_point.visibility = View.VISIBLE
                if (position > 99) {
                    tv_red_point.text = "99+"
                } else {
                    tv_red_point.text = position.toString()
                }
            }
        }
    }

    fun setBottomTitle(title: String) {
        tv_bottom?.text = title


    }

    fun setImg(src: Int) {
        srcNormal = src
        img_bottom?.setImageResource(srcNormal)

    }

    fun setImgResource(src: Int) {
        img_bottom?.setImageResource(src)

    }


    fun setCheckImg(src: Int) {
        srcCheck = src
        img_bottom?.setImageResource(srcCheck)

    }

    fun setImg(url: String) {
        img_bottom?.let {
            Glide.with(context)
                .load(url)
                .into(it)
        }
    }
}
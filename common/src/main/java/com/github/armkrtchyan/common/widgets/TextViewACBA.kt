package com.github.armkrtchyan.common.widgets

import com.github.armkrtchyan.common.R
import com.github.armkrtchyan.common.shared.PreventDoubleClickListener
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TextViewACBA : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private var mIsPreventDoubleClick = true

    private fun init(attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.TextViewACBA).apply {
            mIsPreventDoubleClick = getBoolean(R.styleable.TextViewACBA_isPreventClick, true)
            recycle()
        }
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        if (mIsPreventDoubleClick) super.setOnClickListener(PreventDoubleClickListener(onClickListener))
        else super.setOnClickListener(onClickListener)
    }
}
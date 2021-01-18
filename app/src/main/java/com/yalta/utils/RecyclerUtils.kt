package com.yalta.utils

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class RecyclerItemClickListener(recyclerView: RecyclerView, private val listener: OnItemClickListener?) :
    RecyclerView.OnItemTouchListener {
    private var gestureDetector: GestureDetector =
        GestureDetector(recyclerView.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {}
        })

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onLongItemClick(child: View, position: Int)
    }

    init {
        gestureDetector = GestureDetector(recyclerView.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && listener != null) {
                    listener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && listener != null && gestureDetector.onTouchEvent(e)) {
            listener.onItemClick(childView, view.getChildAdapterPosition(childView))
            return true
        }
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}

inline fun RecyclerView.onItemClick(crossinline action: (position: Int) -> Unit) =
    setOnItemClickListener(onClick = action)

inline fun RecyclerView.onLongItemClick(crossinline action: (position: Int) -> Unit) =
    setOnItemClickListener(onLongClick = action)

inline fun RecyclerView.setOnItemClickListener(
    crossinline onClick: (position: Int) -> Unit = {},
    crossinline onLongClick: (position: Int) -> Unit = {}
) {
    addOnItemTouchListener(
        RecyclerItemClickListener(this,
            object : RecyclerItemClickListener.OnItemClickListener {
                override fun onLongItemClick(child: View, position: Int) {
                    onLongClick(position)
                }

                override fun onItemClick(view: View, position: Int) {
                    onClick(position)
                }
            })
    )
}

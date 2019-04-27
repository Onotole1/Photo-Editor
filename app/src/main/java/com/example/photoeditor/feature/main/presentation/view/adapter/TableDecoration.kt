package com.example.photoeditor.feature.main.presentation.view.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditor.R


class TableDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = context.resources.getDimension(R.dimen.table_border)
        color = ContextCompat.getColor(context, android.R.color.black)
    }

    private val path = Path()

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            val viewType = parent.getChildViewHolder(child).itemViewType
            if (viewType == R.layout.item_controller) {
                continue
            }

            val bottom = child.bottom.toFloat()

            val top = child.top.toFloat()

            val endX = (child.right).toFloat()

            val startX = child.x

            path.reset()

            path.moveTo(startX, top)
            path.lineTo(startX, bottom)
            path.lineTo(endX, bottom)
            path.lineTo(endX, top)

            if (parent.getChildAdapterPosition(child) == 1) {
                path.lineTo(startX, top)
            }

            c.drawPath(path, paint)
        }
    }
}
package com.spitchenko.photoeditor.feature.main.presentation.view.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.spitchenko.photoeditor.R


class TableDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val borderSize = context.resources.getDimension(R.dimen.table_border)
    private val evenColor = ContextCompat.getColor(context, R.color.table_item_even)
    private val oddColor = ContextCompat.getColor(context, R.color.table_item_odd)

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = borderSize
        color = ContextCompat.getColor(context, android.R.color.black)
    }

    private val fillPaint = Paint().apply {
        style = Paint.Style.FILL
    }

    private val fillOffset = borderSize / 2

    private val path = Path()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            val viewType = parent.getChildViewHolder(child).itemViewType
            if (viewType == R.layout.item_controller) {
                continue
            }

            val adapterPosition = parent.getChildAdapterPosition(child)

            drawBorders(canvas, child, adapterPosition)

            drawBackground(canvas, child, adapterPosition)
        }
    }

    private fun drawBorders(canvas: Canvas, child: View, adapterPosition: Int) {
        val bottom = child.bottom.toFloat()

        val top = child.top.toFloat()

        val endX = child.right.toFloat()

        val startX = child.x

        path.reset()

        path.moveTo(startX, top)
        path.lineTo(startX, bottom)
        path.lineTo(endX, bottom)
        path.lineTo(endX, top)

        if (adapterPosition == 1) {
            path.lineTo(startX, top)
        }

        canvas.drawPath(path, paint)
    }

    private fun drawBackground(canvas: Canvas, child: View, adapterPosition: Int) {

        fillPaint.color = if (adapterPosition % 2 == 0) {
            evenColor
        } else {
            oddColor
        }

        val bottom = child.bottom.toFloat() - fillOffset

        val top = child.top.toFloat() + fillOffset

        val endX = child.right.toFloat() - fillOffset

        val startX = child.x + fillOffset

        canvas.drawRect(startX, top, endX, bottom, fillPaint)
    }
}
package co.netguru.sectionsDecorator

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlin.math.max
import kotlin.math.min

class HorizontalPainter(
    fixLayoutSize: (View, ViewGroup) -> Unit,
    headerToLineOffset: Float,
    linePaint: Paint
) : Painter(fixLayoutSize, headerToLineOffset, linePaint) {


    override fun getLineStart(sectionIndex: Int, first: View): Float {
        val leftMargin =
            (first.layoutParams as ViewGroup.MarginLayoutParams).leftMargin.toFloat()

        return if (sectionIndex == 0) {
            leftMargin
        } else {
            max(first.left.toFloat(), leftMargin)
        }
    }

    override fun getLineEnd(
        sectionIndex: Int,
        sectionSize: Int,
        canvas: Canvas,
        last: View
    ): Float {
        val rightMargin =
            (last.layoutParams as ViewGroup.MarginLayoutParams).rightMargin.toFloat()
        return if (sectionIndex == sectionSize - 1) {
            canvas.width.toFloat() - rightMargin
        } else {
            min(last.right.toFloat(), canvas.width.toFloat() - rightMargin)
        }

    }


    override fun getXStart(sectionIndex: Int, first: View) = getLineStart(sectionIndex, first)

    override fun getXEnd(
        sectionIndex: Int,
        sectionSize: Int,
        canvas: Canvas,
        last: View
    ) = getLineEnd(sectionIndex, sectionSize, canvas, last)

    override fun getYStart(sectionIndex: Int, first: View) = headerToLineOffset

    override fun getYEnd(
        sectionIndex: Int,
        sectionSize: Int,
        canvas: Canvas,
        last: View
    ) = headerToLineOffset

    override fun getOutRect(outRect: Rect) {
        outRect.top = headerToLineOffset.toInt()
    }

    override fun drawHeader(
        canvas: Canvas,
        textView: TextView,
        startPosition: Float,
        headerWidth: Int
    ) {
        canvas.save()
        canvas.translate(startPosition, 0f)
        textView.draw(canvas)
        canvas.restore()
    }

}

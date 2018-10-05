package co.netguru.sectionsDecorator

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlin.math.max
import kotlin.math.min

class VerticalPainter(
    fixLayoutSize: (View, ViewGroup) -> Unit,
    headerToLineOffset: Float,
    linePaint: Paint
) : Painter(fixLayoutSize, headerToLineOffset, linePaint) {


    override fun getLineStart(sectionIndex: Int, first: View): Float {
        val topMargin =
            (first.layoutParams as ViewGroup.MarginLayoutParams).topMargin.toFloat()
        return if (sectionIndex == 0) {
            topMargin
        } else {
            max(first.top.toFloat(), topMargin)
        }
    }

    override fun getLineEnd(
        sectionIndex: Int,
        sectionSize: Int,
        canvas: Canvas,
        last: View
    ): Float {
        val bottomMargin =
            (last.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin.toFloat()

        return if (sectionIndex == sectionSize - 1) {
            canvas.height.toFloat() - bottomMargin
        } else {
            min(last.bottom.toFloat(), canvas.height.toFloat() - bottomMargin)
        }
    }

    override fun getXStart(sectionIndex: Int, first: View) = headerToLineOffset
    override fun getYStart(sectionIndex: Int, first: View) = getLineStart(sectionIndex, first)

    override fun getXEnd(sectionIndex: Int, sectionSize: Int, canvas: Canvas, last: View) =
        headerToLineOffset

    override fun getYEnd(sectionIndex: Int, sectionSize: Int, canvas: Canvas, last: View) =
        getLineEnd(sectionIndex, sectionSize, canvas, last)

    override fun drawHeader(
        canvas: Canvas,
        textView: TextView,
        startPosition: Float,
        headerWidth: Int
    ) {
        canvas.save()
        canvas.translate(0f, startPosition + headerWidth)
        canvas.rotate(-90f)
        textView.draw(canvas)
        canvas.restore()
    }

    override fun getOutRect(outRect: Rect) {
        outRect.left = headerToLineOffset.toInt()
    }
}

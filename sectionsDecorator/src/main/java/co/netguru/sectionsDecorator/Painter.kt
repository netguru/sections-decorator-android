package co.netguru.sectionsDecorator

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

abstract class Painter(
    internal val fixLayoutSize: (View, ViewGroup) -> Unit,
    internal val headerToLineOffset: Float,
    private val linePaint: Paint
) {
    abstract fun getOutRect(outRect: Rect)

    fun paint(
        canvas: Canvas,
        sectionIndex: Int,
        sectionTitle: String,
        sectionsVisibleElements: List<View>,
        headerView: TextView,
        sectionSize: Int,
        parent: ViewGroup
    ) {

        val lineStart = getLineStart(sectionIndex, sectionsVisibleElements.first())
        val lineEnd = getLineEnd(sectionIndex, sectionSize, canvas, sectionsVisibleElements.last())

        if (lineEnd > lineStart) {
            canvas.drawLine(
                getXStart(sectionIndex, sectionsVisibleElements.first()),
                getYStart(sectionIndex, sectionsVisibleElements.first()),
                getXEnd(sectionIndex, sectionSize, canvas, sectionsVisibleElements.last()),
                getYEnd(sectionIndex, sectionSize, canvas, sectionsVisibleElements.last()),
                linePaint
            )
        }

        headerView.apply {
            text = sectionTitle
            fixLayoutSize(this, parent)

            val headerWidth = with(this) {
                width + paddingStart + paddingEnd +
                        kotlin.with(layoutParams as ViewGroup.MarginLayoutParams) {
                            marginStart + marginEnd
                        }
            }

            val startPosition = if (lineEnd - lineStart < headerWidth && sectionIndex == 0) {
                lineEnd - headerWidth
            } else {
                lineStart
            }

            drawHeader(canvas, this, startPosition, headerWidth)
        }
    }

    internal abstract fun getLineStart(sectionIndex: Int, first: View): Float
    internal abstract fun getLineEnd(
        sectionIndex: Int,
        sectionSize: Int,
        canvas: Canvas,
        last: View
    ): Float

    internal abstract fun getXStart(sectionIndex: Int, first: View): Float
    internal abstract fun getXEnd(
        sectionIndex: Int,
        sectionSize: Int,
        canvas: Canvas,
        last: View
    ): Float

    internal abstract fun getYStart(sectionIndex: Int, first: View): Float
    internal abstract fun getYEnd(
        sectionIndex: Int,
        sectionSize: Int,
        canvas: Canvas,
        last: View
    ): Float

    internal abstract fun drawHeader(
        canvas: Canvas,
        textView: TextView,
        startPosition: Float,
        headerWidth: Int
    )


}

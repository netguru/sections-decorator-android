package co.netguru.sectionsDecorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlin.math.max


class SectionDecorator(private val context: Context) : RecyclerView.ItemDecoration() {

    private val headerHorizontalOffset by lazy { context.resources.getDimensionPixelSize(R.dimen.margin_default) }
    private val headerVerticalOffset by lazy { context.resources.getDimensionPixelSize(R.dimen.header_vertical_offset) }
    private val headerToLineOffset by lazy { context.resources.getDimensionPixelSize(R.dimen.header_to_line_offset).toFloat() }
    private val linePaint = Paint()

    private var headerView: TextView? = null
    private var headerLayoutId: Int = R.layout.section_header

    init {
        linePaint.color = context.getColorCompat(android.R.color.black)
        linePaint.strokeWidth = context.resources.getDimensionPixelSize(R.dimen.divider_size).toFloat()
    }

    fun setLineColor(@ColorRes color: Int){
        linePaint.color = context.getColorCompat(color)
    }

    fun setLineWidth(width: Float){
        linePaint.strokeWidth = width
    }

    fun setHeaderView(@LayoutRes layout: Int){
        headerView = null
        headerLayoutId = layout
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = headerVerticalOffset
    }

    private fun getSectionsAdapter(parent: RecyclerView): SectionsAdapterInterface {
        return parent.adapter as SectionsAdapterInterface
    }

    private fun createHeaderView(parent: RecyclerView) {
        headerView = LayoutInflater.from(parent.context)
                .inflate(headerLayoutId, parent, false) as TextView
        fixLayoutSize(headerView!!, parent)
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(canvas, parent, state)
        val adapter = getSectionsAdapter(parent)

        if (headerView == null) createHeaderView(parent)

        parent.asSequence()
                .map {
                    Pair(adapter.getSectionTitleForPosition(parent.getChildAdapterPosition(it)), it)
                }.fold(mapOf<String, List<View>>()) { acc, data ->
                    acc.addToValueList(data.first, data.second)
                }.forEach { (sectionTitle, sectionsVisibleElements) ->
                    //draw line for section
                    val lineStart = max(sectionsVisibleElements.first().left.toFloat(), headerHorizontalOffset.toFloat())
                    val lineEnd = sectionsVisibleElements.last().right.toFloat()

                    if (lineEnd > lineStart) {
                        canvas.drawLine(lineStart, headerToLineOffset, lineEnd, headerToLineOffset, linePaint)
                    }

                    //draw title for section
                    headerView?.apply {
                        text = sectionTitle
                        fixLayoutSize(this, parent)
                        drawHeader(canvas, this, lineStart, lineEnd, sectionTitle)
                    }
                }
    }

    private fun SectionsAdapterInterface.getSectionTitleForPosition(currentPosition: Int): String {
        var count = 0
        for (i in 0..getSectionsCount()) {
            count += getItemCountForSection(i)
            if (currentPosition < count) return getSectionTitleAt(i)
        }
        throw IndexOutOfBoundsException("try to get index=$currentPosition from items lenght=$count")
    }

    private fun drawHeader(canvas: Canvas, headerView: View, start: Float, end: Float, debugSection: String) {

        val headerWidth = with(headerView) {
            width + paddingStart + paddingEnd +
                    with(layoutParams as ViewGroup.MarginLayoutParams) {
                        marginStart + marginEnd
                    }
        }

        val startPosition = if (headerWidth + start > end) {
            end - headerWidth
        } else {
            start
        }

        canvas.save()
        canvas.translate(startPosition, 0f)
        headerView.draw(canvas)
        canvas.restore()
    }

    /**
     * Measures the headerTitle view to make sure its size is greater than 0 and will be drawn
     * [RecyclerView item decorations](https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations)
     */
    private fun fixLayoutSize(view: View, parent: ViewGroup) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
                View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val childWidth = ViewGroup.getChildMeasureSpec(
                widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
                heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height
        )

        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }
}

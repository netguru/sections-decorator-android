package co.netguru.sectionsDecorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView


class SectionDecorator(private val context: Context) : RecyclerView.ItemDecoration() {

    private val headerVerticalOffset by lazy {
        context.resources.getDimensionPixelSize(R.dimen.header_vertical_offset).toFloat()
    }
    private val headerToLineOffset by lazy {
        context.resources.getDimensionPixelSize(R.dimen.header_to_line_offset).toFloat()
    }
    private val linePaint = Paint()

    private var headerView: TextView? = null
    private var headerLayoutId: Int = R.layout.section_header

    private var painter: Painter? = null

    init {
        linePaint.color = context.getColorCompat(android.R.color.black)
        linePaint.strokeWidth =
                context.resources.getDimensionPixelSize(R.dimen.divider_size).toFloat()
    }

    fun setLineColor(@ColorRes color: Int) {
        linePaint.color = context.getColorCompat(color)
    }

    fun setLineWidth(width: Float) {
        linePaint.strokeWidth = width
    }

    fun setHeaderView(@LayoutRes layout: Int) {
        headerView = null
        headerLayoutId = layout
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State?
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        createPainter(parent)

        painter?.getOutRect(outRect)

    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(canvas, parent, state)
        val adapter = getSectionsAdapter(parent)

        createPainter(parent)

        if (headerView == null) createHeaderView(parent)

        val sectionsList = parent.asSequence()
            .map {
                Pair(adapter.getSectionTitleForPosition(parent.getChildAdapterPosition(it)), it)
            }.fold(mapOf<String, List<View>>()) { acc, data ->
                acc.addToValueList(data.first, data.second)
            }.toList()

        nullCheck2(painter, headerView) { painter, headerView ->
            sectionsList.forEachIndexed { index, (sectionTitle, sectionsVisibleElements) ->
                painter.paint(
                    canvas,
                    index,
                    sectionTitle,
                    sectionsVisibleElements,
                    headerView,
                    sectionsList.size,
                    parent
                )
            }
        }
    }

    private fun getSectionsAdapter(parent: RecyclerView): SectionsAdapterInterface {
        return parent.adapter as SectionsAdapterInterface
    }

    private fun createHeaderView(parent: RecyclerView) {
        headerView = LayoutInflater.from(parent.context)
            .inflate(headerLayoutId, parent, false) as TextView
        fixLayoutSize(headerView!!, parent)
    }

    private fun createPainter(parent: RecyclerView) {
        if (painter == null) {
            if (parent.layoutManager !is LinearLayoutManager) {
                throw IllegalArgumentException("Section decorator only works with linear layout manager")
            } else {
                painter =
                        if ((parent.layoutManager as LinearLayoutManager).orientation == LinearLayout.HORIZONTAL) {
                            HorizontalPainter(::fixLayoutSize, headerToLineOffset, linePaint)
                        } else {
                            VerticalPainter(::fixLayoutSize, headerVerticalOffset, linePaint)
                        }
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

package co.netguru.android.sectionsDecoratorDemo.feature.demo

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import co.netguru.android.sectionsDecoratorDemo.R
import co.netguru.sectionsDecorator.SectionsAdapterInterface
import kotlinx.android.synthetic.main.list_item_horizontal.view.*

class MyAdapter(val orientation: Int) : RecyclerView.Adapter<MyViewHolder>(),
    SectionsAdapterInterface {

    private val items = linkedMapOf(
            Pair("Section 1", listOf("one 1", "two 1", "three 1", "four 1", "five 1", "six 1", "seven 1", "eight 1", "nine 1", "ten 1")),
            Pair("Middle", listOf("middle")),
            Pair("Section 2", listOf("one 2", "two 2", "three 2", "four 2", "five 2", "six 2", "seven 2", "eight 2", "nine 2", "ten 2"))
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = if (orientation == LinearLayout.HORIZONTAL) {
            R.layout.list_item_horizontal
        } else {
            R.layout.list_item_vertical
        }
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return items.values.flatten().size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sectionNameOfItem = items.entries.flatMap { entry -> entry.value.map { Pair(entry.key, it) } }[position].first
        holder.bind(
            items.values.flatten()[position],
            items.keys.asSequence().toList().indexOf(sectionNameOfItem)
        )
    }

    override fun getSectionsCount(): Int {
        return items.keys.size
    }

    override fun getSectionTitleAt(sectionIndex: Int): String {
        return items.keys.toList()[sectionIndex]
    }

    override fun getItemCountForSection(sectionIndex: Int): Int {
        return (items[items.keys.toList()[sectionIndex]] ?: emptyList()).size
    }
}

class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){
    private val textView = view.item_text!!

    fun bind(text: String, section: Int){
        if (section.rem(2) == 0) {
            view.setBackgroundColor(Color.CYAN)
        } else {
            view.setBackgroundColor(Color.LTGRAY)
        }
        textView.text = text
    }
}

package co.netguru.android.sectionsDecoratorDemo.feature.demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import co.netguru.android.sectionsDecoratorDemo.R
import co.netguru.sectionsDecorator.SectionDecorator
import kotlinx.android.synthetic.main.fragment_horizontal.*

class HorizontalFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_horizontal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = MyAdapter(LinearLayout.HORIZONTAL)
        recyclerView.addItemDecoration(SectionDecorator(context!!).apply {
            setLineColor(R.color.green)
            setLineWidth(15f)
        })
    }
}

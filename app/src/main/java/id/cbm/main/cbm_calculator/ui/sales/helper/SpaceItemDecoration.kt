package id.cbm.main.cbm_calculator.ui.sales.helper // ktlint-disable package-name

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
//        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = space
    }
}

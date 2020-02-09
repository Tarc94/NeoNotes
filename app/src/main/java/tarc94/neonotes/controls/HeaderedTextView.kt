package tarc94.neonotes.controls

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.headeredtextview.view.*
import tarc94.neonotes.R

/**
 * A custom view for a text view with a header
 */

class HeaderedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private var controlHeader: String? = null

    private var controlContent: String? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.headeredtextview, this, true)

        orientation = VERTICAL

        if (attrs != null) {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.HeaderedTextView, defStyle, 0)

            setHeader(typedArray.getString(R.styleable.HeaderedTextView_header))

            setContent(typedArray.getString(R.styleable.HeaderedTextView_content))

            typedArray.recycle()
        }
    }

    /**
     * Sets the header of the control
     */
    fun setHeader(header: String?) {
        controlHeader = header

        HeaderedTextView_Header.text = header

        updateVisibility()
    }

    /**
     * Sets the main body of text of the control
     */
    fun setContent(content: String?) {
        controlContent = content

        HeaderedTextView_Content.text = content

        updateVisibility()
    }

    /**
     * Updates the visibility of the controls dependant upon the text set for both the header and main body of content
     */
    private fun updateVisibility() {
        if (controlHeader == null || controlHeader!!.trim().isEmpty()) {
            HeaderedTextView_Header.visibility = View.GONE
        } else {
            HeaderedTextView_Header.visibility = View.VISIBLE
        }

        if (controlContent == null || controlContent!!.trim().isEmpty()) {
            HeaderedTextView_Header.visibility = View.GONE
            HeaderedTextView_Content.visibility = View.GONE
        } else {
            HeaderedTextView_Content.visibility = View.VISIBLE
        }
    }
}

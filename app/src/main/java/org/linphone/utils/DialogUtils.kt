
package org.linphone.utils

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import org.linphone.R
import org.linphone.activities.main.viewmodels.DialogViewModel
import org.linphone.databinding.DialogBinding

class DialogUtils {
    companion object {
        fun getDialog(context: Context, viewModel: DialogViewModel): Dialog {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            val binding: DialogBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog, null, false)
            binding.viewModel = viewModel
            dialog.setContentView(binding.root)

            val d: Drawable = ColorDrawable(ContextCompat.getColor(dialog.context, R.color.dark_grey_color))
            d.alpha = 200
            dialog.window
                ?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
            dialog.window?.setBackgroundDrawable(d)
            return dialog
        }
    }
}

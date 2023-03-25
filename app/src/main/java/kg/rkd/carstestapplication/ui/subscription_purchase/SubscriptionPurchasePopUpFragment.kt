package kg.rkd.carstestapplication.ui.subscription_purchase

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kg.rkd.carstestapplication.AppConfig
import kg.rkd.carstestapplication.ui.CarsViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SubscriptionPurchasePopUpFragment : DialogFragment() {

    companion object {
        fun newInstance(): SubscriptionPurchasePopUpFragment {
            return SubscriptionPurchasePopUpFragment()
        }
    }

    private val viewModel: CarsViewModel by lazy {
        requireParentFragment().getViewModel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireParentFragment().context
        return AlertDialog.Builder(requireContext())
            .setTitle("Subscribe and increase your car collection!")
            .setMessage(
                """
                Get unlimited access to premium feature for ${AppConfig.SUBSCRIPTION_DURATION / 60} minutes for only ${AppConfig.SUBSCRIPTION_PRICE}.
            """.trimIndent()
            )
            .setNegativeButton("No, thanks") { _, _ -> }
            .setPositiveButton("Buy") { d, i ->
                viewModel.startSubscriptionPurchaseFlow {
                    Toast.makeText(
                        context,
                        "Поздравляем! Вы приобрели подписку!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .create()
    }
}

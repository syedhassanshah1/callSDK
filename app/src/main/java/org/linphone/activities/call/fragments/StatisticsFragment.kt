
package org.linphone.activities.call.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import org.linphone.R
import org.linphone.activities.GenericFragment
import org.linphone.activities.call.viewmodels.StatisticsListViewModel
import org.linphone.databinding.CallStatisticsFragmentBinding

class StatisticsFragment : GenericFragment<CallStatisticsFragmentBinding>() {
    private lateinit var viewModel: StatisticsListViewModel

    override fun getLayoutId(): Int = R.layout.call_statistics_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(StatisticsListViewModel::class.java)
        binding.viewModel = viewModel
    }
}

package com.example.browsing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.browsing.databinding.FragmentBrowseCatsBinding
import com.example.core.utils.NetworkHelper
import com.example.core.utils.Resource
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import android.util.Pair as UtilPair

class BrowseCatsFragment : Fragment(), KoinComponent {

    private val injectModule = module {
        single<OnCatSelectedListener> {
            this@BrowseCatsFragment.onCatSelectedListener
        }
    }

    lateinit var binding: FragmentBrowseCatsBinding

    private val browseViewModel: BrowseCatsViewModel by activityViewModels()
    private val catsAdapter = CatsAdapter()
    private val networkHelper: NetworkHelper by inject()
    private var tmpIsOnline: Boolean? = null
    private val onlineSnackbar by lazy {
        Snackbar.make(binding.coordinatorLayout, R.string.online, Snackbar.LENGTH_SHORT).also {
            it.view.setBackgroundColor(ContextCompat.getColor(it.context, R.color.colorPrimaryDark))
        }
    }
    private val offlineSnackbar by lazy {
        Snackbar.make(binding.coordinatorLayout, R.string.offline, Snackbar.LENGTH_INDEFINITE)
            .also {
                it.view.setBackgroundColor(
                    ContextCompat.getColor(
                        it.context,
                        R.color.colorPrimaryDark
                    )
                )
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBrowseCatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        subscribeToNetworkChanges()
        configureRecyclerView()
        loadKoinModules(injectModule)

        browseViewModel.catsLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.lottieLoader.visibility = View.GONE
                    binding.list.visibility = View.VISIBLE
                    catsAdapter.replaceItems(it.data ?: emptyList())
                }
                Resource.Status.LOADING -> {
                    if (catsAdapter.itemCount == 0) {
                        binding.lottieLoader.visibility = View.VISIBLE
                        binding.list.visibility = View.GONE
                        binding.offlineGroup.visibility = View.GONE
                    }
                }
                Resource.Status.ERROR -> {
                    binding.lottieLoader.visibility = View.GONE
                    binding.list.visibility = View.VISIBLE
                    if (catsAdapter.itemCount == 0)
                        binding.offlineGroup.visibility = View.VISIBLE
                }
                else -> {
                }
            }
        })

        binding.buttonRetry.setOnClickListener {
            browseViewModel.fetchCats()
        }

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unloadKoinModules(injectModule)
    }

    private fun subscribeToNetworkChanges() {
        networkHelper.connectivityLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { isOnline ->
                if (isOnline) {
                    if (tmpIsOnline == false) {
                        onlineSnackbar.show()
                        offlineSnackbar.dismiss()
                    }
                } else {
                    onlineSnackbar.dismiss()
                    offlineSnackbar.show()
                }
                if (catsAdapter.itemCount == 0) {
                    browseViewModel.fetchCats()
                }
            }
            tmpIsOnline = it
        })
    }

    private fun configureRecyclerView() {
        binding.list.adapter = catsAdapter
        binding.list.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastItemIndex =
                    (binding.list.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                if (lastItemIndex > catsAdapter.itemCount - 6
                    && networkHelper.connectivityLiveData.value == true
                    && browseViewModel.catsLiveData.value?.status != Resource.Status.LOADING
                ) {
                    browseViewModel.fetchCats()
                    Toast.makeText(
                        requireContext(),
                        "$lastItemIndex",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private val onCatSelectedListener = object : OnCatSelectedListener {
        override fun onCatSelected(cat: Cat, imageView: ImageView) {
            Toast.makeText(requireContext(), cat.url, Toast.LENGTH_SHORT).show()
            val transitionName = ViewCompat.getTransitionName(imageView) ?: ""
            val extras = FragmentNavigatorExtras( imageView to transitionName)
            findNavController().navigate(BrowseCatsFragmentDirections.actionBrowsCatsFragmentToDetailsFragment(cat.url, transitionName), extras)
        }
    }

    interface OnCatSelectedListener {
        fun onCatSelected(cat: Cat, imageView: ImageView)
    }
}
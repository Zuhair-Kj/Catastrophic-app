package com.example.browsing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import androidx.transition.CircularPropagation
import androidx.transition.TransitionInflater
import com.example.browsing.databinding.FragmentDetailsBinding

class DetailsFragment: Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(this.context).inflateTransition(android.R.transition.move)
//        sharedElementReturnTransition = TransitionInflater.from(this.context).inflateTransition(android.R.transition.move)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.image.transitionName = args.transitionName
        binding.lifecycleOwner = this
        binding.imageUrl = args.imageUrl
        binding.executePendingBindings()
    }


}
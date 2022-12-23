package com.olp.weco.ui.array

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.olp.weco.base.BaseFragment
import com.olp.weco.databinding.FragmentArrayBinding

class ArrayFragment : BaseFragment() {

    private lateinit var _binding: FragmentArrayBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArrayBinding.inflate(layoutInflater)
        return _binding.root
    }


}
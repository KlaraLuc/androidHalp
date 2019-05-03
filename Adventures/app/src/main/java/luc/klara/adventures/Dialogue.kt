package luc.klara.adventures

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.databinding.DataBindingUtil
import luc.klara.adventures.databinding.DialogueBinding
import luc.klara.adventures.models.Story

class Dialogue : Fragment() {
    companion object {
        fun new() = Dialogue()
    }

    private lateinit var viewModel: DialogueViewModel
    private lateinit var binding: DialogueBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialogue, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let{
            viewModel = ViewModelProviders.of(it).get(DialogueViewModel::class.java)
        }

        viewModel.stroy.observe(this, Observer {
            story ->

            story?.let {
                binding.dialogue = it
            }
        })
    }
}
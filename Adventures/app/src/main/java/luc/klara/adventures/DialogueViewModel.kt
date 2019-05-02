package luc.klara.adventures

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.MutableLiveData
import luc.klara.adventures.models.DialogueItemViewModel

class DialogueViewModel : ViewModel() {
    val stroy = MutableLiveData<DialogueItemViewModel>()
}
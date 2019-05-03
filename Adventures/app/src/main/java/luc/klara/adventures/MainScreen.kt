package luc.klara.adventures

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.dialogue.*
import luc.klara.adventures.models.DialogueItemViewModel
import luc.klara.adventures.models.Story
import org.json.JSONArray
import java.io.InputStream
import java.lang.Exception

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class MainScreen : AppCompatActivity() {
    private val story = arrayListOf<Story>()
    private var id: String = "0"

    private lateinit var dialogueViewModel: DialogueViewModel

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
      /*  fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION*/
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
   //     fullscreen_content_controls.visibility = View.VISIBLE
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_screen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dialogueViewModel = ViewModelProviders.of(this).get(DialogueViewModel::class.java)

        mVisible = true

        readJSON()

        val initialModel = DialogueItemViewModel(story[0])

        dialogueViewModel.stroy.postValue(initialModel)


        println("it begins")
        choice_one.setOnClickListener{
            readStory(id + "1")
        }

        choice_two.setOnClickListener{
            readStory(id + "2")
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
  //      fullscreen_content_controls.visibility = View.GONE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
   //     fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }

    fun readJSON(){
        var json: String? = null

        try {
            val inputStream: InputStream = assets.open("story.json")
            json = inputStream.bufferedReader().use {it.readText()}

            var jArray = JSONArray(json)

            for (i in 0 until jArray.length() - 1){
                var obj = jArray.getJSONObject(i)
                story.add(Story(obj.getString("story"), obj.getString("image"), obj.getString("text"),
                    obj.getString("button1"), obj.getString("button2"), obj.getString("display")))
            }

        } catch (e: Exception){

        }
    }

    fun readStory(id: String){
        this.id = id
        val filtered = story.filter { it.id.equals(id) }
        if(filtered.isNotEmpty()) {
            val storyModel = DialogueItemViewModel(filtered[0])
            dialogueViewModel.stroy.postValue(storyModel)
            if(storyModel.image != ""){
                character_iv.setImageResource(getDrawable(storyModel.image))
            } else {
                character_iv.setImageResource(0)
            }

            if(storyModel.display != "") {
                game_background_iv.setImageResource(getDrawable(storyModel.display))
            }
        } else {
            // we need to make the game over scene here
        }
    }

    //taken from https://stackoverflow.com/questions/21856260/how-can-i-convert-string-to-drawable
    fun getDrawable(imgName: String): Int {
        val name = imgName
        val id = resources.getIdentifier(name, "drawable", packageName)
        return id
    }
}

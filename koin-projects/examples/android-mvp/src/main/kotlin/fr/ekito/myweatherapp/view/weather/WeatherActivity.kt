package fr.ekito.myweatherapp.view.weather

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import fr.ekito.myweatherapp.R
import fr.ekito.myweatherapp.domain.UserSession
import kotlinx.android.synthetic.main.activity_result.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.bindScope
import org.koin.android.scope.ext.android.createScope

/**
 * Weather Result View
 */
class WeatherActivity : AppCompatActivity() {

    val TAG = this::class.java.simpleName

    val userSession: UserSession by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Bind "session" scope to WeatherActivity lifecycle
        bindScope(createScope("session"))

        val weatherTitleFragment = WeatherHeaderFragment()
        val resultListFragment = WeatherListFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.weather_title, weatherTitleFragment)
            .replace(R.id.weather_list, resultListFragment)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        println("UserSession : $this got $userSession")
    }

    fun showError(error: Throwable) {
        Log.e(TAG, "error $error while displaying weather")
        weather_views.visibility = View.GONE
        weather_error.visibility = View.VISIBLE
        Snackbar.make(
            weather_result,
            "WeatherActivity got error : $error",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.retry) {
                startActivity(intentFor<WeatherActivity>().clearTop().clearTask().newTask())
            }
            .show()
    }
}

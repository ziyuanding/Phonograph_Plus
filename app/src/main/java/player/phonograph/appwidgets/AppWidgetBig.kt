package player.phonograph.appwidgets


import coil.target.Target
import mt.util.color.primaryTextColor
import player.phonograph.R
import player.phonograph.appwidgets.base.BaseAppWidget
import player.phonograph.model.Song
import player.phonograph.util.ui.getScreenSize
import androidx.core.graphics.drawable.toBitmapOrNull
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.RemoteViews


class AppWidgetBig : BaseAppWidget() {

    override val layoutId: Int get() = R.layout.app_widget_big

    override val name: String = NAME

    override val darkBackground: Boolean get() = true

    override fun updateText(context: Context, view: RemoteViews, song: Song) {
        if (TextUtils.isEmpty(song.title) && TextUtils.isEmpty(song.artistName)) {
            view.setViewVisibility(R.id.media_titles, View.INVISIBLE)
        } else {
            view.setViewVisibility(R.id.media_titles, View.VISIBLE)
            view.setTextViewText(R.id.title, song.title)
            view.setTextViewText(R.id.text, getSongArtistAndAlbum(song))
        }
    }

    /**
     * Update all active widget instances by pushing changes
     */
    override fun performUpdate(context: Context, isPlaying: Boolean, appWidgetIds: IntArray?) {
        isServiceStarted = true
        val color = context.primaryTextColor(true)
        val appWidgetView = RemoteViews(context.packageName, R.layout.app_widget_big)
        val song = queueManager.currentSong

        // Set the titles and artwork
        updateText(context, appWidgetView, song)

        appWidgetView.bindDrawable(context, R.id.button_toggle_play_pause, playPauseRes(isPlaying), color)

        // Link actions buttons to intents
        setupDefaultPhonographWidgetButtons(context, appWidgetView)
        setupLaunchingClick(context, appWidgetView)

        // Load the album cover async and push the update on completion
        val p = context.getScreenSize()
        val widgetImageSize = p.x.coerceAtMost(p.y)
        loadImage(
            context = context.applicationContext,
            song = song,
            widgetImageSize = widgetImageSize,
            target =
            object : Target {
                val mainHandler: Handler = Handler(Looper.getMainLooper())
                override fun onStart(placeholder: Drawable?) {
                    mainHandler.post { onUpdate(null) }
                }

                override fun onError(error: Drawable?) {
                    mainHandler.post { onUpdate(null) }
                }

                override fun onSuccess(result: Drawable) {
                    mainHandler.post { onUpdate(result.toBitmapOrNull()) }
                }

                private fun onUpdate(bitmap: Bitmap?) {
                    if (bitmap == null) {
                        appWidgetView.setImageViewResource(R.id.image, R.drawable.default_album_art)
                    } else {
                        appWidgetView.setImageViewBitmap(R.id.image, bitmap)
                    }
                    pushUpdate(context.applicationContext, appWidgetIds, appWidgetView)
                }
            }
        )


    }

    override fun setupLaunchingClick(context: Context, view: RemoteViews) {
        view.setOnClickPendingIntent(R.id.clickable_area, clickingPendingIntent(context))
    }

    companion object {
        const val NAME = "app_widget_big"
        private var mInstance: AppWidgetBig? = null

        @JvmStatic
        @get:Synchronized
        val instance: AppWidgetBig
            get() {
                if (mInstance == null) {
                    mInstance = AppWidgetBig()
                }
                return mInstance!!
            }
    }
}

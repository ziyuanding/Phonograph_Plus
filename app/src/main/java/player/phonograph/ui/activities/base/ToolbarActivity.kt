/*
 * Copyright (c) 2022 chr_56 & Abou Zeid (kabouzeid) (original author)
 */

package player.phonograph.ui.activities.base

import android.graphics.drawable.ColorDrawable
import android.view.Menu
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.WindowDecorActionBar
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.ToolbarWidgetWrapper
import util.mdcolor.pref.ThemeColor
import util.mddesign.util.MenuTinter

/**
 * An abstract class providing material activity with toolbar
 * @author Karim Abou Zeid (kabouzeid)
 */
abstract class ToolbarActivity : ThemeActivity() {

    private var toolbar: Toolbar? = null

    //
    // Toolbar & Actionbar
    //
    override fun setSupportActionBar(toolbar: Toolbar?) {
        this.toolbar = toolbar
        super.setSupportActionBar(toolbar)
    }

    protected fun getToolbar(): Toolbar? {
        return getSupportActionBarView(supportActionBar)
    }

    fun getToolbarBackgroundColor(toolbar: Toolbar?): Int {
        toolbar?.let {
            if (toolbar.background is ColorDrawable) return (toolbar.background as ColorDrawable).color
        }
        return 0
    }

    protected open fun getSupportActionBarView(ab: ActionBar?): Toolbar? {
        return if (ab == null || ab !is WindowDecorActionBar) null else try {
            var field = WindowDecorActionBar::class.java.getDeclaredField("mDecorToolbar")
            field.isAccessible = true
            val wrapper = field[ab] as ToolbarWidgetWrapper
            field = ToolbarWidgetWrapper::class.java.getDeclaredField("mToolbar")
            field.isAccessible = true
            field[wrapper] as Toolbar
        } catch (t: Throwable) {
            throw RuntimeException(
                "Failed to retrieve Toolbar from AppCompat support ActionBar: " + t.message,
                t
            )
        }
    }


    //
    // Menu (Tint)
    //
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        MenuTinter.setMenuColor(this, getToolbar(), menu!!, MaterialColor.White._1000.asColor) //todo
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        MenuTinter.applyOverflowMenuTint(this, getToolbar(), ThemeColor.accentColor(this))
        return super.onPrepareOptionsMenu(menu)
    }
}

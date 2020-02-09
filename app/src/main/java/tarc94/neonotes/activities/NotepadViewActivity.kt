package tarc94.neonotes.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_notepad_view.*
import tarc94.neonotes.R

class NotepadViewActivity : AppCompatActivity() {

    private var notepadID = 0
    private var notepadTitle: String? = null
    private var notepadDesc: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notepad_view)

        try {
            val bundle: Bundle? = this.intent.extras
            notepadID = bundle!!.getInt("ID")
            if (notepadID != 0) {
                notepadTitle = bundle.getString("Title")
                notepadDesc = bundle.getString("Description")
            }
        } catch (ex: Exception) {
        }

        updateView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notepad_view_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.Notepad_View_Edit) {
            val intent = Intent(this, NotepadAddActivity::class.java)
            intent.putExtra("ID", notepadID)
            intent.putExtra("Title", notepadTitle)
            intent.putExtra("Description", notepadDesc)
            intent.putExtra("FromView", true)
            startActivityForResult(intent, 2)
            return true
        } else if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2) {
            notepadID = data!!.getIntExtra("ID", 0)
            notepadTitle = data.getStringExtra("Title")
            notepadDesc = data.getStringExtra("Description")
        }

        updateView()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun updateView() {
        Notepad_View_Title.setContent(notepadTitle)
        Notepad_View_Description.setContent(notepadDesc)
    }
}

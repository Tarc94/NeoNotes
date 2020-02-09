package tarc94.neonotes.activities

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_notepad_add.*
import tarc94.neonotes.R.layout.activity_notepad_add
import tarc94.neonotes.helpers.DbManager

class NotepadAddActivity : AppCompatActivity() {

    private var notepadID = 0
    private var notepadTitle: String? = null
    private var notepadDesc: String? = null
    private var fromView: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_notepad_add)

        try {
            val bundle: Bundle? = this.intent.extras
            fromView = bundle!!.getBoolean("FromView")
            notepadID = bundle.getInt("ID")
            if (notepadID != 0) {
                notepadTitle = bundle.getString("Title")
                notepadDesc = bundle.getString("Description")
            }
        } catch (ex: Exception) {
        }

        Notepad_Add_Title.setText(notepadTitle)
        Notepad_Add_Description.setText(notepadDesc)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure?")
        builder.setMessage("Any changes you have made will be lost. Are you sure you want to continue?")
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int ->
            val intent = Intent()
            intent.putExtra("ID", notepadID)
            intent.putExtra("Title", notepadTitle)
            intent.putExtra("Description", notepadDesc)
            setResult(2, intent)
            finish()
        }
        builder.setNegativeButton("No") { _: DialogInterface, _: Int -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        return
    }

    fun addNotepad(view: View) {
        val dbManager = DbManager(this)

        val values = ContentValues()
        values.put("Title", Notepad_Add_Title.text.toString())
        values.put("Description", Notepad_Add_Description.text.toString())

        if (notepadID == 0) {
            val id = dbManager.insert(values)
            if (id > 0) {
                Toast.makeText(this, "Notepad has been added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error: Notepad could not added", Toast.LENGTH_SHORT).show()
            }
        } else {
            val selectionArgs = arrayOf(notepadID.toString())
            val id = dbManager.update(values, "ID=?", selectionArgs)
            if (id > 0) {
                if (fromView != null && fromView == true) {
                    val intent = Intent()
                    intent.putExtra("ID", notepadID)
                    intent.putExtra("Title", Notepad_Add_Title.text.toString())
                    intent.putExtra("Description", Notepad_Add_Description.text.toString())
                    setResult(2, intent)
                }
                Toast.makeText(this, "Notepad has been updated", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error: Notepad could not updated", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

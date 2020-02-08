package neority.privacynotes

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_notepad_add.*

class NotepadAddActivity : AppCompatActivity() {

    private var notepadID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notepad_add)

        try{
            val bundle: Bundle? = this.intent.extras
            notepadID = bundle!!.getInt("ID")
            if(notepadID != 0){
                Notepad_Add_Title.setText(bundle.getString("Title"))
                Notepad_Add_Description.setText(bundle.getString("Description"))
            }
        }catch(ex:Exception){}
    }

    fun addNotepad(view: View) {
        val dbManager = DbManager(this)

        val values = ContentValues()
        values.put("Title", Notepad_Add_Title.text.toString())
        values.put("Description", Notepad_Add_Description.text.toString())

        if(notepadID == 0){
            val id = dbManager.insert(values)
            if(id > 0){
                Toast.makeText(this, "Notepad has been added", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Error: Notepad could not added", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            val selectionArgs = arrayOf(notepadID.toString())
            val id = dbManager.update(values, "ID=?", selectionArgs)
            if(id > 0){
                Toast.makeText(this, "Notepad has been updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Error: Notepad could not updated", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

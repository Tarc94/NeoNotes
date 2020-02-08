package tarc94.neonotes

import android.app.SearchManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.notepadtile.view.*
import tarc94.neonotes.R.layout

class MainActivity : AppCompatActivity() {

    private var listNotepad = ArrayList<Notepad>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        loadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }

    private fun loadQuery(title: String) {
        val dbManager = DbManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projections, "Title like ?", selectionArgs, "Title")
        listNotepad.clear()
        if (cursor.moveToFirst()) {
            do {
                val notepadID = cursor.getInt(cursor.getColumnIndex("ID"))
                val notepadName = cursor.getString(cursor.getColumnIndex("Title"))
                val notepadDes = cursor.getString(cursor.getColumnIndex("Description"))

                listNotepad.add(Notepad(notepadID, notepadName, notepadDes))
            } while (cursor.moveToNext())
        }

        val myNotepadAdapter = MyNotepadAdapter(this, listNotepad)
        this.Notepad_AllView_List.adapter = myNotepadAdapter

        val total = this.Notepad_AllView_List.count
        val mActionBar = supportActionBar
        if (mActionBar != null) {
            mActionBar.subtitle = "You have $total notepad(s)"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.notepad_list_menu, menu)

        val searchView = menu!!.findItem(R.id.Notepad_Menu_Search).actionView as SearchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQuery("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadQuery("%$newText%")
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.Notepad_Menu_Add) {
            startActivity(Intent(this, NotepadAddActivity::class.java))
        } else if (item.itemId == R.id.Notepad_Menu_Settings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

    inner class MyNotepadAdapter(context: Context, listNotepad: ArrayList<Notepad>) :
        BaseAdapter() {

        private var context: Context? = context
        private var listNotepadAdapter = listNotepad

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val tileView = layoutInflater.inflate(layout.notepadtile, null)
            val notepad = listNotepadAdapter[position]
            tileView.Notepad_Tile_Title.text = notepad.notepadName
            tileView.Notepad_Tile_Description.text = notepad.notepadDes

            tileView.Notepad_Tile_Delete_Button.setOnClickListener {
                val dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(notepad.notepadID.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadQuery("%")
            }

            tileView.Notepad_Tile_Edit_Button.setOnClickListener {
                goToUpdate(notepad)
            }

            tileView.Notepad_Tile_Copy_Button.setOnClickListener {
                val title = notepad.notepadName
                val description = notepad.notepadDes
                val copyData = title + "\n" + description

                val clipBoard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipBoard.setPrimaryClip(ClipData.newPlainText("Notepad", copyData))
                Toast.makeText(this@MainActivity, "Copied: $copyData", Toast.LENGTH_SHORT).show()
            }

            tileView.Notepad_Tile_Share_Button.setOnClickListener {
                val title = notepad.notepadName
                val description = notepad.notepadDes
                val copyData = title + "\n" + description

                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, copyData)
                startActivity(Intent.createChooser(shareIntent, copyData))
            }

            return tileView
        }

        override fun getItem(position: Int): Any {
            return listNotepadAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotepadAdapter.size
        }
    }

    private fun goToUpdate(notepad: Notepad) {
        val intent = Intent(this, NotepadAddActivity::class.java)
        intent.putExtra("ID", notepad.notepadID)
        intent.putExtra("Title", notepad.notepadName)
        intent.putExtra("Description", notepad.notepadDes)
        startActivity(intent)
    }
}



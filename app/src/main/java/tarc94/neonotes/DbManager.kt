package tarc94.neonotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager(context: Context) {

    //database name
    val dbName = "MyNotepads.db"
    //table name
    val dbTable = "Notepads"
    //columns
    private val colID = "ID"
    private val colTitle = "Title"
    private val colDes = "Description"
    //database version
    var dbVersion = 1

    private var sqlDB: SQLiteDatabase? = null

    val sqlCreateTable: String =
        ("CREATE TABLE IF NOT EXISTS $dbTable ($colID INTEGER PRIMARY KEY,$colTitle TEXT,$colDes TEXT)")

    init {
        val db = DatabaseHelperNotepad(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotepad(context: Context) :
        SQLiteOpenHelper(context, dbName, null, dbVersion) {

        private var context: Context? = context

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "Database created...", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS $dbTable")
            onCreate(db)
        }
    }

    fun insert(values: ContentValues): Long {
        return sqlDB!!.insert(dbTable, "", values)
    }

    fun query(
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sorOrder: String
    ): Cursor {
        val database = SQLiteQueryBuilder()
        database.tables = dbTable
        return database.query(sqlDB, projection, selection, selectionArgs, null, null, sorOrder)
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        return sqlDB!!.delete(dbTable, selection, selectionArgs)
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        return sqlDB!!.update(dbTable, values, selection, selectionArgs)
    }
}
package com.example.libraryapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BookDatabase"
        private const val TABLE_BOOKS = "books"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_AUTHOR = "author"
        private const val KEY_GENRE = "genre"
        private const val KEY_YEAR = "year"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_BOOKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
                + KEY_AUTHOR + " TEXT," + KEY_GENRE + " TEXT," + KEY_YEAR + " INTEGER" + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        onCreate(db)
    }

    fun addBook(book: Book): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, book.title)
        contentValues.put(KEY_AUTHOR, book.author)
        contentValues.put(KEY_GENRE, book.genre)
        contentValues.put(KEY_YEAR, book.year)
        val success = db.insert(TABLE_BOOKS, null, contentValues)
        db.close()
        return success
    }

    fun getBook(id: Long): Book? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_BOOKS,
            arrayOf(KEY_ID, KEY_TITLE, KEY_AUTHOR, KEY_GENRE, KEY_YEAR),
            "$KEY_ID=?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )
        var book: Book? = null
        if (cursor != null && cursor.moveToFirst()) {
            book = Book(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                author = cursor.getString(cursor.getColumnIndexOrThrow(KEY_AUTHOR)),
                genre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_GENRE)),
                year = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_YEAR))
            )
            cursor.close()
        }
        db.close()
        return book
    }

    fun getAllBooks(): List<Book> {
        val bookList = ArrayList<Book>()
        val selectQuery = "SELECT  * FROM $TABLE_BOOKS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val book = Book(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                    author = cursor.getString(cursor.getColumnIndexOrThrow(KEY_AUTHOR)),
                    genre = cursor.getString(cursor.getColumnIndexOrThrow(KEY_GENRE)),
                    year = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_YEAR))
                )
                bookList.add(book)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return bookList
    }

    fun updateBook(book: Book): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, book.title)
        contentValues.put(KEY_AUTHOR, book.author)
        contentValues.put(KEY_GENRE, book.genre)
        contentValues.put(KEY_YEAR, book.year)
        val success = db.update(TABLE_BOOKS, contentValues, "id=" + book.id, null)
        db.close()
        return success
    }

    fun deleteBook(book: Book): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_BOOKS, "id=" + book.id, null)
        db.close()
        return success
    }
}

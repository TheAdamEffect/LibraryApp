package com.example.libraryapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private lateinit var db: DatabaseHelper
    private val booksState = mutableStateOf<List<Book>>(emptyList())

    override fun onResume() {
        super.onResume()
        booksState.value = db.getAllBooks()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHelper(this)
        booksState.value = db.getAllBooks()
        setContent {
            BookListScreen(
                books = booksState.value,
                onDelete = { book ->
                    db.deleteBook(book)
                    booksState.value = db.getAllBooks()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(books: List<Book>, onDelete: (Book) -> Unit) {
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Navigate to Add/Edit Screen
                val intent = Intent(context, AddEditBookActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Book")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            items(books) { book ->
                BookItem(
                    book = book,
                    onDelete = { onDelete(book) },
                    onEdit = {
                        val intent = Intent(context, AddEditBookActivity::class.java)
                        intent.putExtra("BOOK_ID", book.id)
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun BookItem(book: Book, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${book.title} by ${book.author}")
        Row {
            IconButton(onClick = { onEdit() }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit Book")
            }
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Book")
            }
        }
    }
}

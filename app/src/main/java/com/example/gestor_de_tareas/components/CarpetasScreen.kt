package com.example.gestor_de_tareas.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.gestor_de_tareas.models.Folder
import com.example.gestor_de_tareas.navigation.Screen
import com.example.gestor_de_tareas.viewModel.FoldersViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

/*@Composable
fun CarpetasScreen(navController: NavController, viewModel: FoldersViewModel = viewModel()) {
    val folders by viewModel.folders.observeAsState(initial = emptyList())

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(folders) { folder ->
            FolderItem(
                folder = folder,
                onFolderClick = { navController.navigate(Screen.FileList.createRoute(folder.id)) }
            )
        }
    }

    FloatingActionButton(onClick = { /* Mostrar diálogo para añadir carpeta */ }) {
        Icon(Icons.Default.Add, contentDescription = "Añadir carpeta")
    }
}*/
@Composable
fun CarpetasScreen(
    navController: NavController,
    viewModel: FoldersViewModel = hiltViewModel()
) {
    val folders by viewModel.folders.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Show add folder dialog */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Folder")
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = paddingValues
        ) {
            items(folders) { folder ->
                FolderItem(
                    folder = folder,
                    onClick = { navController.navigate("files/${folder.id}") }
                )
            }
        }
    }
}

@Composable
fun FolderItem(folder: Folder, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(folder.imagePath),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Text(text = folder.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = folder.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}
package com.example.myapplication.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.myapplication.model.PictureBean
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.MainViewModel

@Composable
fun SearchScreen(modifier: Modifier = Modifier, mainViewModel: MainViewModel = MainViewModel()) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .background(Color.LightGray)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally // Pour centrer horizontalement les éléments
    ) {
        println("SearchScreen()")
        TextField(
            value = searchText, // Valeur affichée
            onValueChange = { newValue -> searchText = newValue }, // Nouveau texte entré
            leadingIcon = { // Image d'icône
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            },
            singleLine = true,
            label = { // Texte d'aide qui se déplace
                Text("Votre recherche ici")
            },
            //placeholder = { // Texte d'aide qui disparaît
            //Text("Recherche")
            //},
            modifier = Modifier
                .fillMaxWidth() // Prendre toute la largeur
                .heightIn(min = 56.dp) // Hauteur minimum
                .padding(8.dp) // Ajouter un peu d'espace autour du champ de texte
        )
        Spacer(Modifier.size(8.dp))

        // Observation
        val list = mainViewModel.dataList.collectAsStateWithLifecycle().value

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(list) { item ->
                PictureRowItem(data = item)
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Pousse les boutons vers le bas

        // Row pour les boutons
        Row(
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* Action pour Clear filter */ },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(4.dp)) // Espace entre l'icône et le texte
                Text("Clear filter")
            }

            Button(
                onClick = { /* Action pour Load data */ },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(4.dp)) // Espace entre l'icône et le texte
                Text("Load data")
            }
        }
    }
}

        @OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PictureRowItem(modifier: Modifier = Modifier, data: PictureBean) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Affichage de l'image avec Glide
        GlideImage(
            model = data.url,
            contentDescription = "Image de ${data.title}",
            loading = placeholder(android.R.drawable.stat_sys_download),
            failure = placeholder(android.R.drawable.stat_notify_error),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 8.dp)
        )

        // Affichage du titre et de la description
        Column {
            Text(text = data.title, fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = data.longText,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    MyApplicationTheme {

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val viewmodel = MainViewModel()
            viewmodel.loadFakeData(true, "une erreur")
            SearchScreen(modifier = Modifier.padding(innerPadding), mainViewModel = viewmodel)
        }
    }
}
package com.example.myapplication.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.myapplication.R
import com.example.myapplication.model.PictureBean
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.MainViewModel

@Composable
fun SearchScreen(modifier: Modifier = Modifier, mainViewModel: MainViewModel = MainViewModel()) {

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        val searchText = remember { mutableStateOf("") }

        SearchBar(searchText = searchText)

        // Observation
        val list = mainViewModel.dataList.collectAsStateWithLifecycle().value.filter { it.title.contains(searchText.value, true) }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(5f)) {
            items(list.size) {
                PictureRowItem(data = list[it])
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Pousse les boutons vers le bas

        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    searchText.value = ""
                },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text("${stringResource(R.string.clear_filter)} Clear filter")
            }

            Button(
                onClick = { mainViewModel.loadData() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    tint = MaterialTheme.colorScheme.surface,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text("Load data")
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, searchText: MutableState<String>) {

    TextField(
        value = searchText.value, //Valeur affichée
        onValueChange = {newValue:String -> searchText.value = newValue }, //Nouveau texte entrée
        leadingIcon = { //Image d'icone
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        singleLine = true,
        label = { //Texte d'aide qui se déplace
            Text("Enter text")
            //Pour aller le chercher dans string.xml
            //Text(stringResource(R.string.placeholder_search))
        },
        //placeholder = { //Texte d'aide qui disparait
        //Text("Recherche")
        //},

        //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // Définir le bouton "Entrée" comme action de recherche
        //keyboardActions = KeyboardActions(onSearch = {onSearchAction()}), // Déclenche l'action définie
        //Comment le composant doit se placer
        modifier = modifier
            .fillMaxWidth() // Prend toute la largeur
            .heightIn(min = 56.dp) //Hauteur minimum
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable //Composable affichant 1 PictureBean
fun PictureRowItem(modifier: Modifier = Modifier, data: PictureBean) {

    var expended by remember { mutableStateOf(false) }

    Row(modifier = modifier
        .background(MaterialTheme.colorScheme.tertiaryContainer)
        .fillMaxWidth()) {

        //Permission Internet nécessaire
        GlideImage(
            model = data.url,
            //Pour aller le chercher dans string.xml
            //contentDescription = getString(R.string.picture_of_cat),
            //En dur
            contentDescription = "une photo de chat",
            // Image d'attente. Permet également de voir l'emplacement de l'image dans la Preview
            loading = placeholder(R.mipmap.ic_launcher_round),
            // Image d'échec de chargement
            failure = placeholder(R.mipmap.ic_launcher),
            contentScale = ContentScale.Fit,
            //même autres champs qu'une Image classique
            modifier = Modifier
                .heightIn(max = 100.dp) //Sans hauteur il prendra tous l'écran
                .widthIn(max = 100.dp)
        )



        Column(modifier = Modifier.padding(4.dp).fillMaxWidth().clickable {
            expended = !expended
        }) {


            Text(text = data.title, style = MaterialTheme.typography.titleLarge)

            val text = if(expended) data.longText else (data.longText.take(20) + "...")

            Text(text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.animateContentSize()
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES, locale = "fr")
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
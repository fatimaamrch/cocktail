package com.example.myapplication.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.myapplication.R
import com.example.myapplication.model.CocktailBean
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.MainViewModel

@Composable
fun SearchScreen(modifier: Modifier = Modifier, mainViewModel: MainViewModel = MainViewModel()) {
    Column(modifier= modifier.fillMaxSize()) {

        // Observation
        val list = mainViewModel.dataList.collectAsStateWithLifecycle().value

        list.forEach {
            CocktailRowItem(data = it)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable // Composable affichant 1 CocktailBean
fun CocktailRowItem(modifier: Modifier = Modifier, data: CocktailBean) {
    Row(modifier = modifier.background(MaterialTheme.colorScheme.tertiaryContainer).fillMaxWidth()) {

        // Permission Internet nécessaire
        GlideImage(
            model = data.image,
            contentDescription = "Image du cocktail ${data.title}",
            // Image d'attente. Permet également de voir l'emplacement de l'image dans la Preview
            loading = placeholder(R.mipmap.ic_launcher_round),
            // Image d'échec de chargement
            failure = placeholder(R.mipmap.ic_launcher),
            contentScale = ContentScale.Fit,
            modifier = Modifier.heightIn(max = 100.dp).widthIn(max = 100.dp)
        )

        Column(modifier = Modifier.padding(4.dp)) {
            Text(text = data.title, style = MaterialTheme.typography.titleLarge)
            Text(text = "Difficulté: ${data.difficulty}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onTertiaryContainer)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenPreview() {
    MyApplicationTheme {

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val viewModel = MainViewModel()
            viewModel.loadFakeData(true, "une erreur") // Utilisation de fake data pour la preview
            SearchScreen(modifier = Modifier.padding(innerPadding), mainViewModel = viewModel)
        }
    }
}
package com.example.myapplication.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.MainViewModel

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DetailScreenPreview() {
    MyApplicationTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DetailScreen(
                modifier = Modifier.padding(innerPadding),
                cocktailId = 1 // Ajoutez ce paramètre
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel(),
    navHostController: NavHostController? = null,
    cocktailId: Int
) {
    val cocktailDetails = mainViewModel.selectedCocktailDetails.collectAsState().value

    LaunchedEffect(cocktailId) {
        mainViewModel.loadCocktailDetails(cocktailId)
    }

    cocktailDetails?.let {
        Column(modifier = modifier.padding(8.dp)) {
            Text(
                text = it.title,
                fontSize = 36.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(16.dp))

            GlideImage(
                model = it.image,
                contentDescription = "Image du cocktail",
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )

            Spacer(Modifier.size(16.dp))

            Text(text = it.description, style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.size(16.dp))

            Text(text = "Portion: ${it.portion}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Time: ${it.time}", style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.size(16.dp))

            Text(text = "Ingrédients:", style = MaterialTheme.typography.bodyLarge)
            it.ingredients.forEach { ingredient ->
                Text(text = ingredient, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.size(16.dp))

            Text(text = "Méthode:", style = MaterialTheme.typography.bodyLarge)
            it.method.forEach { step ->
                Text(text = step.step, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.size(16.dp))

            Button(
                onClick = { navHostController?.popBackStack() },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier.align(CenterHorizontally)
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Retour",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Retour")
            }
        }
    }
}
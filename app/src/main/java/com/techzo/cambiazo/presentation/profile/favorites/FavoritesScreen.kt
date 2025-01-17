package com.techzo.cambiazo.presentation.profile.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.techzo.cambiazo.common.components.ButtonIconHeaderApp
import com.techzo.cambiazo.common.components.DialogApp
import com.techzo.cambiazo.common.components.EmptyStateMessage
import com.techzo.cambiazo.common.components.LoadingMessage
import com.techzo.cambiazo.common.components.MainScaffoldApp
import com.techzo.cambiazo.common.components.Products
import com.techzo.cambiazo.common.components.TextTitleHeaderApp
import com.techzo.cambiazo.presentation.exchanges.ExchangeBox

@Composable
fun FavoritesScreen(
    back: () -> Unit = {},
    onProductClick: (String, String) -> Unit,
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
) {
    val state = favoritesViewModel.favoriteProducts.value
    val productToRemove = favoritesViewModel.productToRemove.value


    MainScaffoldApp(
        paddingCard = PaddingValues(top = 15.dp),
        contentsHeader = {
            Column(
                Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ButtonIconHeaderApp(Icons.Filled.ArrowBack, onClick = { back() })
                TextTitleHeaderApp("Favoritos")
            }
        },
        content = {
            Column(modifier = Modifier.padding(horizontal = 0.dp)) {
                if (state.isLoading) {
                    LoadingMessage()
                } else if (state.data.isNullOrEmpty()) {
                    EmptyStateMessage(
                        icon = Icons.Default.Info,
                        message = "Sin favoritos aún",
                        subMessage = "Explora y guarda lo que te guste aquí.",
                        modifier = Modifier.padding(20.dp)
                    )
                } else {
                    LazyColumn {
                        items(state.data!!.reversed()) { product ->
                            Products(
                                product = product,
                                icon = Icons.Filled.Favorite,
                                onClickIcon = { favoritesViewModel.confirmRemoveProduct(product) },
                                onProductClick = onProductClick
                            )
                        }
                        item { Spacer(modifier = Modifier.padding(15.dp)) }
                    }
                }
            }
        }
    )

    productToRemove?.let { product ->
        DialogApp(
            message = "Confirmación",
            description = "¿Está seguro de que desea eliminar este producto de sus favoritos?",
            labelButton1 = "Aceptar",
            labelButton2 = "Cancelar",
            onDismissRequest = { favoritesViewModel.cancelRemoveProduct() },
            onClickButton1 = {
                favoritesViewModel.removeProductFromFavorites(product.id)
                favoritesViewModel.cancelRemoveProduct()
            },
            onClickButton2 = { favoritesViewModel.cancelRemoveProduct() }
        )
    }
}

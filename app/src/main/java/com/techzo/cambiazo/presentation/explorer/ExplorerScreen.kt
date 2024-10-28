package com.techzo.cambiazo.presentation.explorer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.skydoves.landscapist.glide.GlideImage
import com.techzo.cambiazo.common.components.MainScaffoldApp
import com.techzo.cambiazo.common.components.Products
import com.techzo.cambiazo.domain.Product

@Composable
fun ExplorerScreen(
    viewModel: ExplorerListViewModel = hiltViewModel(),
    bottomBar: @Composable () -> Unit = {},
    onFilter: () -> Unit = {},
    onProductClick: (String, String) -> Unit
) {
    val searcher = viewModel.name.value
    val categories = viewModel.productCategories.value
    val state = viewModel.state.value

    val boostProducts = state.data?.filter { it.available && it.boost } ?: emptyList()
    val availableProducts = state.data?.filter { it.available && !it.boost } ?: emptyList()

    val isRefreshing = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val rowState = rememberLazyListState()

    fun refreshData() {
        isRefreshing.value = true
        viewModel.loadProducts()
    }

    LaunchedEffect(isRefreshing.value) {
        if (isRefreshing.value) {
            listState.scrollToItem(0)
            rowState.scrollToItem(0)
            isRefreshing.value = false
        }
    }


    LaunchedEffect(viewModel.categoryId.value) {
        listState.scrollToItem(0)
        rowState.scrollToItem(0)
    }

    MainScaffoldApp(
        bottomBar = bottomBar,
        paddingCard = PaddingValues(top = 5.dp),
        contentsHeader = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
            ) {
                BasicTextField(
                    value = searcher,
                    onValueChange = { viewModel.onNameChanged(it) },
                    modifier = Modifier
                        .shadow(10.dp, RoundedCornerShape(10.dp))
                        .weight(1f)
                        .height(50.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(9.dp, Color.Transparent, RoundedCornerShape(8.dp)),
                    singleLine = true,
                    maxLines = 1,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                tint = Color.Gray,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (searcher.isEmpty()) {
                                    Text(
                                        "Buscar",
                                        color = Color.Gray,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    }
                )
                IconButton(onClick = { onFilter() },
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .background(Color.Black, RoundedCornerShape(8.dp))
                        .shadow(10.dp, RoundedCornerShape(10.dp))
                        .size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = "Filtro",
                        tint = Color.White
                    )
                }
            }
        }
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp)
        ) {
            items(categories.data ?: emptyList()) { category ->
                val isSelected = viewModel.categoryId.value == category.id

                Box(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .height(35.dp)
                        .background(
                            if (isSelected) Color(0xFFFFD146)
                            else Color.White,
                            RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { viewModel.onProductCategorySelected(category.id) }
                        .border(1.dp, Color(0xFFFFD146), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 25.dp),
                        text = category.name,
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing.value),
            onRefresh = { refreshData() },
            refreshTriggerDistance = 35.dp,
            indicator = { state, refreshTrigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = refreshTrigger,
                    contentColor = Color(0xFFFFD146),
                    backgroundColor = Color.Black.copy(alpha = 0.5f)
                )
            }
        ) {
            LazyColumn(state = listState) {
                if (boostProducts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Productos destacados",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .padding(bottom = 10.dp)
                        )
                        LazyRow(state = rowState) {
                            item { Spacer(modifier = Modifier.width(20.dp)) }
                            items(boostProducts.reversed()) { product ->
                                ProductsRow(product, onProductClick)
                            }
                            item { Spacer(modifier = Modifier.width(10.dp)) }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                if (availableProducts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Productos Recientes",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                        )
                    }
                    items(availableProducts.reversed()) { product ->
                        Products(product, onProductClick)
                    }
                    item { Spacer(modifier = Modifier.height(15.dp)) }
                }
            }
        }
    }
}

@Composable
fun ProductsRow(product: Product, onProductClick: (String, String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(380.dp)
            .padding(end = 10.dp)
            .border(0.dp, Color.Transparent, RoundedCornerShape(15.dp))
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(15.dp))
            .clickable { onProductClick(product.id.toString(), product.user.id.toString()) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            GlideImage(
                imageModel = { product.image },
                modifier = Modifier.fillMaxSize(),
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(35.dp)
                    .background(
                        Color.Black.copy(alpha = 0.6f),
                        RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ElectricBolt,
                    contentDescription = null,
                    tint = Color(0xFFFFD146)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .padding(bottom = 70.dp)
                    .background(
                        Color.Black.copy(alpha = 0.4f), RoundedCornerShape(13.dp)
                    )
                    .padding(horizontal = 14.dp)
                    .padding(vertical = 4.dp),
            ) {
                Text(
                    text = "S/${product.price} aprox.",
                    color = Color(0xFFFFD146),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 15.dp)
            ) {
                Column {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Ubicación",
                            tint = Color(0xFFFFD146),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "${product.location.districtName}, ${product.location.departmentName}",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
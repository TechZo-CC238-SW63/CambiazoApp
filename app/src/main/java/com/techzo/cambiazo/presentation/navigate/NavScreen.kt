package com.techzo.cambiazo.presentation.navigate

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.techzo.cambiazo.presentation.articles.ArticlesScreen
import com.techzo.cambiazo.presentation.articles.publish.PublishScreen
import com.techzo.cambiazo.presentation.explorer.productdetails.ProductDetailsScreen
import com.techzo.cambiazo.presentation.donations.DonationsScreen
import com.techzo.cambiazo.presentation.exchanges.exchangedetails.ExchangeDetailsScreen
import com.techzo.cambiazo.presentation.exchanges.ExchangeScreen
import com.techzo.cambiazo.presentation.explorer.ExplorerListViewModel
import com.techzo.cambiazo.presentation.explorer.ExplorerScreen
import com.techzo.cambiazo.presentation.explorer.filter.FilterScreen
import com.techzo.cambiazo.presentation.auth.login.SignInScreen
import com.techzo.cambiazo.presentation.explorer.offer.ConfirmationOfferScreen
import com.techzo.cambiazo.presentation.explorer.offer.MakeOfferScreen
import com.techzo.cambiazo.presentation.explorer.offer.OfferViewModel
import com.techzo.cambiazo.presentation.profile.ProfileScreen
import com.techzo.cambiazo.presentation.profile.editprofile.EditProfileScreen
import com.techzo.cambiazo.presentation.profile.favorites.FavoritesScreen
import com.techzo.cambiazo.presentation.profile.myreviews.MyReviewsScreen
import com.techzo.cambiazo.presentation.auth.register.SignUpScreen
import com.techzo.cambiazo.presentation.auth.register.TyC.TermsAndConditionsScreen
import com.techzo.cambiazo.presentation.explorer.review.ReviewScreen

sealed class ItemsScreens(val icon: ImageVector,val iconSelected: ImageVector, val title: String,val route: String, val navigate: () -> Unit = {}) {
    data class Explorer(val onNavigate: () -> Unit = {}) : ItemsScreens(
        iconSelected = Icons.Outlined.Search,
        icon = Icons.Filled.Search,
        title = "Explorar",
        navigate = onNavigate,
        route = Routes.Explorer.route

    )

    data class Exchange(val onNavigate: () -> Unit = {}) : ItemsScreens(
        iconSelected = Icons.Filled.SwapHoriz,
        icon = Icons.Outlined.SwapHoriz,
        title = "Intercambios",
        navigate = onNavigate,
        route = Routes.Exchange.route
    )

    data class Articles(val onNavigate: () -> Unit = {}) : ItemsScreens(
        iconSelected = Icons.Filled.LocalOffer,
        icon = Icons.Outlined.LocalOffer,
        title = "Mis Artículos",
        navigate = onNavigate,
        route = Routes.Article.route
    )

    data class Donation(val onNavigate: () -> Unit = {}) : ItemsScreens(
        iconSelected = Icons.Filled.Handshake,
        icon = Icons.Outlined.Handshake,
        title = "Donaciones",
        navigate = onNavigate,
        route = Routes.Donation.route
    )

    data class Profile(val onNavigate: () -> Unit = {}) : ItemsScreens(
        iconSelected = Icons.Filled.Person,
        icon = Icons.Outlined.Person,
        title = "Perfil",
        navigate = onNavigate,
        route = Routes.Profile.route
    )
}

sealed class Routes(val route: String) {
    object SignUp : Routes("SignUpScreen")
    object SignIn : Routes("SignInScreen")
    object Filter : Routes("FilterScreen")
    object Explorer : Routes("ExplorerScreen")
    object Article : Routes("ArticleScreen")
    object Donation : Routes("DonationScreen")
    object Profile : Routes("ProfileScreen")
    object Exchange : Routes("ExchangeScreen")
    object TermsAndConditions : Routes("TermsAndConditionsScreen")
    object ExchangeDetails : Routes("ExchangeDetailsScreen/{exchangeId}/{page}") {
        fun createExchangeDetailsRoute(exchangeId: String, page: String) = "ExchangeDetailsScreen/$exchangeId/$page"
    }
    object ProductDetails : Routes("ProductDetailsScreen/{productId}/{userId}") {
        fun createProductDetailsRoute(productId: String, userId: String) = "ProductDetailsScreen/$productId/$userId"
    }
    object Reviews : Routes("ReviewsScreen/{userId}") {
        fun createRoute(userId: String) = "ReviewsScreen/$userId"
    }

    object MakeOffer : Routes("MakeOfferScreen/{desiredProductId}/{offeredProductIds}") {
        fun createMakeOfferRoute(desiredProductId: String, offeredProductIds: List<String>) =
            "MakeOfferScreen/$desiredProductId/${offeredProductIds.joinToString(",")}"
    }

    object ConfirmationOffer : Routes("ConfirmationOfferScreen/{desiredProductId}/{offeredProductId}") {
        fun createConfirmationOfferRoute(desiredProductId: String, offeredProductId: String) =
            "ConfirmationOfferScreen/$desiredProductId/$offeredProductId"
    }

    object EditProfile : Routes("EditProfileScreen")
    object MyReviews : Routes("MyReviewsScreen")
    object Publish : Routes("PublishScreen")
    object Favorites : Routes("FavoritesScreen")
}

@Composable
fun NavScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?:""

    val items = listOf(
        ItemsScreens.Explorer(onNavigate = { navController.navigate(Routes.Explorer.route) }),
        ItemsScreens.Exchange(onNavigate = { navController.navigate(Routes.Exchange.route) }),
        ItemsScreens.Articles(onNavigate = { navController.navigate(Routes.Article.route) }),
        ItemsScreens.Donation(onNavigate = { navController.navigate(Routes.Donation.route) }),
        ItemsScreens.Profile(onNavigate = { navController.navigate(Routes.Profile.route) })
    )

    NavHost(navController = navController, startDestination = Routes.SignIn.route) {
        composable(route = Routes.SignUp.route) {
            SignUpScreen(
                back = { navController.popBackStack() },
                openLogin = { navController.navigate(Routes.SignIn.route) },
                navigateToTermsAndConditions = { navController.navigate(Routes.TermsAndConditions.route) }
            )
        }

        composable(route = Routes.SignIn.route) {
            SignInScreen(
                openRegister = { navController.navigate(Routes.SignUp.route) },
                openApp = { navController.navigate(Routes.Explorer.route) }
            )
        }

        composable(route = Routes.Explorer.route) {
            ExplorerScreen(
                bottomBar = { BottomBarNavigation(items,currentRoute) },
                onFilter = { navController.navigate(Routes.Filter.route) },
                onProductClick = { productId, userId ->
                    navController.navigate(
                        Routes.ProductDetails.createProductDetailsRoute(
                            productId.toString(),
                            userId.toString()
                        )
                    )
                }
            )
        }

        composable(route = Routes.Filter.route) {
            FilterScreen(
                back = { navController.popBackStack() },
                openExplorer = { navController.navigate(Routes.Explorer.route) }
            )
        }

        composable(route = Routes.Exchange.route) {
            ExchangeScreen(
                bottomBar = { BottomBarNavigation(items,currentRoute) },
                goToDetailsScreen = { exchangeId, page ->
                    navController.navigate(
                        Routes.ExchangeDetails.createExchangeDetailsRoute(
                            exchangeId,
                            page
                        )
                    )
                }
            )
        }

        composable(route = Routes.Article.route) {
            ArticlesScreen(
                bottomBar = { BottomBarNavigation(items,currentRoute) },
                onPublish = {navController.navigate(Routes.Publish.route)},
                onProductClick = { productId, userId ->
                    navController.navigate(
                        Routes.ProductDetails.createProductDetailsRoute(
                            productId.toString(),
                            userId.toString()
                        )
                    )
                }

            )
        }

        composable(route = Routes.ExchangeDetails.route) { backStackEntry ->
            val exchange = backStackEntry.arguments?.getString("exchangeId")?.toIntOrNull()
            val page = backStackEntry.arguments?.getString("page")?.toIntOrNull()
            if (exchange != null && page != null) {
                ExchangeDetailsScreen(
                    goBack = { navController.popBackStack() },
                    exchangeId = exchange,
                    page = page
                )
            }
        }

        composable(route = Routes.Profile.route) {
            ProfileScreen(
                logOut = {
                    navController.navigate(Routes.SignIn.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                openMyReviews = { navController.navigate(Routes.MyReviews.route) },
                openEditProfile = { navController.navigate(Routes.EditProfile.route) },
                openFavorites = { navController.navigate(Routes.Favorites.route) },
                bottomBar = { BottomBarNavigation(items,currentRoute) }
            )
        }

        composable(route = Routes.Donation.route) {
            DonationsScreen(
                bottomBar = { BottomBarNavigation(items,currentRoute) }
            )
        }

        composable(route = Routes.TermsAndConditions.route) {
            TermsAndConditionsScreen(back = { navController.popBackStack() })
        }

        composable(route = Routes.MyReviews.route) {
            MyReviewsScreen(
                back = { navController.popBackStack() },
                OnUserClick = { userId ->
                    navController.navigate(Routes.Reviews.createRoute(userId.toString()))
                }
            )
        }
        composable(route = Routes.EditProfile.route) {
            EditProfileScreen(
                back = { navController.popBackStack() }
            )
        }

        composable(route = Routes.Favorites.route) {
            FavoritesScreen(
                back = { navController.popBackStack() },
                onProductClick = { productId, userId ->
                    navController.navigate(
                        Routes.ProductDetails.createProductDetailsRoute(
                            productId,
                            userId
                        )
                    )
                }
            )
        }

        composable(route = Routes.Reviews.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            if (userId != null) {
                ReviewScreen(
                    userId = userId,
                    onBack = { navController.popBackStack() },
                    onProductClick = { productId,productUserid ->
                        navController.navigate(
                            Routes.ProductDetails.createProductDetailsRoute(
                                productId.toString(),
                                productUserid.toString()
                            )
                        )
                    },
                    onUserClick = { selectedUserId ->
                        navController.navigate(Routes.Reviews.createRoute(selectedUserId.toString()))
                    }
                )
            }
        }

        composable(route = Routes.ProductDetails.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull()
            if (productId != null && userId != null) {
                ProductDetailsScreen(
                    productId = productId,
                    userId = userId,
                    onBack = { navController.popBackStack() },
                    onMakeOffer = { desiredProduct, offeredProduct ->
                        navController.navigate(
                            Routes.MakeOffer.createMakeOfferRoute(
                                desiredProductId = desiredProduct.id.toString(),
                                offeredProductIds = listOf(offeredProduct.id.toString())
                            )
                        )
                    },
                    onUserClick = {
                        navController.navigate(Routes.Reviews.createRoute(userId.toString()))
                    }
                )
            }
        }

        composable(route = Routes.Publish.route){
            PublishScreen(
                back = {navController.popBackStack()},
                openMyArticles = {navController.navigate(Routes.Article.route)}
            )
        }

        composable(route = Routes.MakeOffer.route) { backStackEntry ->
            val desiredProductIdString = backStackEntry.arguments?.getString("desiredProductId")
            val offeredProductIdsString = backStackEntry.arguments?.getString("offeredProductIds")

            val desiredProductId = desiredProductIdString?.toIntOrNull()
            val offeredProductIds = offeredProductIdsString?.split(",")?.mapNotNull { it.toIntOrNull() }

            val explorerViewModel: ExplorerListViewModel = hiltViewModel()
            val offerViewModel: OfferViewModel = hiltViewModel()

            if (desiredProductId != null && offeredProductIds != null && offeredProductIds.isNotEmpty()) {
                val desiredProduct = explorerViewModel.getProductById(desiredProductId)
                val offeredProduct = explorerViewModel.getProductById(offeredProductIds.first())

                if (desiredProduct != null && offeredProduct != null) {
                    LaunchedEffect(key1 = desiredProduct, key2 = offeredProduct) {
                        offerViewModel.initProducts(desiredProduct, offeredProduct)
                    }

                    MakeOfferScreen(
                        desiredProduct = desiredProduct,
                        onOfferMade = { /* Lógica para manejar oferta */ },
                        onBack = { navController.popBackStack() },
                        onPublish = { navController.navigate(Routes.Publish.route) },
                        navController = navController
                    )
                }
            }
        }


        composable(route = Routes.ConfirmationOffer.route) { backStackEntry ->
            val desiredProductId = backStackEntry.arguments?.getString("desiredProductId")?.toIntOrNull()
            val offeredProductId = backStackEntry.arguments?.getString("offeredProductId")?.toIntOrNull()

            val explorerViewModel: ExplorerListViewModel = hiltViewModel()
            val offerViewModel: OfferViewModel = hiltViewModel()

            if (desiredProductId != null && offeredProductId != null) {
                val desiredProduct = explorerViewModel.getProductById(desiredProductId)
                val offeredProduct = explorerViewModel.getProductById(offeredProductId)

                if (desiredProduct != null && offeredProduct != null) {
                    LaunchedEffect(key1 = desiredProduct, key2 = offeredProduct) {
                        offerViewModel.initProducts(desiredProduct, offeredProduct)
                    }

                    ConfirmationOfferScreen(
                        navController = navController,
                        desiredProduct = desiredProduct,
                        offeredProduct = offeredProduct
                    )
                }
            }
        }
    }
}
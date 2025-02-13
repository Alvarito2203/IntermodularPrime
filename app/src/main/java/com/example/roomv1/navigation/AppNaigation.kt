import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.roomv1.viewmodels.FacturasViewModel
import com.example.roomv1.views.FacturaAddView
import com.example.roomv1.views.FacturaListView
import com.example.roomv1.views.FacturaUpdateView

sealed class AppScreens(val route: String) {
    object FacturaListView : AppScreens("factura_list_view")
    object FacturaAddView : AppScreens("factura_add_view")
    object FacturaUpdateView : AppScreens("factura_update_view/{facturaId}") {
        fun createRoute(facturaId: String) = "factura_update_view/$facturaId"
    }
}

@Composable
fun AppNavigation(navController: NavHostController, viewModel: FacturasViewModel) {
    NavHost(navController = navController, startDestination = AppScreens.FacturaListView.route) {

        composable(route = AppScreens.FacturaListView.route) {
            FacturaListView(navController = navController, viewModel = viewModel)
        }

        composable(route = AppScreens.FacturaAddView.route) {
            FacturaAddView(navController = navController, viewModel = viewModel)
        }

        composable(
            route = AppScreens.FacturaUpdateView.route,
            arguments = listOf(navArgument("facturaId") { type = NavType.StringType })
        ) { backStackEntry ->
            val facturaId = backStackEntry.arguments?.getString("facturaId") ?: ""
            FacturaUpdateView(navController = navController, viewModel = viewModel, facturaId = facturaId)
        }
    }
}

package io.github.garykam.budgetmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import io.github.garykam.budgetmate.ui.navigation.AppNavHost
import io.github.garykam.budgetmate.ui.theme.BudgetMateTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BudgetMateTheme {
                AppNavHost()
            }
        }
    }
}

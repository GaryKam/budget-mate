package io.github.garykam.budgetmate.data.local.model

import androidx.annotation.DrawableRes
import io.github.garykam.budgetmate.R

enum class CreditCardBrand(
    val displayName: String,
    @param:DrawableRes val iconRes: Int
) {
    CHASE("Chase", R.drawable.ic_chase),
    AMEX("American Express", R.drawable.ic_amex),
    DISCOVER("Discover", R.drawable.ic_discover);

    companion object {
        fun fromString(name: String): CreditCardBrand? {
            return entries.find { it.displayName == name }
        }
    }
}

package io.github.garykam.budgetmate.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.garykam.budgetmate.data.local.entity.CreditCard
import io.github.garykam.budgetmate.data.local.model.CreditCardBrand

class CardPreviewProvider : PreviewParameterProvider<CreditCard> {
    override val values = sequenceOf(
        CreditCard(name = "Dining and 1.5%", brand = CreditCardBrand.CHASE.displayName),
        CreditCard(name = "Online shopping and groceries", brand = CreditCardBrand.AMEX.displayName),
        CreditCard(name = "Other", brand = null)
    )
}

package io.github.garykam.budgetmate.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import io.github.garykam.budgetmate.data.local.entity.CreditCard

class CardPreviewProvider : PreviewParameterProvider<CreditCard> {
    override val values = sequenceOf(
        CreditCard(name = "Personal Visa", brand = "Visa"),
        CreditCard(name = "Business Card with a Very Long Name", brand = null),
        CreditCard(name = "Savings", brand = "Mastercard")
    )
}

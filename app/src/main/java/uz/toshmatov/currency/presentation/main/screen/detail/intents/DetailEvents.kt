package uz.toshmatov.currency.presentation.main.screen.detail.intents

interface DetailEvents {
    data class SearchQuery(val query: String) : DetailEvents
}
package com.vanard.vshop.domain.model

sealed class Category(val name: String) {
    data object Men: Category("men's clothing")
    data object Women: Category("women's clothing")
    data object Jewelery: Category("jewelery")
}
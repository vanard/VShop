package com.vanard.domain.model

sealed class Category(val name: String) {
//    data object AllItems : Category("All Items")
    data object Men : Category("men's clothing")
    data object Women : Category("women's clothing")
    data object Jewelery : Category("jewelery")
}

enum class Categories(val value: String){
    AllItems("All Items"),
    Men("men's clothing"),
    Women("women's clothing"),
    Jewelery("jewelery"),
}

fun getAllCategories(): List<Categories>{
    return listOf(Categories.AllItems, Categories.Men, Categories.Women, Categories.Jewelery)
}

fun getCategories(value: String): Categories? {
    val map = Categories.entries.associateBy(Categories::value)
    return map[value]
}

val listCategory = listOf(
    Categories.AllItems,
    Categories.Men,
    Categories.Women,
    Categories.Jewelery
)
package com.example.shoppingapp.common


data class ProductModel(
    val productId: Any? = null,
    val productName: String = "",
    val productCategory: String = "",
    val productSize: List<sizeData> = emptyList(),
    val productFreeSize : Boolean = false,
    val productPhoto: String ="",
    val productDisp: String = "",
    val minOrderQuen: String = "1",
    val totalQuen: String ="0",
    val productPrize : Int = 0,
    val productDiscaountInPersebtage : Int = 0,
    val reating : Int = 0
)

data class CategoryModel(
    var categoryName: String = "",
    var createdBy: String = "",
    var uri: String ="",
    val date: Long = 0L,
)
data class sizeData(
    val id: Int=0,
    val size: String="",
    val click: Boolean=false,
)

data class setProduct(
    val name : String = ""
)

data class UserLocation(
    val name :String = "",
    val address : String = "",
    val state : String = "",
    val pin :String = "" ,
    val phoneNo : String ="",

)

package com.example.mad_assignment.viewModel

data class Property_datatype(
    var documentId: String = "",
    val propertyName: String = "",
    val propertyPrice: Int = 0,
    val propertyImage: String = "",
    val propertyAddress: String = "",
    val propertyCity: String = "",
    val propertyState: String = "",
    val ttlBathrooms: Int = 0,
    val ttlBedrooms: Int = 0,
    val propertyDescription: String = ""
)

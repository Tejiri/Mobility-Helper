package com.example.mobilityhelper.models

import org.json.JSONObject

class Taxi {
    var name: String
    var number: String
    var address: String
    var latitude: Double
    var longitude: Double

    constructor(map: JSONObject) {
        this.name = map["name"].toString()
        this.address = map["address"].toString()
        this.number = map["number"].toString()
        this.latitude = map["latitude"] as Double
        this.longitude = map["longitude"] as Double
    }
}
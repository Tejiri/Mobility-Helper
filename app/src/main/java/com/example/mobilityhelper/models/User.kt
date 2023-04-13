package com.example.mobilityhelper.models

class User {

    var email: String
    var firstName: String
    var id: String
    var gender: String
    var lastName: String
    var phoneNumber: String
    var role: String
    var username: String

    constructor(
        email: String,
        firstName: String,
        id: String,
        gender: String,
        lastName: String,
        phoneNumber: String,
        role: String,
        username: String
    ) {
        this.email = email
        this.firstName = firstName
        this.id = id
        this.gender = gender
        this.lastName = lastName
        this.phoneNumber = phoneNumber
        this.role = role
        this.username = username
    }

    constructor(map: Map<String, String>) {
        this.email = map["email"].toString()
        this.firstName = map["firstname"].toString()
        this.id = map["id"].toString()
        this.gender = map["gender"].toString()
        this.lastName = map["lastname"].toString()
        this.phoneNumber = map["phonenumber"].toString()
        this.role = map["role"].toString()
        this.username = map["username"].toString()

    }

}
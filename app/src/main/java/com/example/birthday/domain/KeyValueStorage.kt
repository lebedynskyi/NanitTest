package com.example.birthday.domain

interface KeyValueStorage {
    fun getString(key: String, default: String? = null): String?
    fun put(key: String, value: String?)
}
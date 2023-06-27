package com.example.gofit.api

class bookingKelasApi {

    companion object {
        val BASE_URL = mainApi.MAIN

        val GET_ALL_URL = BASE_URL + "booking_kelas/"
        val ADD_URL = BASE_URL + "booking_kelas/"
        val GET_BY_ID_URL = BASE_URL + "booking_kelas/"
        val GET_BY_MEMBER = BASE_URL + "booking_kelas/byMember/"
    }
}
package com.omnisoft.newskills.Others

class APIs {
    companion object{
        var BASE_URL = ""

        fun getAPI(endPoints: EndPoints):String{
            return when(endPoints){
                EndPoints.LOGIN ->{
                    BASE_URL+"centralServices/appUser/login"
                }
                EndPoints.USER_DATA ->{
                    BASE_URL+"centralServices/appUser/sessionData"
                }
                EndPoints.TIME ->{
                    BASE_URL+"centralServices/common/getUtcTime"
                }
            }
        }

        fun getEndpoint(endPoints: EndPoints):String{
            return getAPI(endPoints).split("/").last()
        }
    }

    enum class EndPoints{
        LOGIN,
        USER_DATA,
        TIME
    }
}
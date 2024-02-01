package com.example.projectpmobile

class ModelAdopter {
    var dataAlamatAdopter: String? = null
    var dataEmailAdopter: String? = null
    var dataImageAdopter: String? = null
    var dataNamaAdopter: String? = null
    var dataNoHpAdopter: String? = null
    var dataJenisRumahAdopter: String? = null
    var dataTujuanAdopter: String? = null
    var key: String? = null

    constructor(dataAlamatAdopter: String?, dataEmailAdopter: String?, dataImageAdopter: String?, dataNamaAdopter: String?, dataNoHpAdopter: String?, dataJenisRumahAdopter: String?, dataTujuanAdopter: String?){
        this.dataAlamatAdopter = dataAlamatAdopter
        this.dataEmailAdopter = dataEmailAdopter
        this.dataImageAdopter = dataImageAdopter
        this.dataNamaAdopter = dataNamaAdopter
        this.dataNoHpAdopter = dataNoHpAdopter
        this.dataJenisRumahAdopter = dataJenisRumahAdopter
        this.dataTujuanAdopter = dataTujuanAdopter
    }
    constructor()
    {}
}
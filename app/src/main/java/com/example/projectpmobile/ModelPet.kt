package com.example.projectpmobile

class ModelPet {
    var dataDescPet: String? = null
    var dataImagePet: String? = null
    var dataKategoriPet: String? = null
    var dataNamaPet: String? = null
    var dataRasPet: String? = null
    var dataTglLahirPet: String? = null
    var dataUmurPet: String? = null
    var key: String? = null

    constructor(dataDescPet: String?, dataImagePet: String?, dataKategoriPet: String?, dataNamaPet: String?, dataRasPet: String?, dataTglLahirPet: String?, dataUmurPet: String?){
        this.dataDescPet = dataDescPet
        this.dataImagePet = dataImagePet
        this.dataKategoriPet = dataKategoriPet
        this.dataNamaPet = dataNamaPet
        this.dataRasPet = dataRasPet
        this.dataTglLahirPet = dataTglLahirPet
        this.dataUmurPet = dataUmurPet
    }
    constructor()
    {}
}


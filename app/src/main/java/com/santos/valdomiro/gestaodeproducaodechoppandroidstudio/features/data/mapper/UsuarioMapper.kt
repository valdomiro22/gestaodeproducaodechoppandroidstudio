package com.santos.valdomiro.gestaodeproducaodechoppandroidstudio.features.data.mapper

import com.santos.valdomiro.gestaodeproducaodechoppandroidstudio.features.data.dto.UsuarioDto
import com.santos.valdomiro.gestaodeproducaodechoppandroidstudio.features.domain.entity.UsuarioEntity

fun UsuarioDto.toModel() : UsuarioEntity {
    return UsuarioEntity(
        id = this.id,
        nome = this.nome,
        sobrenome = this.sobrenome,
        email = this.email,
        urlFotoPerfil = this.urlFotoPerfil,
    );
}

fun UsuarioEntity.toEntity() : UsuarioDto {
    return UsuarioDto(
        id = this.id,
        nome = this.nome,
        sobrenome = this.sobrenome,
        email = this.email,
        urlFotoPerfil = this.urlFotoPerfil,
    );
}
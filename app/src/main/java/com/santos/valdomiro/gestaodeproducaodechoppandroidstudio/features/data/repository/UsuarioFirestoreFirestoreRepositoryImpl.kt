package com.santos.valdomiro.gestaoproducaochopp.features.autenticacao.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.santos.valdomiro.gestaoproducaochopp.core.common.exceptions.AcessoNegadoException
import com.santos.valdomiro.gestaoproducaochopp.core.common.exceptions.ErroBancoDadosDesconhecidoException
import com.santos.valdomiro.gestaoproducaochopp.core.common.exceptions.NaoEncontradoException
import com.santos.valdomiro.gestaoproducaochopp.core.common.exceptions.ServicoIndisponivelException
import com.santos.valdomiro.gestaoproducaochopp.core.utils.TAG
import com.santos.valdomiro.gestaoproducaochopp.features.autenticacao.data.datasource.UsuarioRemoteDataSource
import com.santos.valdomiro.gestaoproducaochopp.features.autenticacao.data.mapper.toDocument
import com.santos.valdomiro.gestaoproducaochopp.features.autenticacao.data.mapper.toModel
import com.santos.valdomiro.gestaoproducaochopp.features.autenticacao.domain.model.Usuario
import com.santos.valdomiro.gestaoproducaochopp.features.autenticacao.domain.repository.UsuarioFirestoreRepository
import javax.inject.Inject

class UsuarioFirestoreFirestoreRepositoryImpl @Inject constructor(
    private val usuarioDataSource: UsuarioRemoteDataSource
) : UsuarioFirestoreRepository {

    override suspend fun insertUser(usuario: Usuario): Result<Unit> {
        return try {
            usuarioDataSource.insertUser(usuario.toDocument())
            Result.success(Unit)
        } catch (e: FirebaseFirestoreException) {
            val exceptionMapeada =  when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED ->
                    AcessoNegadoException(e)

                FirebaseFirestoreException.Code.NOT_FOUND ->
                    NaoEncontradoException(e)

                FirebaseFirestoreException.Code.UNAVAILABLE ->
                    ServicoIndisponivelException(e)

                else ->
                    ErroBancoDadosDesconhecidoException(e)
            }
            Result.failure(exceptionMapeada)
        } catch (e: Exception) {
            Result.failure(ErroBancoDadosDesconhecidoException(e))
        }
    }

    override suspend fun updateUser(id: String, usuario: Usuario): Result<Unit> {
        return try {
            usuarioDataSource.updateUser(id, usuario.toDocument())
            Log.d(TAG, "Update usuario chamado")
            Result.success(Unit)
        } catch (e: FirebaseFirestoreException) {
            val exceptionMapeada = when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> AcessoNegadoException(e)
                FirebaseFirestoreException.Code.NOT_FOUND -> NaoEncontradoException(e)
                FirebaseFirestoreException.Code.UNAVAILABLE -> ServicoIndisponivelException(e)
                else -> ErroBancoDadosDesconhecidoException(e)
            }
            Result.failure(exceptionMapeada)
        } catch (e: Exception) {
            Result.failure(ErroBancoDadosDesconhecidoException(e))
        }
    }

    override suspend fun getUser(id: String): Result<Usuario?> {
        return try {
            val usuarioDocument = usuarioDataSource.getUser(id)
            Result.success(usuarioDocument?.toModel())
        } catch (e: FirebaseFirestoreException) {
            val exceptionMapeada = when (e.code) {
                FirebaseFirestoreException.Code.NOT_FOUND -> NaoEncontradoException(e)
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> AcessoNegadoException(e)
                FirebaseFirestoreException.Code.UNAVAILABLE -> ServicoIndisponivelException(e)
                else -> ErroBancoDadosDesconhecidoException(e)
            }
            Result.failure(exceptionMapeada)
        } catch (e: Exception) {
            Result.failure(ErroBancoDadosDesconhecidoException(e))
        }
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        return try {
            usuarioDataSource.deleteUser(id)
            Result.success(Unit)
        } catch (e: FirebaseFirestoreException) {
            val exceptionMapeada = when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> AcessoNegadoException(e)
                FirebaseFirestoreException.Code.NOT_FOUND -> NaoEncontradoException(e) // usuário já não existe
                FirebaseFirestoreException.Code.UNAVAILABLE -> ServicoIndisponivelException(e)
                else -> ErroBancoDadosDesconhecidoException(e)
            }
            Result.failure(exceptionMapeada)
        } catch (e: Exception) {
            Result.failure(ErroBancoDadosDesconhecidoException(e))
        }

    }

    override suspend fun getAllUsers(): Result<List<Usuario>> {
        return try {
            val listaDocument = usuarioDataSource.getAllUsers()
            val listaModel = listaDocument.map { it.toModel() }
            Result.success(listaModel)
        } catch (e: FirebaseFirestoreException) {
            val exceptionMapeada = when (e.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> AcessoNegadoException(e)
                FirebaseFirestoreException.Code.UNAVAILABLE -> ServicoIndisponivelException(e)
                else -> ErroBancoDadosDesconhecidoException(e)
            }
            Result.failure(exceptionMapeada)
        } catch (e: Exception) {
            Result.failure(ErroBancoDadosDesconhecidoException(e))
        }
    }
}
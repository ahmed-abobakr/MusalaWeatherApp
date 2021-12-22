package com.ahmed_abobakr.musalaweatherapp.base.exceptions


public class ApiException(override val message: String, val errorCode: Int = 0) : Exception(message)

public class NetowrkException(message: String): Exception(message)

public class AuthException(override val message: String, val throwable: Throwable): Exception(message)

public class UnknownException(val throwable: Throwable): Exception(throwable)
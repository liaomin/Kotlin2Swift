package com.liam.gen.swift

import org.jetbrains.kotlin.psi.*

open class TypeUtils {

    val basicTypes = arrayOf("Int","Int64","Int8","Int16","UInt8","UInt16","UInt","UInt64","Character","Bool")

    open fun getType(typeRef: KtTypeReference?):String?{
        return getType(typeRef?.typeElement)
    }

    open fun getType(type: KtTypeElement?):String?{
        type?.let {
            when(it){
                is KtUserType -> {
                    when (it.referencedName){
                        "Long" -> return "Int64"
                        "Byte" ->  return "Int8"
                        "Short" ->  return "Int16"
                        "ULong" ->  return "UInt64"
                        "UByte" ->  return "UInt8"
                        "UShort" ->  return "UInt16"
                        "UInt" ->  return "UInt"
                        "Boolean" ->  return "Bool"
                        "Char" ->  return "Character"
                        else -> return it.referencedName
                    }
                }
                is KtNullableType -> return getType(it.innerType)+"?"
//                is KtFunctionType -> {
//                    //Block
//                    Node.TypeRef.Func(
//                            receiverType = v.receiverTypeReference?.let(::convertType),
//                            params = v.parameters.map {
//                                Node.TypeRef.Func.Param(
//                                        name = it.name,
//                                        type = convertType(it.typeReference ?: error("No param type"))
//                                ).map(it)
//                            },
//                            type = convertType(v.returnTypeReference ?: error("No return type"))
//                    )
//                }
//                KtDynamicType
                else -> {
                    notSupport()
                }
            }
        }
        return null
    }

    fun isBasicType(type:String?) = basicTypes.contains(type)

    companion object : TypeUtils()
}
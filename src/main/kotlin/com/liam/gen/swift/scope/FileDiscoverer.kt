package com.liam.gen.swift.scope

import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty

open class FileDiscoverer : Discoverer<KtFile>{

    override fun onDiscovery(target: KtFile,scope: Scope) {
        target.declarations.forEach {
            when (it){
                is KtClassOrObject -> {

                }
                is KtNamedFunction -> {

                }
                is KtProperty -> {

                }
            }
        }
    }

    companion object:FileDiscoverer()
}
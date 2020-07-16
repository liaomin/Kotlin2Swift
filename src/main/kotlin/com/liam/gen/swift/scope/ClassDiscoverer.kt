package com.liam.gen.swift.scope

import org.jetbrains.kotlin.psi.KtClassOrObject

open class ClassDiscoverer : Discoverer<KtClassOrObject>{

    override fun onDiscovery(target: KtClassOrObject,scope: Scope) {

    }

    companion object:ClassDiscoverer()
}
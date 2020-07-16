package com.liam.gen.swift.scope

interface Discoverer<T> {

    fun onDiscovery(target: T,scope: Scope)

}
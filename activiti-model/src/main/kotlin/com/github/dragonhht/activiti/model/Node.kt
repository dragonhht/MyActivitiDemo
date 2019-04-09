package com.github.dragonhht.activiti.model

import kotlin.reflect.KClass

/**
 * 流程节点信息模型.
 *
 * @author: huang
 * @Date: 2019-4-9
 */
class Node constructor() {

    var id: String? = null
    var name: String? = null
    lateinit var type: KClass<out Any>

    constructor(id: String?, name: String?, type: KClass<out Any>): this() {
        this.id = id
        this.name = name
        this.type = type
    }

    override fun toString(): String {
        return "Node(id=$id, name=$name, type=$type)"
    }

}
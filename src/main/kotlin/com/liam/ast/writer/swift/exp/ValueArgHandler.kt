package com.liam.ast.writer.swift.exp

import com.liam.ast.psi.Node
import com.liam.ast.writer.*

/**
 * @author liaomin
 * @date 6/22/20 9:53 上午
 * @version 1.0
 */
open class ValueArgHandler : SwiftHandler<Node.ValueArg>() {

    override fun getHandleClass(): Class<Node.ValueArg> = Node.ValueArg::class.java

    override fun onHandle(writer: LanguageWriter, node: Node.ValueArg, statement: Statement) {
        if(node.name != null){
            notSupport()
            statement.append("${node.name}:")
        }else{
//            notSupport()
        }
        writer.onWriteNode(node.expr,statement,node)
    }

}

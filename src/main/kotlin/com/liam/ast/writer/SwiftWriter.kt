package com.liam.ast.writer

import com.liam.ast.psi.Node
import com.liam.uitls.PackageUtils

/**
 * @author liaomin
 * @date 6/19/20 2:04 下午
 * @version 1.0
 */
open class SwiftWriter : BaseLanguageWriter {

    val imports = ArrayList<String>()

    constructor():super(){
        PackageUtils.getClassesInPackage("com.liam.ast.writer.swift")
            .filter { Handler.isHandlerClass(it) }
            .forEach { try {
                addHandler(it.newInstance() as Handler<out Node>)
            }catch (e:Exception){
                e.printStackTrace()
            } }
    }

    override fun write(file: Node.File, statement: Statement) {
        imports.clear()
        super.write(file, statement)
    }

//    override fun onWriteImport(node: Node.Import, statement: Statement) {
//        if(node.names.size > 0){
//            var path:String? = null
//            if(node.wildcard){
//                path = node.names.last()
//            }else if(node.names.size > 1){
//                path = node.names[node.names.size - 2]
//            }
//            path?.also {
//                if(!imports.contains(it)){
//                    imports.add(it)
//                    statement.append("import $it\n")
//                }
//            }
//        }
//    }


//    override fun joinPropertyVarAndExpr(node: Node.Decl.Property, statement: Statement) {
//        statement.append(" = ")
//    }


//    override fun onWriteTypeNullable(node: Node.TypeRef.Nullable, statement: Statement) {
//        super.onWriteTypeNullable(node, statement)
//        statement.append("?")
//    }
//


//    override fun onWriteExpConst(node: Node.Expr.Const, statement: Statement) {
//        val from = node.form
//        when(from){
//            Node.Expr.Const.Form.NULL -> statement.append("nil")
//            Node.Expr.Const.Form.INT -> {
//                var v = node.value
//                if(v.endsWith("L")){
//                    statement.append(v.subSequence(0,v.length-1))
//                }else{
//                    statement.append(v)
//                }
//            }
//        }
//    }

    override fun getLanguage(): Language = Language.SWIFT

    companion object : SwiftWriter()



}

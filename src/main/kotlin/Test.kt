
val oneLong = 1L
//@Ani()
private var dqwd:Long = 12L
//TODO bug
val dq = dqwd-- + ++dqwd + dqwd
val dq2 = 2 + 2

val oneMillion = 1_000_000
val creditCardNumber = 1234_5678_9012_3456L
val socialSecurityNumber = 999_99_9999L
val hexBytes = 0xFF_EC_DE_5E
val bytes = 0b11010010_01101001_10010100_10010010

class A1{}

private class Test<Z:A1,Q> {
    var a1:Int = 0


    private fun <T> test(a:Int):T{
        this.a1=2
        var q1:Int? = 1
        val q = 1 as? T?
        return 1 as T
    }
}

//fun <T> MutableList<T>.swap(index1: Int, index2: Int):T {
//    val tmp = this[index1]
//    this[index1] = this[index2]
//    this[index2] = tmp
//    return 1 as T
//}

//private enum class A1 {
//    DW,
//    DW2,
//}

//private enum class A(val int:Int){
//    DW(1),
//    DW2(2),
//}
//
//
//class QE(a:String) {
//    constructor(a:Int):this(""){
//        A.DW.ordinal
//    }
//}

//
//fun t1(block:(()->Unit)? = null){
//    block?.invoke()
//}
//
//fun t2() = t1{}
//
//open class  B{
//    fun t(){}
//}
//
//
//@Ani
//class Test:B {
//    var q2:Int = 1
//        set(value) {
//            var d:B? = B()
////            d!!.t()
//            if(value > 0 &&  field > 0){
//                field = value
//            }else{
//                field = 0
//            }
//        }
//        @Ani
//        get() {
//            return field
//        }
//
//    constructor():super(){
//        @Ani var dw = 1
//    }
//
//    constructor(int :Int):super(){
//
//    }
//
//    @Ani
//    private var int = 1
//
//    val string:String = ""
//
//}



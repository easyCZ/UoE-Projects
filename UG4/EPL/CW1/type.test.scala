import CW1._

val env = Map[Variable, Type]()

// Eq
tyOf(env, Eq(Num(1), Num(2))) == BoolTy
tyOf(env, Eq(Str("a"), Str("b"))) == BoolTy
tyOf(env, Eq(Bool(true), Bool(false))) == BoolTy

println(">>> Expect errors")
tyOf(env, Eq(Num(1), Str("a")))
tyOf(env, Eq(Str("a"), Bool(false)))
tyOf(env, Eq(Bool(true), Num(2)))
println(">>> Expect errors stop")


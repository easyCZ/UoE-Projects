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

// IfThenElse
tyOf(env, IfThenElse(Eq(Num(1), Num(2)), Num(1), Num(2)))
tyOf(env, IfThenElse(Eq(Str("a"), Str("a")), Bool(true), Bool(false)))
tyOf(env, IfThenElse(Eq(Bool(true), Bool(true)), Str("true"), Str("false")))

println(">>> Expect errors")
tyOf(env, IfThenElse(Eq(Num(1), Str("a")), Num(1), Num(2)))
tyOf(env, IfThenElse(Eq(Str("a"), Num(2)), Bool(true), Bool(false)))
tyOf(env, IfThenElse(Eq(Bool(true), Bool(true)), Num(1), Str("false")))
tyOf(env, IfThenElse(Eq(Bool(true), Bool(true)), Str("true"), Bool(false)))
tyOf(env, IfThenElse(Eq(Bool(true), Bool(true)), Bool(true), Num(2)))
println(">>> Expect errors stop")

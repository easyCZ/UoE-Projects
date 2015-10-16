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
println()

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
println()

// Length
tyOf(env, Length(Str("hello"))) == IntTy

println(">>> Expect errors")
tyOf(env, Length(Bool(true)))
tyOf(env, Length(Num(1)))
println(">>> Expect errors stop")
println()

// Index
tyOf(env, Index(Str("hello"), Num(2))) == StringTy

println(">>> Expect errors")
tyOf(env, Index(Num(2), Bool(true)))
tyOf(env, Index(Str("hello"), Bool(false)))
println(">>> Expect errors stop")
println()

// Concat
tyOf(env, Concat(Str("hel"), Str("lo"))) == StringTy

println(">>> Expect errors")
tyOf(env, Concat(Str("hel"), Bool(true)))
tyOf(env, Concat(Str("hel"), Num(1)))
tyOf(env, Concat(Bool(false), Str("lo")))
tyOf(env, Concat(Num(1), Str("lo")))
tyOf(env, Concat(Num(1), Num(2)))
tyOf(env, Concat(Bool(true), Bool(false)))
println(">>> Expect errors stop")
println()

// LetFun
tyOf(env, LetFun(
    "f", "x", IntTy,
    Plus(Var("x"), Num(1)),
    Apply(Var("f"), Num(10))
)) == IntTy

tyOf(env, LetFun(
    "f", "x", StringTy,
    Concat(Var("x"), Str("world")),
    Apply(Var("f"), Str("hello"))
)) == StringTy


// LetRec
tyOf(
    env,
    LetRec(
        "f", "x", IntTy, IntTy,
        IfThenElse(
            Eq(Var("x"), Num(10)),
            Num(2),
            Apply(
                Var("f"),
                Plus(Var("x"), Num(1))
            )
        ),
        Apply(
            Var("f"),
            Num(5)
        )
    )
) == IntTy

tyOf(
    env,
    LetRec(
        "f", "x", StringTy, StringTy,
        IfThenElse(
            Eq(Length(Var("x")), Num(10)),
            Var("x"),
            Apply(
                Var("f"),
                Concat(Var("x"), Str("aa"))
            )
        ),
        Apply(
            Var("f"),
            Str("hello")
        )
    )
) == StringTy



println(">>> Expect errors")
tyOf(
    env,
    LetRec(
        "f", "x", StringTy, StringTy,
        IfThenElse(
            Eq(Length(Var("x")), Num(10)),
            Var("x"),
            Apply(
                Var("f"),
                Concat(Var("x"), Str("aa"))
            )
        ),
        Apply(
            Var("f"),
            Str("hello")
        )
    )
) == BoolTy
println(">>> Expect errors stop")
println()
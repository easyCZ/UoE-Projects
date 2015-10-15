import CW1


val apply1 = CW1.Apply(
    CW1.Lambda(
        "x",
        CW1.IntTy,
        CW1.Plus(
            CW1.Var("y"),
            CW1.Var("x")
        )
    ),
    CW1.Num(2)
)
CW1.eval(Map("y" -> CW1.NumV(7)), applied) == CW1.NumV(9)

val apply2 = CW1.Apply(
    CW1.Lambda(
        "x",
        CW1.IntTy,
        CW1.Plus(
            CW1.Var("x"),
            CW1.Var("x")
        )
    ),
    CW1.Apply(
        CW1.Lambda(
            "x",
            CW1.IntTy,
            CW1.Plus(
                CW1.Var("x"),
                CW1.Num(1)
            )
        ),
        CW1.Num(1)
    )
)
CW1.eval(Map(), apply2) == CW1.NumV(4)

// Rec

val apply3 = CW1.Apply(
    CW1.Rec(
        "f",
        "x",
        CW1.IntTy,
        CW1.IntTy,
        CW1.IfThenElse(
            CW1.Eq(
                CW1.Var("x"),
                CW1.Num(3)
            ),
            CW1.Var("x"),
            CW1.Apply(
                CW1.Var("f"),
                CW1.Plus(
                    CW1.Var("x"),
                    CW1.Num(1)
                )
            )
        )
    ),
    CW1.Num(0)
)

CW1.eval(Map(), apply3)
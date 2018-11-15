

package object dragonball {
  type Movimiento = {def apply(pareja: Pareja): Pareja}

  type PlanDeAtaque = List[Movimiento]
  type FormaDeDigerir = (Guerrero, Guerrero) => Guerrero

}

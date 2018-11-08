package object dragonball {
  type Criterio = Guerrero => Guerrero => Movimiento => Double
  type PlanDeAtaque = List[Movimiento]
  type Movimiento = Pareja => Pareja
  type Item = Movimiento
  type FormaDeDigerir = (Guerrero, Guerrero) => Guerrero

}

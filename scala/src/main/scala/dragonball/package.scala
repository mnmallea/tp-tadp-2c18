package object dragonball {
  type Criterio = Pareja => Pareja => Double
  type PlanDeAtaque = List[Movimiento]
  type Movimiento = Pareja => Pareja
  type Item = Pareja => Pareja
  type FormaDeDigerir = (Guerrero, Guerrero) => Guerrero

}

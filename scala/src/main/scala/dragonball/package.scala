package object dragonball {
  type Movimiento = {def apply(pareja: Pareja): Pareja}
  type PlanDeAtaque = List[Movimiento]
  type FormaDeDigerir = Guerrero => Guerrero => Guerrero
  type ResultadoPelea = Either[Guerrero, Pareja]
  type Ganador[Guerrero, Pareja] = Left[Guerrero, Pareja]
  val Ganador = Left
  type Peleando[Guerrero, Pareja] = Right[Guerrero, Pareja]
  val Peleando = Right
}

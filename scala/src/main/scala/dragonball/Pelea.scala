package dragonball

trait Pelea {
  def flatMap(f: Pareja => Pelea): Pelea
}

object Pelea {
  def apply(pareja: Pareja)(movimiento: Movimiento): Pelea = {
    val Pareja(atacante, atacado) = pareja
    val trasPelea = atacante.pelearRound(movimiento)(atacado)
    trasPelea.estados match {
      case (_, Muerto) => Ganador(trasPelea.atacante)
      case (Muerto, _) => Ganador(trasPelea.atacado)
      case _ => Peleando(trasPelea.atacante, trasPelea.atacado)
    }
  }
}

case class Peleando(atacante: Guerrero, atacado: Guerrero) extends Pelea {
  lazy val pareja = Pareja(atacante, atacado)

  override def flatMap(f: Pareja => Pelea): Pelea = f(pareja)

}

case class Ganador(ganador: Guerrero) extends Pelea {
  override def flatMap(f: Pareja => Pelea): Pelea = this
}

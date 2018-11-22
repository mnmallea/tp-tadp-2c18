package dragonball

trait Pelea {
  def flatMap(f: Peleando => Pelea): Pelea
}

case class Peleando(atacante: Guerrero, atacado: Guerrero) extends Pelea {
  override def flatMap(f: Peleando => Pelea): Pelea = f(this)

  def pelearRound(movimiento: Movimiento): Pelea ={
    val trasPelea = atacante.pelearRound(movimiento)(atacado)
    trasPelea.estados match {
      case (_, Muerto) => Ganador(trasPelea.atacante)
      case (Muerto, _) => Ganador(trasPelea.atacado)
      case _ => Peleando(trasPelea.atacante, trasPelea.atacado)
    }
  }
}

case class Ganador(ganador: Guerrero) extends Pelea{
  override def flatMap(f: Peleando => Pelea): Pelea = this
}

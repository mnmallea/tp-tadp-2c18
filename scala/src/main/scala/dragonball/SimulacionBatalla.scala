package dragonball

import dragonball.Criterios.Criterio

trait SimulacionBatalla {
  def get: Option[PlanDeAtaque]

  def simularMejorMovimiento(criterio: Criterio): SimulacionBatalla
}

case object SimulacionFallida extends SimulacionBatalla {
  override def get: Option[PlanDeAtaque] = None

  override def simularMejorMovimiento(criterio: Criterio): SimulacionBatalla = this
}

case class Simulando(pareja: Pareja, planDeAtaque: PlanDeAtaque = Nil) extends SimulacionBatalla {
  val Pareja(atacante, atacado) = pareja

  override def get: Option[PlanDeAtaque] = Some(planDeAtaque)

  private def mejorMovimiento(criterio: Criterio) = atacante.movimientoMasEfectivoContra(atacado)(criterio)

  override def simularMejorMovimiento(criterio: Criterio): SimulacionBatalla = {
    mejorMovimiento(criterio)
      .map(movimiento => copy(atacante.pelearRound(movimiento)(atacado), planDeAtaque :+ movimiento))
      .getOrElse(SimulacionFallida)
  }
}
package dragonball

import scala.util.Random

object FormasDeDigerir {
  def aprenderTodosLosMovimientos(comensal: Guerrero)(digerido: Guerrero): Guerrero = {
      comensal.aprenderMovimientos(digerido.movimientos)
    }

  def aprenderAlgunMovimiento(comensal: Guerrero)(digerido: Guerrero): Guerrero = {
      comensal.aprenderMovimientos(Random.shuffle(comensal.movimientos) take 1)
  }

  def aprenderMovimientosDelUltimoOponente(comensal: Guerrero)(digerido: Guerrero): Guerrero = {
    comensal.copy(movimientos = digerido.movimientos)
  }
}

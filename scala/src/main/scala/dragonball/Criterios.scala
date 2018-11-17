package dragonball

object Criterios {
  type Criterio = Pareja => Pareja => Double

  /** Criterio que se anticipa al peor escenario
    */
  def criterioCagon(antes: Pareja)(despues: Pareja): Double = {
    despues.atacante.energiaActual - antes.atacado.energiaActual
  }

  def criterioOptimista(antes: Pareja)(despues: Pareja): Double = 100

  def criterioMayorDanioRealizado(antes: Pareja)(despues: Pareja): Double = {
    antes.atacado.energiaActual - despues.atacado.energiaActual
  }

  def criterioMenorDanioSufrido(antes: Pareja)(despues: Pareja): Double = {
    despues.atacante.energiaActual - antes.atacante.energiaActual
  }

  def criterioSuicida(antes: Pareja)(despues: Pareja): Double = {
    despues.atacante.energiaActual
  }

  def criterioItems(antes: Pareja)(despues: Pareja): Double = {
    despues.atacante.cantidadDeItems - antes.atacante.cantidadDeItems
  }


}

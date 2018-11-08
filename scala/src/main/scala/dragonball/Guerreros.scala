package dragonball

case class Guerrero(nombre: String, inventario: List[Item], energia: Int, maximoPotencial: Int, especie: Especie
                    , estado: EstadoGuerrero, movimientos: List[Movimiento]) {
  def tieneItem(item: Item): Boolean = inventario.contains(item)

  def aumentarEnergia(cantidad: Int) = this.copy(energia = (energia + cantidad).min(maximoPotencial))

  def recuperarPotencial: Guerrero = copy(energia = maximoPotencial)

  def movimientoMasEfectivoContra(otroGuerrero: Guerrero)(criterio: Criterio): Option[Movimiento] = ???

  def pelearRound(movimiento: Movimiento)(oponente: Guerrero): (Guerrero, Guerrero) = ???

  def planDeAtaqueContra(oponente: Guerrero, cantidadDeRounds: Int)(criterio: Criterio): Option[PlanDeAtaque] = ???

  def pelearContra(oponente: Guerrero)(planDeAtaque: PlanDeAtaque): Any = ???
}

trait EstadoGuerrero

object Vivo extends EstadoGuerrero
object Inconsciente extends EstadoGuerrero
object Muerto extends EstadoGuerrero
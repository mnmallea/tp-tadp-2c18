package dragonball

case class Guerrero(nombre: String, inventario: List[Item], energia: Energia, especie: Especie, estado: EstadoGuerrero, movimientos: Set[Movimiento]) {
  def cantidadEnergiaAlExplotar(): Int = this.especie.tipoEnergia match {
    case _: ConKi => this.energia.actual * 2
    case _: ConBateria => this.energia.actual * 3
  }

  def explotar: Guerrero = copy(energia = energia.modificarMaximo(_ => 0), estado = Muerto)

  def serAtacadoPorExplosion(unaCantidad: Int): Guerrero = especie match {
    case Namekusein() => copy(energia = energia disminuirConMinimo(unaCantidad, 1))
    case _ => copy(energia = energia disminuir unaCantidad)
  }


  def tieneItem(item: Item): Boolean = inventario.contains(item)

  def aumentarEnergia(cantidad: Int) = this.copy(energia = energia aumentar cantidad)

  def recuperarPotencial: Guerrero = copy(energia = energia.cargarAlMaximo)

  def movimientoMasEfectivoContra(otroGuerrero: Guerrero)(criterio: Criterio): Option[Movimiento] = ???

  def pelearRound(movimiento: Movimiento)(oponente: Guerrero): (Guerrero, Guerrero) = ???

  def planDeAtaqueContra(oponente: Guerrero, cantidadDeRounds: Int)(criterio: Criterio): Option[PlanDeAtaque] = ???

  def pelearContra(oponente: Guerrero)(planDeAtaque: PlanDeAtaque): Any = ???
}

trait EstadoGuerrero

object Vivo extends EstadoGuerrero

object Inconsciente extends EstadoGuerrero

object Muerto extends EstadoGuerrero
package dragonball

import dragonball.Items.SemillaDelErmitanio
import dragonball.Movimientos.DejarseFajar

case class Guerrero(nombre: String, inventario: List[Item], energia: Energia, especie: Especie, estado: EstadoGuerrero,
                    movimientos: Set[Movimiento], roundsDejandoseFajar: Int = 0) {


  def tieneSuficienteEnergia(energiaNecesaria: Int): Boolean = energia.actual >= energiaNecesaria

  def cantidadEnergiaAlExplotar(): Int = especie.tipoEnergia.cantidadDeEnergiaAlExplotar(energia.actual)

  def explotar: Guerrero = copy(energia = energia.modificarMaximo(_ => 0), estado = Muerto)

  def serAtacadoPorExplosion(unaCantidad: Int): Guerrero = especie match {
    case Namekusein() => copy(energia = energia disminuir(unaCantidad, 1))
    case _ => copy(energia = energia disminuir unaCantidad)
  }

  def recibirAtaqueDeEnergia(cantidad: Int): Guerrero = {
    especie match {
      case _: Androide => aumentarEnergia(cantidad)
      case _ => disminuirEnergia(cantidad)
    }
  }

  def puedeRealizarMovimiento(movimiento: Movimiento): Boolean = {
    movimiento match {
      case SemillaDelErmitanio => estado != Muerto
      case _ => estado == Vivo
    }
  }

  //TODO habría que validar que tengas el movimiento???
  def realizarMovimientoContra(movimiento: Movimiento, otroGuerrero: Guerrero): Pareja = {
    if (puedeRealizarMovimiento(movimiento)) {
      movimiento(this, Pareja(this, otroGuerrero)).mapAtacante { guerrero =>
        movimiento match {
          case DejarseFajar =>
            guerrero.copy(roundsDejandoseFajar = roundsDejandoseFajar + 1)
          case _ => guerrero.copy(roundsDejandoseFajar = 0)
        }
      }
    }
    else
      Pareja(this, otroGuerrero)
  }

  def tieneItem(item: Item): Boolean = inventario.contains(item)

  def aumentarEnergia(cantidad: Int) = this.copy(energia = energia aumentar cantidad)

  def disminuirEnergia(disminucion: Int): Guerrero = this.copy(energia = energia disminuir disminucion)

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
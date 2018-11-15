package dragonball

import dragonball.Criterios._
import dragonball.Movimientos.DejarseFajar

case class Guerrero(nombre: String, inventario: List[Item], energia: Energia, especie: Especie, estado: EstadoGuerrero,
                    movimientos: List[Movimiento], roundsDejandoseFajar: Int = 0) {

  def perderEsferas() :Guerrero = copy(inventario = inventario.filterNot(_.equals(EsferaDeDragon)))

  def gastarMunicion(arma: Arma, cantidad: Int = 1): Guerrero = {
    def restarMunicion(cantidad: Int, items: List[Item]): List[Item] = {
      if (cantidad > 0) {
        items match {
          case Nil => List()
          case unItem :: otrosItems => unItem match {
            case Municion(`arma`, cantidadDisponible) =>
              if (cantidadDisponible >= cantidad) {
                Municion(arma, cantidadDisponible - cantidad) :: restarMunicion(0, otrosItems)
              } else {
                Municion(arma, 0) :: restarMunicion(cantidad - cantidadDisponible, otrosItems)
              }
            case _ => unItem :: restarMunicion(cantidad, otrosItems)
          }
        }
      } else
        items
    }

    copy(inventario = restarMunicion(cantidad, this.inventario))
  }

  def tieneMunicionDe(arma: Arma, cantidadRequerida: Int = 1): Boolean = cantidadDeMunicionDe(arma) >= cantidadRequerida

  def cantidadDeMunicionDe(arma: Arma): Int = {
    inventario.map {
      case Municion(`arma`, cantidad) => cantidad
      case _ => 0
    }.sum
  }

  def cantidadDeEsferasDelDragon(): Int = {
    inventario.count(_ == EsferaDeDragon)
  }

  lazy val cantidadDeItems: Int = inventario.size

  lazy val energiaActual: Int = energia.actual

  def especie(unaEspecie: Especie): Guerrero = copy(especie = unaEspecie)

  def estado(unEstado: EstadoGuerrero): Guerrero = copy(estado = unEstado)

  def enegiaActual(cantidad: Int): Guerrero = this.copy(energia = energia.setActual(cantidad))

  def tieneSuficienteEnergia(energiaNecesaria: Int): Boolean = energia.actual >= energiaNecesaria

  def cantidadEnergiaAlExplotar(): Int = especie.tipoEnergia.cantidadDeEnergiaAlExplotar(energia.actual)

  def explotar: Guerrero = copy(energia = energia.modificarMaximo(_ => 0), estado = Muerto)

  def serAtacadoPorExplosion(unaCantidad: Int): Guerrero = especie match {
    case Namekusein() => copy(energia = energia disminuir(unaCantidad, 1))
    case _ => disminuirEnergia(unaCantidad)
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
      movimiento(Pareja(this, otroGuerrero)).mapAtacante { guerrero =>
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

  def movimientoMasEfectivoContra(otroGuerrero: Guerrero)(criterio: Criterio): Option[Movimiento] = {
    val parejaActual = Pareja(this, otroGuerrero)

    def obtenerValor(movimiento: Movimiento) = criterio(parejaActual)(realizarMovimientoContra(movimiento, otroGuerrero))

    movimientos.foldLeft(None: Option[Movimiento]) { (semilla, movimiento) => {
      semilla match {
        case None if obtenerValor(movimiento) >= 0 => Some(movimiento)
        case Some(movimientoSemilla) =>
          if (obtenerValor(movimiento) > obtenerValor(movimientoSemilla))
            Some(movimiento)
          else
            Some(movimientoSemilla)
        case _ => None
      }
    }
    }
  }


  // aca no seria this en vez de atacante? - no toy seguro
  def pelearRound(movimiento: Movimiento)(oponente: Guerrero): Pareja = {
    val Pareja(atacante, atacado) = realizarMovimientoContra(movimiento, oponente)
    val mejorContrataque = atacado.movimientoMasEfectivoContra(atacante)(criterioCagon)
    mejorContrataque match {
      case None => Pareja(atacante, atacado)
      case Some(unMovimiento) => atacado.realizarMovimientoContra(unMovimiento, atacante)
    }
  }

  def planDeAtaqueContra(oponente: Guerrero, cantidadDeRounds: Int)(criterio: Criterio): Option[PlanDeAtaque] = {
    (1 to cantidadDeRounds toList).foldLeft((Some(Nil), Pareja(this, oponente)): (Option[PlanDeAtaque], Pareja)) {
      (tupla, _) =>
        (tupla._1.flatMap { movimientos =>
          movimientoMasEfectivoContra(tupla._2.atacado)(criterio) match {
            case None => None
            case Some(movimiento) => Some(movimientos ++ List(movimiento))
          }
        }, movimientoMasEfectivoContra(tupla._2.atacado)(criterio) match {
          case None => tupla._2
          case Some(movimiento) => pelearRound(movimiento)(tupla._2.atacado)
        })
    }._1
  }

  def pelearContra(oponente: Guerrero)(planDeAtaque: PlanDeAtaque): ResultadoPelea = ???

}

trait EstadoGuerrero

object Vivo extends EstadoGuerrero

object Inconsciente extends EstadoGuerrero

object Muerto extends EstadoGuerrero
package dragonball

import dragonball.Criterios._
import dragonball.Movimientos.DejarseFajar

case class Guerrero(nombre: String, inventario: List[Item], energia: Energia, especie: Especie, estado: EstadoGuerrero,
                    movimientos: List[Movimiento], roundsDejandoseFajar: Int = 0) {

  def perderEsferas(): Guerrero = copy(inventario = inventario.filterNot(_.equals(EsferaDeDragon)))

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

  def aprenderMovimientos(unosMovimientos: List[Movimiento]): Guerrero = copy(movimientos = movimientos ++ unosMovimientos)

  def tieneItem(item: Item): Boolean = inventario.contains(item)

  def aumentarEnergia(cantidad: Int) = this.copy(energia = energia aumentar cantidad)

  def disminuirEnergia(disminucion: Int): Guerrero = {
    this.copy(energia = energia disminuir disminucion).verificarEstado()
  }

  def verificarEstado(): Guerrero = {
    if (this.energia.actual == 0) this.copy(estado = Muerto) else this
  }

  def recuperarPotencial: Guerrero = copy(energia = energia.cargarAlMaximo)

  def movimientoMasEfectivoContra(otroGuerrero: Guerrero)(criterio: Criterio): Option[Movimiento] = {
    val parejaActual = Pareja(this, otroGuerrero)

    def obtenerValor(movimiento: Movimiento) = criterio(parejaActual)(realizarMovimientoContra(movimiento, otroGuerrero))

    movimientos.filter(obtenerValor(_) > 0).sortBy(obtenerValor)(Ordering.Double.reverse).headOption
  }

  def pelearRound(movimiento: Movimiento)(oponente: Guerrero): Pareja = {
    val pareja@Pareja(atacante, atacado) = realizarMovimientoContra(movimiento, oponente)
    val mejorContrataque = atacado.movimientoMasEfectivoContra(atacante)(criterioCagon)
    mejorContrataque.map(atacado.realizarMovimientoContra(_, atacante).flip).getOrElse(pareja)
  }

  def planDeAtaqueContra(oponente: Guerrero, cantidadDeRounds: Int)(criterio: Criterio): Option[PlanDeAtaque] = {
    type EstadoAtaque = (PlanDeAtaque, Pareja)

    case class SimulacionBatalla(pareja: Pareja, plan: PlanDeAtaque = Nil){
      def movimientoMasEfectivo = movimientoMasEfectivoContra(pareja.atacado)(criterio)
      def realizarMovimiento(movimiento: Movimiento) = copy(pareja.atacante.pelearRound(movimiento)(pareja.atacado), plan :+ movimiento)
    }

    (1 to cantidadDeRounds).foldLeft(Option(SimulacionBatalla(Pareja(this, oponente)))) { (maybeBatalla, _) =>
      for(
        batalla <- maybeBatalla;
        movimiento <- batalla.movimientoMasEfectivo
      ) yield batalla.realizarMovimiento(movimiento)
//      estadoOption.flatMap {
//        case (unosMovimientos: PlanDeAtaque, Pareja(atacante, atacado)) =>
//          movimientoMasEfectivoContra(atacado)(criterio).flatMap {
//            unMovimiento => Option((unosMovimientos ++ List(unMovimiento), atacante.pelearRound(unMovimiento)(atacado)))
//          }
//      }
    }.map(_.plan)
  }

  def pelearContra(oponente: Guerrero)(planDeAtaque: PlanDeAtaque): ResultadoPelea = {
    planDeAtaque.foldLeft(Peleando(Pareja(this, oponente)): ResultadoPelea) { (peleando, movimiento) =>
      peleando.flatMap(p => {
        val pDespuesDePelear = p.atacante.pelearRound(movimiento)(p.atacado)
        pDespuesDePelear.estados match {
          case (_, Muerto) => Ganador(pDespuesDePelear.atacante)
          case (Muerto, _) => Ganador(pDespuesDePelear.atacado)
          case _ => Peleando(pDespuesDePelear)
        }
      })
    }
  }
}

trait EstadoGuerrero

case object Vivo extends EstadoGuerrero

case object Inconsciente extends EstadoGuerrero

case object Muerto extends EstadoGuerrero
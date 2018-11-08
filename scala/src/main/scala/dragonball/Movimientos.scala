package dragonball

import dragonball.Items.FotoDeLuna

case class Pareja(atacante: Guerrero, atacado: Guerrero) {
  def mapAtacante(f: Guerrero => Guerrero): Pareja = {
    Pareja(f(atacante), atacado)
  }

  def mapAtacado(f: Guerrero => Guerrero): Pareja = {
    Pareja(atacante, f(atacado))
  }

  def mapEspecies(): (Especie, Especie) = (this.atacante.especie, this.atacado.especie)
}


object Movimientos {

  case object DejarseFajar extends Movimiento {
    def apply(pareja: Pareja): Pareja = pareja
  }

  case object CargarKi extends Movimiento {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(atacante => atacante.especie match {
        case Androide(_) => atacante
        case Saiyajin(Super(nivel), _, _) => atacante.aumentarEnergia(150 * nivel)
        case _ => atacante.aumentarEnergia(100)
      }
      )
    }
  }

  case class UsarItem(item: Item) extends Movimiento {
    def apply(pareja: Pareja): Pareja = {
      if (pareja.atacante.tieneItem(item)) {
        item(pareja)
      } else {
        pareja
      }
    }
  }

  case object ComerseOponente extends Movimiento {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(atacante => atacante.especie match {
        case especie: Monstruo => especie.formaDeDigerir(pareja.atacante, pareja.atacado)
        case _ => atacante
      })
    }
  }

  case object ConvertirseEnMono extends Movimiento {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(atacante => atacante.especie match {
        case especie: Saiyajin if especie.tieneCola && atacante.tieneItem(FotoDeLuna) =>
          atacante.copy(especie = especie.convertirseEnMono())
        case _ => atacante
      })
    }
  }

  case object ConvertirseEnSS extends Movimiento {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(atacante => atacante.especie match {
        case especie: Saiyajin if atacante.energia >= atacante.maximoPotencial / 2 =>
          atacante.copy(especie = especie.convertirseEnSS())
        case _ => atacante
      })
    }
  }

  case class Fusion(guerrero: Guerrero) {

  }
}
package dragonball

import dragonball.Items.FotoDeLuna

case class Pareja(atacante: Guerrero, atacado: Guerrero) {
  def mapAtacante(f: Guerrero => Guerrero): Pareja = {
    Pareja(f(atacante), atacado)
  }

  def mapAtacado(f: Guerrero => Guerrero): Pareja = {
    Pareja(atacante, f(atacado))
  }

  lazy val especies: (Especie, Especie) = (this.atacante.especie, this.atacado.especie)
}


object Movimientos {

  case object DejarseFajar extends Movimiento {
    def apply(pareja: Pareja): Pareja = pareja
  }

  case object CargarKi extends Movimiento {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(atacante => atacante.especie match {
        case Androide() => atacante
        case Saiyajin(Super(nivel), _) => atacante.aumentarEnergia(150 * nivel)
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
        case saiyajin: Saiyajin if saiyajin.tieneCola && atacante.tieneItem(FotoDeLuna) =>
          atacante.copy(energia = atacante.energia.modificarMaximo(3 *).cargarAlMaximo, especie = saiyajin setEstado MonoGigante)
        case _ => atacante
      })
    }
  }

  case object ConvertirseEnSS extends Movimiento {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(atacante => atacante.especie match {
        case saiyayin: Saiyajin if atacante.energia.actual >= atacante.energia.maximo / 2 =>
          try {
            val nuevoNivel = saiyayin.proximoNivelSaiyayin
            atacante.copy(especie = saiyayin setEstado Super(nuevoNivel),
              energia = atacante.energia.modificarMaximo(_ * 5 * nuevoNivel))
          } catch {
            case _: SaiyajinException => atacante
          }
        case _ => atacante
      })
    }
  }

  case object Fusion extends Movimiento {
    def apply(pareja: Pareja): Pareja = {
      pareja.especies match {
        case (Humano() | Saiyajin(_, _) | Namekusein(), Humano() | Saiyajin(_, _) | Namekusein()) =>
          pareja.mapAtacante(atacante => {
            atacante.copy(energia = atacante.energia.fusionadaA(pareja.atacado.energia),
              movimientos = atacante.movimientos ++ pareja.atacado.movimientos,
              especie = Fusionado(atacante.especie))
          })
        case _ => pareja
      }
    }
  }

}

case object GolpesNinja extends Movimiento {
  def apply(pareja: Pareja): Pareja = {
    pareja.especies match {
      case (Humano(), Androide()) => ???
      case _ => ???
    }
  }
}

case object Explotar extends Movimiento {
  def apply(pareja: Pareja): Pareja = {
    pareja.atacante.especie match {
      case Androide() | Monstruo(_) => pareja.mapAtacante(_.explotar).mapAtacado(_.serAtacadoPorExplosion)
      case _ => pareja
    }
  }

}

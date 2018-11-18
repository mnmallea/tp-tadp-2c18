package dragonball

case class Pareja(atacante: Guerrero, atacado: Guerrero) {
  def mapAtacante(f: Guerrero => Guerrero): Pareja = {
    Pareja(f(atacante), atacado)
  }

  def mapAtacado(f: Guerrero => Guerrero): Pareja = {
    Pareja(atacante, f(atacado))
  }

  def daniarAlMasDebil(cantidad: Int): Pareja = {
    if (atacante.energia.actual < atacado.energia.actual)
      mapAtacante(_ disminuirEnergia cantidad)
    else
      mapAtacado(_ disminuirEnergia cantidad)
  }

  def flip: Pareja = copy(atacado = atacante, atacante = atacado)

  lazy val especies: (Especie, Especie) = (this.atacante.especie, this.atacado.especie)

  lazy val estados: (EstadoGuerrero, EstadoGuerrero) = (this.atacante.estado, this.atacado.estado)
}


object Movimientos {

//  trait MovimientoBase {
//    def applyMovimiento(pareja: Pareja): Pareja
//    def apply(pareja: Pareja): Pareja = applyMovimiento(pareja.mapAtacante(_.copy(roundsDejandoseFajar = 0)))
//  }
//
//  case object DejarseFajar {
//    def apply(pareja: Pareja): Pareja = pareja.mapAtacante(at => at.copy(roundsDejandoseFajar =  at.roundsDejandoseFajar + 1))
//  }

  case object DejarseFajar {
    def apply(pareja: Pareja): Pareja = pareja
  }

  case object CargarKi {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(atacante => atacante.especie match {
        case Androide() => atacante
        case Saiyajin(Super(nivel), _) => atacante.aumentarEnergia(150 * nivel)
        case _ => atacante.aumentarEnergia(100)
      }
      )
    }
  }

  case class UsarItem(item: ItemUsable) {
    def apply(pareja: Pareja): Pareja = {
      if (pareja.atacante.tieneItem(item))
        item(pareja)
      else
        pareja
    }
  }

  case object ComerseOponente {
    def apply(pareja: Pareja): Pareja = pareja.atacante.especie.comer(pareja)
  }

  case object ConvertirseEnMono {
    def apply(pareja: Pareja): Pareja = pareja.atacante.especie.convertirseEnMono(pareja)
  }

  case object ConvertirseEnSS {
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

  case class Fusion(amigo: Guerrero) { // TODO: fijense si lo "arregle" bien, queda medio feo el match pero creo que es lo mejor
    def apply(pareja: Pareja): Pareja = {
      (pareja.atacante.especie, amigo.especie) match {
        case (Humano() | Saiyajin(_, _) | Namekusein(), Humano() | Saiyajin(_, _) | Namekusein()) =>
          pareja.mapAtacante(atacante => {
            atacante.copy(energia = atacante.energia.fusionadaA(amigo.energia),
              movimientos = atacante.movimientos ++ amigo.movimientos,
              especie = Fusionado(atacante.especie))
          })
        case _ => pareja
      }
    }
  }

  case class Magia(cambiarEstado: CambiarEstado, sobreOponente: Boolean = true) {
    def apply(pareja: Pareja): Pareja = {
      if (sobreOponente) {
        pareja.atacante.especie match {
          case Namekusein() | Monstruo(_) =>
            pareja.copy(atacado = cambiarEstado(pareja.atacado))
          case _ if (pareja.atacante.cantidadDeEsferasDelDragon == 7) =>
            pareja.copy(atacante = pareja.atacante.perderEsferas(), atacado = cambiarEstado(pareja.atacado))
          case _ => pareja
        }
      } else {
        pareja.atacante.especie match {
          case Namekusein() | Monstruo(_) =>
            pareja.copy(atacante = cambiarEstado(pareja.atacante))
          case _ if (pareja.atacante.cantidadDeEsferasDelDragon == 7) =>
            pareja.copy(atacante = cambiarEstado(pareja.atacante.perderEsferas()))
          case _ => pareja
        }
      }
    }
  }

}

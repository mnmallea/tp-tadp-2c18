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
      if (pareja.atacante.tieneItem(item)) {
        item(pareja)
      } else {
        pareja
      }
    }
  }

  case object ComerseOponente {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(atacante => atacante.especie match {
        case especie: Monstruo => especie.formaDeDigerir(pareja.atacante, pareja.atacado)
        case _ => atacante
      })
    }
  }

  case object ConvertirseEnMono {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(atacante => atacante.especie match {
        case saiyajin: Saiyajin if saiyajin.tieneCola && atacante.tieneItem(FotoDeLuna) =>
          atacante.mapEnergia(_.modificarMaximo(3 *).cargarAlMaximo).especie(saiyajin setEstado MonoGigante)
        case _ => atacante
      })
    }
  }

  case object ConvertirseEnSS {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(atacante => atacante.especie match {
        case saiyayin: Saiyajin if atacante.energia.actual >= atacante.energia.maximo / 2 =>
          try {
            val nuevoNivel = saiyayin.proximoNivelSaiyayin
            atacante.especie(saiyayin setEstado Super(nuevoNivel))
              .mapEnergia(_.modificarMaximo(_ * 5 * nuevoNivel))
          } catch {
            case _: SaiyajinException => atacante
          }
        case _ => atacante
      })
    }
  }

  case class Fusion(amigo: Guerrero) {
    def apply(pareja: Pareja): Pareja = {
      (pareja.atacante.especie, amigo.especie) match {
        case (Humano() | Saiyajin(_, _) | Namekusein(), Humano() | Saiyajin(_, _) | Namekusein()) =>
          pareja.mapAtacante(atacante => {
            atacante.mapEnergia(_.fusionadaA(amigo.energia))
              .aprenderMovimientos(amigo.movimientos)
              .especie(Fusionado(atacante.especie))
          })
        case _ => pareja
      }
    }
  }

  case class Magia(nuevoEstado: EstadoGuerrero, sobreOponente: Boolean = true) {
    def apply(pareja: Pareja): Pareja = {
      pareja.atacante.especie match {
        case Namekusein() | Monstruo(_) => cambiarEstado(pareja, debePerderEsferas = false)
        case _ if pareja.atacante.cantidadDeEsferasDelDragon == 7 => cambiarEstado(pareja, debePerderEsferas = true)
        case _ => pareja
      }

    }

    private def cambiarEstado(pareja: Pareja, debePerderEsferas: Boolean): Pareja = {
      if (sobreOponente)
        pareja.mapAtacado(atacado => atacado.estado(nuevoEstado))
          .mapAtacante(atacante => if (debePerderEsferas) atacante.perderEsferas() else atacante)
      else
        pareja.mapAtacante { atacante => atacante.estado(nuevoEstado) }
          .mapAtacado(atacado => if (debePerderEsferas) atacado.perderEsferas() else atacado)
    }
  }


}

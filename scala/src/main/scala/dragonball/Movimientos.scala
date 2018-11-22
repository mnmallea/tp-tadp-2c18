package dragonball

case class Pareja(atacante: Guerrero, atacado: Guerrero) {
  def mapAtacante(f: Guerrero => Guerrero): Pareja = {
    Pareja(f(atacante), atacado)
  }

  def mapAtacado(f: Guerrero => Guerrero): Pareja = {
    Pareja(atacante, f(atacado))
  }

  def atacado(nuevoAtacado: Guerrero): Pareja = mapAtacado(_ => nuevoAtacado)

  def atacante(nuevoAtacante: Guerrero): Pareja = mapAtacado(_ => nuevoAtacante)

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
    def apply(pareja: Pareja): Pareja = if (pareja.atacante.tieneItem(item)) item(pareja) else pareja
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
          saiyayin.proximoNivelSaiyayin.map { nivel =>
            atacante.especie(saiyayin estado Super(nivel))
              .mapEnergia(_ modificarMaximo (_ * 5 * nivel))
          }.getOrElse(atacante)
        case _ => atacante
      })
    }
  }

  case class Fusion(amigo: Guerrero) {
    def apply(pareja: Pareja): Pareja = {
      (pareja.atacante.especie, amigo.especie) match {
        case (Humano() | Saiyajin(_, _) | Namekusein(), Humano() | Saiyajin(_, _) | Namekusein()) =>
          pareja.mapAtacante(atacante => {
            atacante.mapEnergia(_ fusionadaA amigo.energia)
              .aprenderMovimientos(amigo.movimientos)
              .especie(Fusionado(atacante.especie))
          })
        case _ => pareja
      }
    }
  }

  case class Magia(cambiarEstado: CambiarEstado, tipoMagia: TipoMagia) {
    def apply(pareja: Pareja): Pareja = {
      pareja.atacante.especie match {
        case Namekusein() | Monstruo(_) =>
          tipoMagia(cambiarEstado, pareja)
        case _ if pareja.atacante.cantidadDeEsferasDelDragon == 7 =>
          tipoMagia(cambiarEstado, pareja)
            .mapAtacante(_.perderEsferas)
        case _ => pareja
      }
    }
  }

  trait TipoMagia {
    def apply(cambiarEstado: CambiarEstado, pareja: Pareja): Pareja
  }

  case object SobreOponente extends TipoMagia {
    override def apply(cambiarEstado: CambiarEstado, pareja: Pareja): Pareja = pareja mapAtacado cambiarEstado
  }

  case object SobreSiMismo extends TipoMagia {
    override def apply(cambiarEstado: CambiarEstado, pareja: Pareja): Pareja = pareja mapAtacante cambiarEstado
  }

}

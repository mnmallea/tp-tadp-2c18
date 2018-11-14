package dragonball

object Ataques {
  type Ataque = Pareja => Pareja


  case object GolpesNinja extends Ataque {
    def apply(pareja: Pareja): Pareja = {
      pareja.especies match {
        case (Humano(), Androide()) => pareja mapAtacante (_ disminuirEnergia 10)
        case _ => pareja daniarAlMasDebil 20
      }
    }
  }

  case object Explotar extends Ataque {
    def apply(pareja: Pareja): Pareja = {
      pareja.atacante.especie match {
        case Androide() | Monstruo(_) => pareja.mapAtacante(_.explotar).mapAtacado(atacado =>
          atacado.serAtacadoPorExplosion(pareja.atacante.cantidadEnergiaAlExplotar()))
        case _ => pareja
      }
    }

  }

  case class Onda(energiaNecesaria: Int) extends Ataque {
    def apply(pareja: Pareja): Pareja = {
      if (pareja.atacante.tieneSuficienteEnergia(energiaNecesaria))
        pareja.mapAtacante(_.disminuirEnergia(energiaNecesaria))
          .mapAtacado { atacado =>
            atacado.especie match {
              case _: Monstruo => atacado.recibirAtaqueDeEnergia(energiaNecesaria / 2)
              case _ => atacado.recibirAtaqueDeEnergia(energiaNecesaria * 2)
            }
          }
      else
        pareja
    }
  }

  case object Genkidama extends Ataque {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacado(atacado => atacado.recibirAtaqueDeEnergia(10 ^ atacado.roundsDejandoseFajar))
    }
  }

}

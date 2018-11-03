package dragonball

object Items {

  case object SemillaDelErmitanio extends Item {
    def apply(pareja: Pareja): Pareja = {
      pareja.mapAtacante(_.recuperarPotencial)
    }
  }

  val ArmaRoma: Pareja => Pareja = (pareja: Pareja) => {
    pareja.mapAtacado { guerrero =>
      guerrero.especie match {
        case Androide() => guerrero
        case _ if guerrero.energia < 300 => guerrero.copy(estado = Inconsciente)
      }
    }
  }

}
import Especies._
import Guerreros._

package object Movimientos {

  case class Resultado(atacante: Guerrero, atacado: Guerrero)

  type Movimiento = (Guerrero, Guerrero) => Resultado

  object DejarseFajar extends Movimiento {
    def apply(atacante: Guerrero, atacado: Guerrero): Resultado = Resultado(atacante, atacado)
  }

  object CargarKi extends Movimiento {
    def apply(atacante: Guerrero, atacado: Guerrero): Resultado = {
      val nuevoAtacante = atacante.especie match {
        case Androide() => atacante
        case Saiyajin(Super(nivel)) => atacante.aumentarEnergia(150 * nivel)
        case _ => atacante.aumentarEnergia(100)
      }
      Resultado(nuevoAtacante, atacado)
    }
  }

  class UsarItem (item: Item) extends Movimiento{
    def apply(atacante: Guerrero, atacado: Guerrero): Resultado = {
      ???
    }
  }

}

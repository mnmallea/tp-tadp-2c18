import Especies.Especie
import Movimientos.Movimiento

package object Guerreros {

  type Criterio = Guerrero => Guerrero => Movimiento => Double

  type PlanDeAtaque = List[Movimiento]

  type Item

  case class Guerrero(nombre: Int, inventario: List[Item], energia: Int, especie: Especie) {
    def aumentarEnergia(cantidad: Int) = this.copy(energia = energia + cantidad)

    def movimientoMasEfectivoContra(otroGuerrero: Guerrero)(criterio: Criterio): Option[Movimiento] = ???

    def pelearRound(movimiento: Movimiento)(oponente: Guerrero): (Guerrero, Guerrero) = ???

    def planDeAtaqueContra(oponente: Guerrero, cantidadDeRounds: Int)(criterio: Criterio): Option[PlanDeAtaque] = ???

    def pelearContra(oponente: Guerrero)(planDeAtaque: PlanDeAtaque): Any = ???
  }


}


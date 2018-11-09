package dragonball

import Movimientos._
import Items._
import org.scalatest.{FreeSpec, Matchers}

class MovimientoTest extends FreeSpec with Matchers {

  "Test de movimientos" - {
    val androide = Guerrero("androide1785", List(), Energia(100,200), Androide(), Vivo, Set())
    val humano = Guerrero("iron man", List(), Energia(100,200), Humano(), Vivo, Set())

    "el androide se deja fajar por el humano y todo debería quedar igual" in {
      val resultado = DejarseFajar(Pareja(androide, humano))

      resultado.atacante shouldBe androide
      resultado.atacado shouldBe humano
    }

    "el humano intenta usar la semilla del hermitaño pero todo queda igual porque no tiene el item" in {
      val resultado = DejarseFajar(Pareja(humano, androide))

      resultado.atacante shouldBe humano
      resultado.atacado shouldBe androide
    }


    "el humano ahora tiene la semilla del hermitaño, deberia recuperar su energia maxima" in {
      val resultado = UsarItem(SemillaDelErmitanio)(Pareja(humano.copy(inventario = List(SemillaDelErmitanio)), androide))

      resultado.atacante.energia.actual shouldBe 200
      resultado.atacado shouldBe androide
    }

    "el humano es atacado con un ArmaRoma y deberia quedar inconsciente" in {
      val resultado = UsarItem(ArmaRoma)(Pareja(androide.copy(inventario = List(ArmaRoma)), humano))
      resultado.atacado.estado shouldBe Inconsciente
    }


  }

}

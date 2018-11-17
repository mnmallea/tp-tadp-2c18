package dragonball

import dragonball.Movimientos.{CargarKi, DejarseFajar, UsarItem}
import org.scalatest.{FreeSpec, Matchers}

class ArmasTest extends FreeSpec with Matchers {
  "Tests para las municiones" - {
    val androide = Guerrero("androide1785",
      List(Municion(ArmaDeFuego, 3), ArmaDeFuego, Municion(ArmaRoma, 0), ArmaRoma),
      Energia(100, 200), Androide(), Vivo, List())
    "el androide deberia tener municion de arma de fuego" in {
      androide.tieneMunicionDe(ArmaDeFuego, 3) shouldBe true
    }

    "el androide no deberia tener municion de arma roma" in {
      androide.tieneMunicionDe(ArmaRoma) shouldBe false
    }

    "el androide no deberia tener municion de arma filosa" in {
      androide.tieneMunicionDe(ArmaFilosa) shouldBe false
    }
    val humano = Guerrero("iron man", List(Municion(ArmaDeFuego, 2), Municion(ArmaDeFuego, 1)),
      Energia(100, 200), Humano(), Vivo, List())

    "el humano deberia tener 3 municiones de arma filosa" in {
      humano.tieneMunicionDe(ArmaDeFuego, 3) shouldBe true
    }
    "el humano gasta 2 de arma de fuego y le queda uno" in {
      humano.gastarMunicion(ArmaDeFuego, 2).cantidadDeMunicionDe(ArmaDeFuego) shouldBe 1
    }

    "el humano gasta 3 de arma de fuego y no tiene mas municion" in {
      humano.gastarMunicion(ArmaDeFuego, 3).tieneMunicionDe(ArmaDeFuego) shouldBe false
    }

  }

  "Test de uso de Armas" - {
    val goku = Guerrero("goku", List(ArmaFilosa), Energia(100, 100),
      Saiyajin(Normal), Vivo, List[Movimiento](DejarseFajar, CargarKi, UsarItem(ArmaFilosa)))
    val vegeta = Guerrero("vegeta", List(), Energia(100, 100),
      Saiyajin(MonoGigante), Vivo, List())

    "goku usa un arma filosa contra vegeta, su energia queda en 1 por ser un mono gigante" in {
      goku.realizarMovimientoContra(UsarItem(ArmaFilosa),vegeta).atacado.energiaActual shouldBe 1
    }

    "goku queda inconsciente tras ser atacado por un arma filosa" in {
      goku.realizarMovimientoContra(UsarItem(ArmaFilosa),vegeta).atacado.estado shouldBe Inconsciente
    }
  }

}

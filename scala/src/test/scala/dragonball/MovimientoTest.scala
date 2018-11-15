package dragonball

import dragonball.Movimientos._
import org.scalatest.{FreeSpec, Matchers}

class MovimientoTest extends FreeSpec with Matchers {

  "Test de movimientos" - {
    val androide = Guerrero("androide1785", List(), Energia(100, 200), Androide(), Vivo, List())
    val humano = Guerrero("iron man", List(), Energia(100, 200), Humano(), Vivo, List())
    val namekusein = Guerrero("name", List(), Energia(100, 300), Namekusein(), Vivo, List())

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

    "Fusiones" - {

      "fusion humano- androide" - {
        val namekuseinFusion = namekusein.copy(movimientos = List[Movimiento](DejarseFajar, CargarKi))
        val fusion = Fusion(namekuseinFusion)
        val humanoFusion = humano.copy(movimientos = List(fusion))
        val fusionado = fusion(Pareja(humanoFusion, namekuseinFusion)).atacante
        "el humano se fusiona con un namekusein" in {
          fusionado.especie shouldBe Fusionado(humanoFusion.especie)
        }

        "energia de la fusion humano - namekusein" in {
          fusionado.energia shouldBe Energia(200, 500)
        }

        "los movimientos del fusionado deben ser 3" in {
          fusionado.movimientos.size shouldBe 3
        }

        "movimientos del fusionado" in {
          fusionado.movimientos shouldBe List(Fusion(namekuseinFusion), DejarseFajar, CargarKi)
        }

      }

      "el humano intenta fusionarse con el androide pero no pasa nada" in {
        val fusionado = Fusion(androide)(Pareja(humano, androide)).atacante
        fusionado shouldBe humano
      }

    }

    "Sayajines" - {
      def convertirASS(x: Guerrero) = ConvertirseEnSS(Pareja(x, humano)).atacante

      def convertirAMono(x: Guerrero) = ConvertirseEnMono(Pareja(x, humano)).atacante

      val saiyajin = Guerrero("julian ezequiel", List(), Energia(100, 100), Saiyajin(Normal), Vivo, List())

      "Conversión exitosa" - {
        val nuevoGuerrero = convertirASS(saiyajin)
        "se convierte y su nivel debe ser 1" in {
          nuevoGuerrero.especie.asInstanceOf[Saiyajin].estado shouldBe Super(1)
        }
        "se convierte y su ki maximo pasa a ser el quintuple" in {
          nuevoGuerrero.energia.maximo shouldBe saiyajin.energia.maximo * 5
        }
        "se convierte y su ki actual sigue siendo el mismo" in {
          nuevoGuerrero.energia.actual shouldBe saiyajin.energia.actual
        }
      }

      "si es un mono no puede convertirse en saiyayin" in {
        convertirASS(saiyajin.copy(especie = Saiyajin(MonoGigante))).especie.asInstanceOf[Saiyajin].estado shouldBe MonoGigante
      }

      "si tiene el ki bajo no puede convertirse" in {
        convertirASS(saiyajin.copy(energia = Energia(49, 100))).especie.asInstanceOf[Saiyajin].estado shouldBe Normal
      }

    }

    "Magia" - {
      val resultadoMagia = Magia(Muerto)(Pareja(androide, humano))

      "si un androide quiere hacer magia queda todo igual" in {
        resultadoMagia.atacante shouldBe androide
        resultadoMagia.atacado shouldBe humano
      }

      val namecheto = Guerrero("namename", List(), Energia(100, 200), Namekusein(), Vivo, List())
      val resultadoMagia2 = Magia(Muerto)(Pareja(namecheto, humano))

      "si un namekusein quiere hacer magia sobre el opononte cambia el estado del opononte" in {
        resultadoMagia2.atacado.estado shouldBe Muerto
      }

    }

  }

}

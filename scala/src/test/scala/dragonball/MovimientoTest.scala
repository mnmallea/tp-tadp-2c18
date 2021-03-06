package dragonball

import dragonball.Movimientos._
import dragonball.FormasDeDigerir._
import org.scalatest.{FreeSpec, Matchers}

class MovimientoTest extends FreeSpec with Matchers {

  "Test de movimientos" - {
    val androide = Guerrero("androide1785", List(), Energia(100, 200), Androide(), Vivo, List())
    val humano = Guerrero("iron man", List(), Energia(100, 200), Humano(), Vivo, List())
    val namekusein = Guerrero("name", List(), Energia(100, 300), Namekusein(), Vivo, List())

    def borrarItems(guerrero: Guerrero): Guerrero = {
      guerrero.copy(inventario = List())
    }

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

    "Comerse oponente" - {

      "si un humano intenta comer todo sigue igual" in {
        val todoIgual = ComerseOponente((Pareja(humano, androide)) )
        todoIgual.atacante shouldBe humano
        todoIgual.atacado shouldBe androide
      }

      val monstruoDebil = Guerrero("debil", List(), Energia(40, 200), Monstruo(aprenderTodosLosMovimientos), Vivo, List())

      "si el monstruo tiene menos energia que el humano todo sigue igual" in {
        val todoIgual = ComerseOponente((Pareja(monstruoDebil, androide)) )
        todoIgual.atacante shouldBe monstruoDebil
        todoIgual.atacado shouldBe androide
      }

      val cell = Guerrero("cell", List(), Energia(100, 200), Monstruo(aprenderTodosLosMovimientos), Vivo, List())
      val humanoMago = humano.copy(movimientos = List(Magia(borrarItems, SobreOponente)))

      "si cell se come a un humano, humano muere " in {
        val resultado = ComerseOponente(Pareja(cell, humanoMago))

        resultado.atacado.estado shouldBe Muerto
        resultado.atacante.movimientos.size shouldBe 1
      }

      val majinBuu = Guerrero("majinBuu", List(), Energia(100, 200), Monstruo(aprenderMovimientosDelUltimoOponente), Vivo, List())
      val humanoFusionado = humano.copy(movimientos = List(Fusion(androide)))

      "si majinBuu come a un humano mago y dsp a un humano fusionado" in {
        val comerMago = ComerseOponente(Pareja(majinBuu, humanoMago))
        val comerFusionado = ComerseOponente(Pareja(comerMago.atacante, humanoFusionado))

        comerFusionado.atacante.movimientos.size shouldBe 1
        comerFusionado.atacado.estado shouldBe Muerto
      }
    }

    "Magia" - {
      def quitarEnergia(guerrero: Guerrero): Guerrero = {
        guerrero.disminuirEnergia(20)
      }

      val resultadoMagia = Magia(quitarEnergia, SobreOponente)(Pareja(androide, humano))

      "si un androide quiere hacer magia queda todo igual" in {
        resultadoMagia.atacante shouldBe androide
        resultadoMagia.atacado shouldBe humano
      }

      val namecheto = Guerrero("namename", List(), Energia(100, 200), Namekusein(), Vivo, List())
      val resultadoMagia2 = Magia(quitarEnergia,SobreOponente)(Pareja(namecheto, humano))

      "si un namekusein quiere hacer magia sobre el opononte cambia energia del opononte" in {
        resultadoMagia2.atacado.energiaActual shouldBe 80
      }

      val humanoConItems = Guerrero("iron man", List(ArmaRoma), Energia(100, 200), Humano(), Vivo, List())

      "si un namekusein hace magia deja al oponente sin items" in {
        val resultadoMagia = Magia(borrarItems, SobreOponente)(Pareja(namecheto,humanoConItems))

        resultadoMagia.atacado.inventario.size shouldBe 0
      }
    }

  }

}

package dragonball

sealed trait Especie {
  def tipoEnergia: TipoEnergia

  /* no hace nada por defecto */
  def comer(pareja: Pareja): Pareja = pareja

  /* no hace nada por defecto */
  def convertirseEnMono(pareja: Pareja): Pareja = pareja

  def recibirAtaqueDeEnergia(cantidad: Int, guerrero: Guerrero): Guerrero = guerrero.disminuirEnergia(cantidad)

  def recibirAtaqueDeExplosion(cantidad: Int, guerrero: Guerrero): Guerrero = guerrero.disminuirEnergia(cantidad)

  def recibirAtaqueDeArmaRoma(pareja: Pareja): Pareja = {
    if (pareja.atacado.energia.actual < 300)
      pareja mapAtacado(_ estado Inconsciente)
    else pareja
  }
}

case class Saiyajin(estado: EstadoSayajin, tieneCola: Boolean = true) extends Especie with ConKi {
  def perderColaDe(guerrero: Guerrero): Guerrero = {
    val guerreroLastimado = guerrero enegiaActual 1 especie this.perderCola
    this.estado match {
      case MonoGigante => guerreroLastimado.especie(this.estado(Normal)).estado(Inconsciente)
      case _ => guerreroLastimado
    }
  }

  override def convertirseEnMono(pareja: Pareja): Pareja = {
    if (tieneCola && pareja.atacante.tieneItem(FotoDeLuna)) {
      pareja.mapAtacante {
        _.mapEnergia(_ modificarMaximo (3 *) cargarAlMaximo)
          .especie(this estado MonoGigante)
      }
    } else
      pareja
  }

  def perderCola: Saiyajin = copy(tieneCola = false)

  def estado(nuevoEstado: EstadoSayajin): Saiyajin = copy(estado = nuevoEstado)

  def proximoNivelSaiyayin: Int = {
    estado match {
      case MonoGigante => throw new SaiyajinException
      case Super(nivel) => nivel + 1
      case _ => 1
    }
  }

}

case class Humano() extends Especie with ConKi

case class Namekusein() extends Especie with ConKi {
  override def recibirAtaqueDeExplosion(cantidad: Int, guerrero: Guerrero): Guerrero = guerrero.mapEnergia(_ disminuir(cantidad, unMinimo = 1))
}

case class Monstruo(formaDeDigerir: FormaDeDigerir) extends Especie with ConKi {
  override def comer(pareja: Pareja): Pareja = {
    if (pareja.atacante.energiaActual >= pareja.atacado.energiaActual)
      pareja.mapAtacante(formaDeDigerir(_)(pareja.atacado))
          .mapAtacado(_ estado Muerto)
    else
      pareja
  }
}

case class Androide() extends Especie with ConBateria {
  override def recibirAtaqueDeEnergia(cantidad: Int, guerrero: Guerrero): Guerrero = guerrero aumentarEnergia cantidad

  override def recibirAtaqueDeArmaRoma(pareja: Pareja): Pareja = pareja
}

case class Fusionado(especieOriginal: Especie) extends Especie {
  def tipoEnergia: TipoEnergia = especieOriginal.tipoEnergia
}
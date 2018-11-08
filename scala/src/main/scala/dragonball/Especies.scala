package dragonball

sealed trait Especie

sealed trait EstadoSayajin {
  def convertimeEnSS(saiyajin: Saiyajin): Saiyajin

  def convertimeEnMono(saiyajin: Saiyajin): Saiyajin
}

case class Super(nivel: Int) extends EstadoSayajin {
  override def convertimeEnMono(saiyajin: Saiyajin): Saiyajin = saiyajin.copy(estado = MonoGigante())

  override def convertimeEnSS(saiyajin: Saiyajin): Saiyajin = saiyajin.copy(estado = Super(nivel+1))
}

case class Normal() extends EstadoSayajin {
  override def convertimeEnMono(saiyajin: Saiyajin): Saiyajin = saiyajin.copy(estado = MonoGigante())

  override def convertimeEnSS(saiyajin: Saiyajin): Saiyajin = saiyajin.copy(estado = Super(1))
}

case class MonoGigante() extends EstadoSayajin {
  override def convertimeEnMono(saiyajin: Saiyajin): Saiyajin = saiyajin
}

case class Saiyajin(estado: EstadoSayajin, tieneCola: Boolean = true) extends Especie {
  def convertirseEn(estadoNuevo: EstadoSayajin): Saiyajin = estadoNuevo match {
    case MonoGigante() => estado.convertimeEnMono(this)
    case Super(_) => estado.convertimeEnSS(this)
  }
} // tiene cola y estado supersayayin


case class Humano() extends Especie


case class Namekusein() extends Especie //poderes curativos

case class Monstruo(formaDeDigerir: FormaDeDigerir) extends Especie //se comen a sus oponenentes

case class Androide() extends Especie // no tienen ki, no necesitan comer y no pueden quedar inconscientes


//Opción para manejar energia

//sealed trait TipoEnergia
//case object Ki extends TipoEnergia
//case object Bateria extends TipoEnergia
//
//case class Energia(actual: Int, maxima: Int) {
//  def aumentar(aumento: Int): Energia = copy(actual = (actual + aumento).min(maxima))
//}
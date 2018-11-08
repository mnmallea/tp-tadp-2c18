package dragonball

sealed trait Especie

sealed trait EstadoSayajin {
  def convertimeEnSS(saiyajin: Saiyajin): Saiyajin

  def convertimeEnMono(saiyajin: Saiyajin): Saiyajin
}

case class Super(nivel: Int) extends EstadoSayajin {
  override def convertimeEnMono(saiyajin: Saiyajin): Saiyajin = saiyajin.copy(estado = MonoGigante(),
    energia = saiyajin.energia.modificarMaximo(3 *).cargarAlMaximo)

  override def convertimeEnSS(saiyajin: Saiyajin): Saiyajin = saiyajin.copy(estado = Super(nivel + 1),
    energia = saiyajin.energia.modificarMaximo(5 *))
}

case class Normal() extends EstadoSayajin {
  override def convertimeEnMono(saiyajin: Saiyajin): Saiyajin = saiyajin.copy(estado = MonoGigante())

  override def convertimeEnSS(saiyajin: Saiyajin): Saiyajin = saiyajin.copy(estado = Super(1),
    energia = saiyajin.energia.modificarMaximo(5 *))
}

case class MonoGigante() extends EstadoSayajin {
  override def convertimeEnMono(saiyajin: Saiyajin): Saiyajin = saiyajin

  override def convertimeEnSS(saiyajin: Saiyajin): Saiyajin = saiyajin // si es mono no puede combinarse con SS
}

case class Saiyajin(estado: EstadoSayajin, tieneCola: Boolean = true, energia: Energia) extends Especie {

  def convertirseEnSS(): Saiyajin = estado convertimeEnSS this

  def convertirseEnMono(): Saiyajin = estado convertimeEnMono this
} // tiene cola y estado supersayayin


case class Humano(energia: Energia) extends Especie


case class Namekusein(energia: Energia) extends Especie //poderes curativos

case class Monstruo(formaDeDigerir: FormaDeDigerir, energia: Energia) extends Especie //se comen a sus oponenentes

case class Androide(energia: Energia) extends Especie // no tienen ki, no necesitan comer y no pueden quedar inconscientes


//Opción para manejar energia

//sealed trait TipoEnergia
//case object Ki extends TipoEnergia
//case object Bateria extends TipoEnergia
//

case class Energia(actual: Int, maxima: Int) {
  def aumentar(aumento: Int): Energia = copy(actual = (actual + aumento).min(maxima))

  def cargarAlMaximo: Energia = copy(actual = maxima)

  def modificarMaximo(f: Int => Int): Energia = copy(maxima = f(maxima))
}
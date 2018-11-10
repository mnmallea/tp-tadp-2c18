package dragonball


sealed trait TipoEnergia

case object Ki extends TipoEnergia

case object Bateria extends TipoEnergia

trait ConKi {
  def tipoEnergia: TipoEnergia = Ki
}

trait ConBateria {
  def tipoEnergia: TipoEnergia = Bateria
}


case class Energia(actual: Int, maximo: Int) {

  def setActual(n: Int) = copy(actual = n)

  def aumentar(aumento: Int): Energia = copy(actual = (actual + aumento).min(maximo))

  def disminuir(disminucion: Int): Energia = copy(actual = (actual - disminucion).max(0))

  def disminuirConMinimo(disminucion: Int, unMinimo: Int): Energia = copy(actual = (actual - disminucion).max(unMinimo))

  def cargarAlMaximo: Energia = copy(actual = maximo)

  def modificarMaximo(f: Int => Int): Energia = copy(maximo = f(maximo))

  def fusionadaA(otraEnergia: Energia): Energia = copy(actual = actual + otraEnergia.actual,
    maximo = maximo + otraEnergia.maximo)
}
require_relative '../lib/metamodelo'

class Sumador

  attr_accessor :numero

  def initialize(un_numero = 0)
    self.numero = un_numero
  end

  def aumentar_segun(x)
    matches? x do
      with(type(Integer), :numero) {aumentar_cantidad numero}
      with(type(String), :string) {aumentar_cantidad(string.size)}
      with(list([duck(:+), duck(:+), duck(:+)]), :lista) {aumentar_cantidad(lista.reduce :+)}
      otherwise {imprimir_mensaje_error}
    end
  end

  def imprimir_mensaje_error
    puts "T_T"
  end

  def aumentar_cantidad(una_cantidad)
    self.numero += una_cantidad
  end
end



prueba = Sumador.new
prueba.aumentar_segun 10
prueba.aumentar_segun "lalala"
prueba.aumentar_segun [1, 2, 3]
puts(prueba.numero)
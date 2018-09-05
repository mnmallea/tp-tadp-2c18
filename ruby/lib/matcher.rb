class MatcherFactory
  def self.val(un_objeto)
    proc do
    |otro_objeto|
      otro_objeto == un_objeto
    end
  end

  def self.type(un_tipo)
    proc do
    |un_objeto|
      un_objeto.is_a? un_tipo
    end
  end

  #todo match de tamaño
  def self.list(lista, matchear_tamanio = false)
    tamanio = lista.size
    proc do
    |una_lista|
      type(Array).call(lista) && una_lista.take(tamanio) == lista
    end
  end

  def self.duck(*mensajes)
    proc do
    |un_objeto|
      mensajes_objeto = un_objeto.methods
      mensajes.all? do
      |mensaje|
        mensajes_objeto.include? mensaje
      end
    end
  end

end
class Contexto

  def initialize(objeto_original)
    @objeto_original = objeto_original
  end

  def bind(nombre_variable, objeto = nil )
    singleton_class.send :attr_accessor, nombre_variable
    nombre_setter = (nombre_variable.to_s + "=").to_sym
    send nombre_setter, objeto
  end

  def method_missing(symbol, *args)
    @objeto_original.send symbol, *args
  end

  def respond_to_missing?(symbol, include_all = false)
    @objeto_original.respond_to? symbol, include_all
  end
end
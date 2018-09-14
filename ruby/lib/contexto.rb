class Contexto
  def bind(nombre_variable, objeto = nil )
    singleton_class.send :attr_accessor, nombre_variable
    nombre_setter = (nombre_variable.to_s + "=").to_sym
    send nombre_setter, objeto
  end
end
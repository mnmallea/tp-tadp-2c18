class Contexto
  def bind2(nombre_variable, objeto)
    nombre_variable_instancia = ("@" + nombre_variable.to_s).to_sym
    self.instance_variable_set nombre_variable_instancia, objeto
    self.define_singleton_method nombre_variable do
      instance_variable_get nombre_variable_instancia
    end
    nombre_setter = (nombre_variable.to_s + "=").to_sym
    self.define_singleton_method nombre_setter do
    |nuevo_valor|
      instance_variable_set nombre_variable, nuevo_valor
    end
  end

  def bind(nombre_variable, objeto)
    singleton_class.send :attr_accessor, nombre_variable
    nombre_setter = (nombre_variable.to_s + "=").to_sym
    send nombre_setter, objeto
  end
end
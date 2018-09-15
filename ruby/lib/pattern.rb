module Pattern
  def with(*matchers, &bloque)
    if self.todos_matchean?(matchers)

      if matchers.any? {|m| type(Symbol).call(m)}
        symbols = matchers.select {|m| type(Symbol).call(m)}

        context = Contexto.new
        if type(Array).call(self)
          symbols.each.with_index {|s,i| context.bind(s, self[i])}
        else
          context.bind(symbols[0], self)
        end
        context.instance_exec(&bloque)
      else
        bloque.call
      end

    end
  end

  def otherwise(&bloque)
    bloque.call
  end

  def todos_matchean?(unos_matchers)
    unos_matchers.all? { |matcher| matcher.call(self) }
  end
end

class Contexto
  def bind(nombre_variable, objeto = nil )
    self.class.send(:attr_accessor, nombre_variable)
    nombre_setter = (nombre_variable.to_s + "=").to_sym
    send(nombre_setter, objeto)
  end
end
module Pattern
  def with(un_matcher, *otros_matchers, &bloque)
    matchers = otros_matchers << un_matcher
    contexto = self.configurar_contexto matchers

    if self.todos_matchean matchers
      contexto.instance_exec &bloque
    end
  end

  def otherwise(&bloque)
    bloque.call
  end


  def todos_matchean(unos_matchers)
    unos_matchers.all? {|matcher| matcher.call self}
  end

  def configurar_contexto(matchers)
    contexto = Contexto.new
    matchers.each {|matcher| matcher.bind_to contexto, self}
    contexto
  end
end

module Match
  def matches?(an_object, &block)
    an_object.instance_exec &block
  end
end
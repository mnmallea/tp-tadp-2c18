module Pattern
  def with(un_matcher, *otros_matchers, &bloque)
    matchers = otros_matchers << un_matcher
    if self.todos_matchean matchers
      bloque.call
    end
  end

  def otherwise(&bloque)
    bloque.call
  end

  def todos_matchean(unos_matchers)
    unos_matchers.all? {|matcher| matcher.call self}
  end
end
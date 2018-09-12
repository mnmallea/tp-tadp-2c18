module Pattern
  def with(un_matcher, *otros_matchers, &bloque)
    matchers = otros_matchers << un_matcher
    proc do
    |an_object|
      if self.todos_matchean matchers, an_object
        bloque.call
      end
    end

  end

  def todos_matchean(unos_matchers, an_object)
    unos_matchers.all? {|matcher| matcher.call an_object}
  end

  def otherwise(&bloque)
    bloque.call
  end
end

module Match
  def matches?(an_object, &block)
    block.call
  end
end